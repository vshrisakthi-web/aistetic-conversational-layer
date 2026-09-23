package com.aistetic.conversationallayer.repository;

import com.aistetic.conversationallayer.domain.MarketplacePublication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketplacePublicationRepository
        extends JpaRepository<MarketplacePublication, Long> {
}