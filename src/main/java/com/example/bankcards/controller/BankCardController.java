package com.example.bankcards.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.bankcards.controller.payload.BankCardCreateRequest;
import com.example.bankcards.controller.payload.RevealCardInfoRequest;
import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.dto.DetailedBankCardDto;
import com.example.bankcards.dto.RevealedCardInfoDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.mapper.BankCardMapper;
import com.example.bankcards.mapper.DetailedBankCardMapper;
import com.example.bankcards.service.BankCardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BankCardController {
	
	private final BankCardService cardService;
	private final BankCardMapper cardMapper;
	private final DetailedBankCardMapper detailedCardMapper;

	@GetMapping("/cards")
	public List<BankCardDto> getUserCards(@AuthenticationPrincipal Long userId) {
		return cardService
					.getAllByOwnerId(userId)
					.stream()
					.map(cardMapper::toDto)
					.toList();
	}
	
	@GetMapping("/cards/{cardId}")
	public DetailedBankCardDto getCardById(@PathVariable Long cardId,
										   @AuthenticationPrincipal Long userId) {
		BankCard card = cardService.getById(cardId, userId);
		return detailedCardMapper.toDto(card);
	}	
	
	@PostMapping("/cards/{cardId}/reveal")
	public RevealedCardInfoDto revealPan(@PathVariable Long cardId,
										 @AuthenticationPrincipal Long userId,
										 @RequestBody RevealCardInfoRequest request) {
		return cardService.revealCardInfo(cardId, userId, request.password());
	}	
	
	@GetMapping("/admin/cards")
	public List<BankCardDto> getAllCards() {
		return cardService
					.getAll()
					.stream()
					.map(cardMapper::toDto)
					.toList();
	}
	
	@PostMapping("/admin/cards")
	public ResponseEntity<?> createNewCard(@RequestBody BankCardCreateRequest request) {
		BankCard card = cardService.save(cardMapper.toEntity(request));
		return ResponseEntity.ok(cardMapper.toDto(card));
	}
	
	@PostMapping("/admin/cards/{id}/block")
	@ResponseStatus(HttpStatus.OK)
	public void blockCard(@PathVariable Long id) {
		cardService.updateStatus(id, CardStatus.BLOCKED);
	}
	
	@PostMapping("/admin/cards/{id}/activate")
	@ResponseStatus(HttpStatus.OK)
	public void activateCard(@PathVariable Long id) {
		cardService.updateStatus(id, CardStatus.ACTIVE);
	}
	
	@DeleteMapping("/admin/cards/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteCard(@PathVariable Long id) {
		cardService.deleteById(id);
	}
}