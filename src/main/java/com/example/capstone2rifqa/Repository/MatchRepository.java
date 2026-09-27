package com.example.capstone2rifqa.Repository;

import com.example.capstone2rifqa.Entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {

    Match findMatchById(Integer id);

    @Query("select m from Match m where m.userOneId = ?1 or m.userTwoId = ?1")
    List<Match> findMatchesByUserId(Integer userId);

    // A match can be stored as (A, B) or (B, A), so check both orders
    @Query("select m from Match m where (m.userOneId = ?1 and m.userTwoId = ?2) or (m.userOneId = ?2 and m.userTwoId = ?1)")
    Match findMatchBetweenUsers(Integer userOneId, Integer userTwoId);
}