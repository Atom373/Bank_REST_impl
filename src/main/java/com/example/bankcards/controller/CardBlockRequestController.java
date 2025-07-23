package com.example.bankcards.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.service.CardBlockRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CardBlockRequestController {
	
	private final CardBlockRequestService cardBlockRequestService; 

	@PostMapping("/cards/{cardId}/block-requests")
	@ResponseStatus(HttpStatus.CREATED)
	public void createBlockRequest(@PathVariable Long cardId) {
		cardBlockRequestService.createRequest(cardId);
	}
}
