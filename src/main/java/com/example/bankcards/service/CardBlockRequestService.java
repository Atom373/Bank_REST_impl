package com.example.bankcards.service;

import java.util.List;

import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.enums.BlockRequestStatus;

public interface CardBlockRequestService {

	List<CardBlockRequest> getAll();
	
	List<CardBlockRequest> getByStatus(BlockRequestStatus stasus);
	
	void createRequest(Long cardTiBlockId);
}
