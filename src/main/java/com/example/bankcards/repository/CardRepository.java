package com.example.bankcards.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankcards.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

	Optional<Card> findByEncryptedPan(String encryptedPan);
	
	List<Card> findAllByOwnerId(Long ownerId);
	
	Page<Card> findAllByOwnerId(Long id, Pageable pageable);
}