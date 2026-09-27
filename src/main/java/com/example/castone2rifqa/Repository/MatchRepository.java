package com.example.castone2rifqa.Repository;

import com.example.castone2rifqa.Entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {

    Match findMatchById(Integer id);

    List<Match> findMatchesByUserOneIdOrUserTwoId(Integer userOneId, Integer userTwoId);

    Match findMatchByUserOneIdAndUserTwoId(Integer userOneId, Integer userTwoId);

}