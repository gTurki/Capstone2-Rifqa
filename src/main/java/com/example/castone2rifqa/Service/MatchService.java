package com.example.castone2rifqa.Service;

import com.example.castone2rifqa.Api.ApiException;
import com.example.castone2rifqa.DTO.MatchSuggestion;
import com.example.castone2rifqa.Entity.Match;
import com.example.castone2rifqa.Entity.User;
import com.example.castone2rifqa.Entity.UserStatus;
import com.example.castone2rifqa.Repository.MatchRepository;
import com.example.castone2rifqa.Repository.UserRepository;
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

    public List<Match> getMatchesByUserId(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found with ID: " + userId);
        }
        List<Match> matches = matchRepository.findMatchesByUserOneIdOrUserTwoId(userId, userId);
        if (matches.isEmpty()) {
            throw new ApiException("This user has no matches yet");
        }
        return matches;
    }

    // Asks the AI for the best candidate. Nothing is saved here.
    public MatchSuggestion suggestMatch(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found with ID: " + userId);
        }
        if (user.getStatus() != UserStatus.LOOKING) {
            throw new ApiException("User must have status LOOKING to receive match suggestions");
        }

        List<User> candidates = new ArrayList<>();
        for (User candidate : userRepository.findUsersByCityAndStatus(user.getCity(), UserStatus.LOOKING)) {
            if (candidate.getId().equals(userId)) {
                continue;
            }
            if (!candidate.getGender().equalsIgnoreCase(user.getGender())) {
                continue;
            }
            candidates.add(candidate);
            if (candidates.size() >= MAX_CANDIDATES) {
                break;
            }
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

        suggestion.setUserId(userId);
        suggestion.setSuggestedUserName(suggestedUser.getName());
        return suggestion;
    }

    // Saves the pairing after the user approves the suggestion
    public Boolean confirmMatch(Match match) {
        if (match.getUserOneId().equals(match.getUserTwoId())) {
            throw new ApiException("A user cannot be matched with themselves");
        }

        User userOne = userRepository.findUserById(match.getUserOneId());
        if (userOne == null) {
            throw new ApiException("User not found with ID: " + match.getUserOneId());
        }
        User userTwo = userRepository.findUserById(match.getUserTwoId());
        if (userTwo == null) {
            throw new ApiException("User not found with ID: " + match.getUserTwoId());
        }

        if (userOne.getStatus() != UserStatus.LOOKING || userTwo.getStatus() != UserStatus.LOOKING) {
            throw new ApiException("Both users must have status LOOKING to be matched");
        }

        Match existing = matchRepository.findMatchByUserOneIdAndUserTwoId(match.getUserOneId(), match.getUserTwoId());
        if (existing == null) {
            existing = matchRepository.findMatchByUserOneIdAndUserTwoId(match.getUserTwoId(), match.getUserOneId());
        }
        if (existing != null) {
            throw new ApiException("These users are already matched");
        }

        matchRepository.save(match);

        userOne.setStatus(UserStatus.MATCHED);
        userTwo.setStatus(UserStatus.MATCHED);
        userRepository.save(userOne);
        userRepository.save(userTwo);

        sendMatchEmail(userOne, userTwo, match.getCompatibilityScore());
        sendMatchEmail(userTwo, userOne, match.getCompatibilityScore());
        return true;
    }

    // Unmatching puts both users back into the LOOKING pool
    public Boolean deleteMatch(Integer id) {
        Match match = matchRepository.findMatchById(id);
        if (match == null) {
            throw new ApiException("Match not found with ID: " + id);
        }

        matchRepository.delete(match);

        resetToLooking(match.getUserOneId());
        resetToLooking(match.getUserTwoId());
        return true;
    }

    private void resetToLooking(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user != null && user.getStatus() == UserStatus.MATCHED) {
            user.setStatus(UserStatus.LOOKING);
            userRepository.save(user);
        }
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