package com.example.capstone2rifqa.Repository;

import com.example.capstone2rifqa.Entity.Match;
import com.example.capstone2rifqa.Entity.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {

    Match findMatchById(Integer id);

    List<Match> findMatchesByUserOneIdAndStatus(Integer userOneId, MatchStatus status);

    List<Match> findMatchesByUserTwoIdAndStatus(Integer userTwoId, MatchStatus status);

    // Everything the user started, plus requests and matches they received
    @Query("select m from Match m where m.userOneId = ?1 or (m.userTwoId = ?1 and m.status <> ?2)")
    List<Match> findVisibleMatchesByUserId(Integer userId, MatchStatus hiddenStatus);

    @Query("select m from Match m where (m.userOneId = ?1 or m.userTwoId = ?1) and m.status = ?2")
    List<Match> findMatchesByUserIdAndStatus(Integer userId, MatchStatus status);

    @Query("select m from Match m where (m.userOneId = ?1 or m.userTwoId = ?1) and m.status <> ?2")
    List<Match> findMatchesByUserIdAndStatusNot(Integer userId, MatchStatus status);

    // we check both orders because user1 can be first or user2
    @Query("select m from Match m where ((m.userOneId = ?1 and m.userTwoId = ?2) or (m.userOneId = ?2 and m.userTwoId = ?1)) and m.status = ?3")
    Match findMatchBetweenUsersByStatus(Integer userOneId, Integer userTwoId, MatchStatus status);
}