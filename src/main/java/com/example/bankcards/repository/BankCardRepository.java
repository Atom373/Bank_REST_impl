package com.example.bankcards.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankcards.entity.BankCard;

public interface BankCardRepository extends JpaRepository<BankCard, Long> {

	Optional<BankCard> findByEncryptedPan(String encryptedPan);
	
	List<BankCard> findAllByOwnerId(Long ownerId);
}