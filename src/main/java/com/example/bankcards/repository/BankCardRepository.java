package com.example.bankcards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankcards.entity.BankCard;

public interface BankCardRepository extends JpaRepository<BankCard, Long> {

	BankCard findByEncryptedPan(String maskedPan);
	
	List<BankCard> findAllByOwnerId(Long ownerId);
}