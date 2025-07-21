package com.example.bankcards.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankcards.entity.CardBlockRequest;

public interface CardBlockRequestRepository extends JpaRepository<CardBlockRequest, Long> {}