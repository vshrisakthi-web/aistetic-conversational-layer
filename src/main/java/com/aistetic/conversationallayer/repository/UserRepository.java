package com.aistetic.conversationallayer.repository;

import com.aistetic.conversationallayer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}