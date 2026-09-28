package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.DTO.MatchSuggestion;
import com.example.capstone2rifqa.Entity.AgreementStatus;
import com.example.capstone2rifqa.Entity.Match;
import com.example.capstone2rifqa.Entity.MatchStatus;
import com.example.capstone2rifqa.Entity.User;
import com.example.capstone2rifqa.Entity.UserStatus;
import com.example.capstone2rifqa.Repository.AgreementRepository;
import com.example.capstone2rifqa.Repository.MatchRepository;
import com.example.capstone2rifqa.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    // Keeps the AI prompt a reasonable size
    private static final int MAX_CANDIDATES = 50;

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final AgreementRepository agreementRepository;
    private final AiService aiService;
    private final EmailService emailService;

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Match getMatchById(Integer id) {
        Match match = matchRepository.findMatchById(id);
        if (match == null) {
            throw new ApiException("Match not found with ID: " + id);
        }
        return match;
    }

    // A user sees their own suggestions and every request or match they are part of,
    // but not the suggestions other users received about them
    public List<Match> getMatchesByUserId(Integer userId) {
        getUser(userId);
        List<Match> matches = matchRepository.findVisibleMatchesByUserId(userId, MatchStatus.SUGGESTED);
        if (matches.isEmpty()) {
            throw new ApiException("This user has no matches yet");
        }
        return matches;
    }

    public List<Match> getIncomingRequests(Integer userId) {
        getUser(userId);
        List<Match> requests = matchRepository.findMatchesByUserTwoIdAndStatus(userId, MatchStatus.PENDING);
        if (requests.isEmpty()) {
            throw new ApiException("This user has no incoming match requests");
        }
        return requests;
    }

    // The AI picks the best candidate and it is saved as SUGGESTED. Only the latest suggestion is kept.
    public MatchSuggestion suggestMatch(Integer userId) {
        User user = getUser(userId);
        if (user.getStatus() != UserStatus.LOOKING) {
            throw new ApiException("User must have status LOOKING to receive match suggestions");
        }

        // Skip anyone who already has a pending request with this user, in either direction
        List<Integer> pendingWith = new ArrayList<>();
        for (Match pending : matchRepository.findMatchesByUserIdAndStatus(userId, MatchStatus.PENDING)) {
            if (pending.getUserOneId().equals(userId)) {
                pendingWith.add(pending.getUserTwoId());
            } else {
                pendingWith.add(pending.getUserOneId());
            }
        }

        List<User> candidates = new ArrayList<>();
        for (User candidate : userRepository.findMatchCandidates(user.getCity(), user.getGender(), UserStatus.LOOKING, userId)) {
            if (!pendingWith.contains(candidate.getId())) {
                candidates.add(candidate);
            }
        }
        if (candidates.size() > MAX_CANDIDATES) {
            candidates = candidates.subList(0, MAX_CANDIDATES);
        }
        if (candidates.isEmpty()) {
            throw new ApiException("No compatible roommates are currently looking in " + user.getCity());
        }

        MatchSuggestion suggestion = aiService.suggestBestMatch(user, candidates);

        // Make sure the AI picked someone who was actually in the candidate list
        User suggestedUser = null;
        for (User candidate : candidates) {
            if (candidate.getId().equals(suggestion.getSuggestedUserId())) {
                suggestedUser = candidate;
                break;
            }
        }
        if (suggestedUser == null) {
            throw new ApiException("AI returned an invalid suggestion, please try again");
        }

        // Removed only after the AI succeeds, so a failed call doesn't lose the old suggestion
        matchRepository.deleteAll(matchRepository.findMatchesByUserOneIdAndStatus(userId, MatchStatus.SUGGESTED));

        Match match = new Match();
        match.setUserOneId(userId);
        match.setUserTwoId(suggestedUser.getId());
        match.setCompatibilityScore(suggestion.getCompatibilityScore());
        match.setAiReasoning(suggestion.getAiReasoning());
        match.setStatus(MatchStatus.SUGGESTED);
        matchRepository.save(match);

        suggestion.setMatchId(match.getId());
        suggestion.setUserId(userId);
        suggestion.setSuggestedUserName(suggestedUser.getName());
        return suggestion;
    }

    // userOne sends the suggestion to userTwo as a request: SUGGESTED -> PENDING
    public Boolean sendRequest(Integer matchId, Integer userId) {
        Match match = getMatchById(matchId);
        if (!match.getUserOneId().equals(userId)) {
            throw new ApiException("Only the user who received this suggestion can send the request");
        }
        if (match.getStatus() != MatchStatus.SUGGESTED) {
            throw new ApiException("This match is already " + match.getStatus());
        }

        User sender = getUser(match.getUserOneId());
        User receiver = getUser(match.getUserTwoId());
        if (sender.getStatus() != UserStatus.LOOKING) {
            throw new ApiException("You must have status LOOKING to send a match request");
        }
        if (receiver.getStatus() != UserStatus.LOOKING) {
            throw new ApiException("This user is no longer looking for a roommate");
        }

        // The other user may have sent a request to this user after the suggestion was made
        Match existing = matchRepository.findMatchBetweenUsersByStatus(sender.getId(), receiver.getId(), MatchStatus.PENDING);
        if (existing != null) {
            throw new ApiException("There is already a pending request between you and this user");
        }

        match.setStatus(MatchStatus.PENDING);
        matchRepository.save(match);

        sendRequestEmail(receiver, sender, match.getCompatibilityScore());
        return true;
    }

    // userTwo accepts: PENDING -> CONFIRMED, both users become MATCHED
    public Boolean acceptRequest(Integer matchId, Integer userId) {
        Match match = getPendingMatchForReceiver(matchId, userId);

        User sender = getUser(match.getUserOneId());
        User receiver = getUser(match.getUserTwoId());
        if (sender.getStatus() != UserStatus.LOOKING || receiver.getStatus() != UserStatus.LOOKING) {
            throw new ApiException("Both users must still have status LOOKING to be matched");
        }

        match.setStatus(MatchStatus.CONFIRMED);
        matchRepository.save(match);

        sender.setStatus(UserStatus.MATCHED);
        receiver.setStatus(UserStatus.MATCHED);
        userRepository.save(sender);
        userRepository.save(receiver);

        // Other suggestions and requests of these two users are no longer needed
        removeOpenMatches(sender.getId());
        removeOpenMatches(receiver.getId());

        sendMatchEmail(sender, receiver, match.getCompatibilityScore());
        sendMatchEmail(receiver, sender, match.getCompatibilityScore());
        return true;
    }

    // userTwo declines: the request is removed
    public Boolean declineRequest(Integer matchId, Integer userId) {
        Match match = getPendingMatchForReceiver(matchId, userId);

        matchRepository.delete(match);
        return true;
    }

    // Either user can cancel an open request or unmatch a confirmed one.
    // A suggestion is private to the user it was made for, so only userOne can remove it.
    public Boolean deleteMatch(Integer matchId, Integer userId) {
        Match match = getMatchById(matchId);

        boolean isUserOne = match.getUserOneId().equals(userId);
        boolean isUserTwo = match.getUserTwoId().equals(userId) && match.getStatus() != MatchStatus.SUGGESTED;
        if (!isUserOne && !isUserTwo) {
            throw new ApiException("You are not part of this match");
        }

        // Roommates living together under an agreement can't unmatch until it is ended
        if (match.getStatus() == MatchStatus.CONFIRMED
                && !agreementRepository.findAgreementsBetweenUsersByStatusNot(match.getUserOneId(), match.getUserTwoId(), AgreementStatus.TERMINATED).isEmpty()) {
            throw new ApiException("Terminate or delete your agreement before removing this match");
        }

        matchRepository.delete(match);

        // Unmatching puts both users back into the LOOKING pool
        if (match.getStatus() == MatchStatus.CONFIRMED) {
            resetToLooking(match.getUserOneId());
            resetToLooking(match.getUserTwoId());
        }
        return true;
    }

    // Shared by accept and decline: the match exists, this user received it, and it is waiting for a response
    private Match getPendingMatchForReceiver(Integer matchId, Integer userId) {
        Match match = getMatchById(matchId);
        if (!match.getUserTwoId().equals(userId)) {
            throw new ApiException("Only the user who received this request can accept or decline it");
        }
        if (match.getStatus() != MatchStatus.PENDING) {
            throw new ApiException("Only pending requests can be accepted or declined");
        }
        return match;
    }

    private User getUser(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found with ID: " + userId);
        }
        return user;
    }

    private void removeOpenMatches(Integer userId) {
        matchRepository.deleteAll(matchRepository.findMatchesByUserIdAndStatusNot(userId, MatchStatus.CONFIRMED));
    }

    private void resetToLooking(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user != null && user.getStatus() == UserStatus.MATCHED) {
            user.setStatus(UserStatus.LOOKING);
            userRepository.save(user);
        }
    }

    // The phone number is not shared until the request is accepted
    private void sendRequestEmail(User recipient, User sender, Double score) {
        emailService.sendEmail(
                recipient.getEmail(),
                "Rifqa - You have a new roommate request",
                "Hi " + recipient.getName() + ",\n\n"
                        + sender.getName() + " would like to be your roommate. "
                        + "Your compatibility score is " + Math.round(score) + "%.\n"
                        + "Open Rifqa to accept or decline the request.\n\n"
                        + "The Rifqa Team"
        );
    }

    private void sendMatchEmail(User recipient, User roommate, Double score) {
        emailService.sendEmail(
                recipient.getEmail(),
                "Rifqa - You have a new roommate match!",
                "Hi " + recipient.getName() + ",\n\n"
                        + "You have been matched with " + roommate.getName()
                        + " with a compatibility score of " + Math.round(score) + "%.\n"
                        + "You can reach them at " + roommate.getPhoneNumber() + ".\n\n"
                        + "The Rifqa Team"
        );
    }
}