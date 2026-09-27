package com.example.castone2rifqa.Repository;

import com.example.castone2rifqa.Entity.ListingRequest;
import com.example.castone2rifqa.Entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRequestRepository extends JpaRepository<ListingRequest, Integer> {

    ListingRequest findListingRequestById(Integer id);

    List<ListingRequest> findListingRequestsByListingId(Integer listingId);

    List<ListingRequest> findListingRequestsByRequesterId(Integer requesterId);

    List<ListingRequest> findListingRequestsByListingIdAndStatus(Integer listingId, RequestStatus status);

    ListingRequest findListingRequestByListingIdAndRequesterId(Integer listingId, Integer requesterId);

}