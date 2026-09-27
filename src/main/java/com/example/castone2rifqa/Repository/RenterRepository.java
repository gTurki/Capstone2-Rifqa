package com.example.castone2rifqa.Repository;

import com.example.castone2rifqa.Entity.Renter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenterRepository extends JpaRepository<Renter, Integer> {

    Renter findRenterById(Integer id);

    Renter findRenterByEmail(String email);
}