package com.example.bankcards.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankcards.entity.BankCard;

public interface BankCardRepository extends JpaRepository<BankCard, Long> {

	BankCard findByMaskedPan(String maskedPan);
}