package com.aistetic.conversationallayer.repository;

import com.aistetic.conversationallayer.domain.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long> {

    long countByStatus(String status);
}