package com.example.bankcards.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.enums.BlockRequestStatus;
import com.example.bankcards.exception.CardBlockRequestNotFoundException;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.service.CardBlockRequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardBlockRequestServiceImpl implements CardBlockRequestService {

	private final CardBlockRequestRepository cardBlockRequestRepository;
	
	@Override
	public List<CardBlockRequest> getAll() {
		return cardBlockRequestRepository.findAll();
	}

	@Override
	public List<CardBlockRequest> getAllByStatus(BlockRequestStatus status) {
		return this.getAll()
					.stream()
					.filter(req -> req.getStatus() == status)
					.toList();
	}

	@Override
	public void createRequest(Long cardToBlockId) {
		CardBlockRequest request = new CardBlockRequest();
		request.setCardToBlockId(cardToBlockId);
		request.setStatus(BlockRequestStatus.WAITING);
		cardBlockRequestRepository.save(request);
	}

	@Override
	public void updateStatus(Long id, BlockRequestStatus stasus) {
		CardBlockRequest request = cardBlockRequestRepository.findById(id).orElseThrow(
				() -> new CardBlockRequestNotFoundException("Block request not found")
		);
		request.setStatus(stasus);
		cardBlockRequestRepository.save(request);
	}

}
