package com.example.bankcards.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.controller.payload.CardCreateRequest;
import com.example.bankcards.controller.payload.RevealCardInfoRequest;
import com.example.bankcards.controller.payload.TransactionRequest;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.DetailedCardDto;
import com.example.bankcards.dto.GenericListDto;
import com.example.bankcards.dto.RevealedCardInfoDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.mapper.DetailedCardMapper;
import com.example.bankcards.service.CardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CardController {
	
	private final CardService cardService;
	private final CardMapper cardMapper;
	private final DetailedCardMapper detailedCardMapper;

	@GetMapping("/cards")
	public ResponseEntity<?> getUserCards(@RequestParam(required = false) Integer page,
		 	 							  @RequestParam(required = false) Integer size,
		 	 							  @AuthenticationPrincipal Long userId) {
		if (page == null || size == null) {
			List<CardDto> cards = cardService.getAllByOwnerId(userId)
												 .stream()
												 .map(cardMapper::toDto)
												 .toList();
			// GenericListDto is used to ensure that both responses contain a "content" field.
			return ResponseEntity.ok(new GenericListDto<CardDto>(cards)); 
		}
		return ResponseEntity.ok(
				cardService.getAllByOwnerId(userId, PageRequest.of(page, size))
							.map(cardMapper::toDto)
		);
	}
	
	@GetMapping("/cards/{cardId}")
	public DetailedCardDto getCardById(@PathVariable Long cardId,
										   @AuthenticationPrincipal Long userId) {
		Card card = cardService.getById(cardId, userId);
		return detailedCardMapper.toDto(card);
	}	
	
	@PostMapping("/cards/{cardId}/reveal")
	public RevealedCardInfoDto revealCardInfo(@PathVariable Long cardId,
											  @AuthenticationPrincipal Long userId,
											  @RequestBody RevealCardInfoRequest request) {
		return cardService.revealCardInfo(cardId, userId, request.password());
	}	
	
	@PostMapping("/cards/transfer")
	@ResponseStatus(HttpStatus.OK)
	public void transfer(@AuthenticationPrincipal Long userId,
			 			 @RequestBody TransactionRequest request) {
		cardService.transfer(request, userId);
	}
	
	@GetMapping("/admin/cards")
	public List<CardDto> getAllCards() {
		return cardService
					.getAll()
					.stream()
					.map(cardMapper::toDto)
					.toList();
	}
	
	@PostMapping("/admin/cards")
	public ResponseEntity<?> createNewCard(@RequestBody CardCreateRequest request) {
		Card card = cardService.save(cardMapper.toEntity(request));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(cardMapper.toDto(card));
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