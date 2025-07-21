package com.example.bankcards.entity;

import com.example.bankcards.enums.BlockRequestStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "card_block_requests")
public class CardBlockRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	Long cardToBlockId;
	BlockRequestStatus status;
}