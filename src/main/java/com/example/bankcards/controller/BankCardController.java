package com.example.bankcards.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.controller.payload.BankCardCreateRequest;
import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.mapper.BankCardMapper;
import com.example.bankcards.service.BankCardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
public class BankCardController {
	
	private final BankCardService cardService;
	private final BankCardMapper cardMapper;

	@GetMapping
	public List<BankCardDto> getAllCards() {
		return cardService
					.getAll()
					.stream()
					.map(cardMapper::toDto)
					.toList();
	}
	
	@PostMapping
	public ResponseEntity<?> createNewCard(@RequestBody BankCardCreateRequest request) {
		BankCard card = cardService.save(cardMapper.toEntity(request));
		return ResponseEntity.ok(cardMapper.toDto(card));
	}
	
	@PostMapping("/{id}/block")
	@ResponseStatus(HttpStatus.OK)
	public void blockCard(@PathVariable Long id) {
		cardService.updateStatus(id, CardStatus.BLOCKED);
	}
	
	@PostMapping("/{id}/activate")
	@ResponseStatus(HttpStatus.OK)
	public void activateCard(@PathVariable Long id) {
		cardService.updateStatus(id, CardStatus.ACTIVE);
	}
	
}