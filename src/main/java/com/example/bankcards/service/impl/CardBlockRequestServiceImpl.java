package com.example.bankcards.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.enums.BlockRequestStatus;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.service.CardBlockRequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardBlockRequestServiceImpl implements CardBlockRequestService {

	private final CardBlockRequestRepository cardBlockRequestRepository;
	
	@Override
	public List<CardBlockRequest> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CardBlockRequest> getByStatus(BlockRequestStatus stasus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createRequest(Long cardToBlockId) {
		CardBlockRequest request = new CardBlockRequest();
		request.setCardToBlockId(cardToBlockId);
		request.setStatus(BlockRequestStatus.WAITING);
		cardBlockRequestRepository.save(request);
	}

}
