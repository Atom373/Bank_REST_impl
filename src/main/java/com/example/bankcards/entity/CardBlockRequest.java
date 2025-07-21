package com.example.bankcards.entity;

import com.example.bankcards.enums.BlockRequestStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class CardBlockRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	Long cardToBlockId;
	BlockRequestStatus status;
}