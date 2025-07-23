package com.example.bankcards.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.enums.BlockRequestStatus;
import com.example.bankcards.service.CardBlockRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CardBlockRequestController {
	
	private final CardBlockRequestService cardBlockRequestService; 

	@GetMapping("/admin/block-requests")
	public List<CardBlockRequest> getBlockRequests(@RequestParam(required = false) String status) {
		if (status == null) {
			return cardBlockRequestService.getAll();
		} 
		return cardBlockRequestService.getByStatus(
				BlockRequestStatus.valueOf(status.toUpperCase())
		);
	}
	
	@PostMapping("/cards/{cardId}/block-requests")
	@ResponseStatus(HttpStatus.CREATED)
	public void createBlockRequest(@PathVariable Long cardId) {
		cardBlockRequestService.createRequest(cardId);
	}
}
