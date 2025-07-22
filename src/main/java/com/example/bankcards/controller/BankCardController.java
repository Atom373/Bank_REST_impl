package com.example.bankcards.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.controller.payload.BankCardCreateRequest;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.mapper.BankCardMapper;
import com.example.bankcards.service.BankCardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
public class BankCardController {
	
	private final BankCardService cardService;
	private final BankCardMapper cardMapper;

	@PostMapping
	public ResponseEntity<?> createNewCard(@RequestBody BankCardCreateRequest request) {
		System.out.println(request);
		BankCard card = cardService.save(cardMapper.toEntity(request));
		return ResponseEntity.ok(cardMapper.toDto(card));
	}
}