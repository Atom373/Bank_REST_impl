package com.example.bankcards.entity;

import com.example.bankcards.enums.BlockRequestStatus;
import com.example.bankcards.enums.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor()
@Table(name = "card_block_requests")
public class CardBlockRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long cardToBlockId;
	
	@Enumerated(EnumType.STRING)
	private BlockRequestStatus status;
}