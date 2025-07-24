package com.example.bankcards.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bankcards.controller.payload.CardCreateRequest;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.service.UserService;

@Mapper(componentModel = "spring", uses = { UserService.class })
public interface CardMapper {

	@Mapping(target = "balance", expression = "java(new java.math.BigDecimal(request.balance()))")
	@Mapping(source = "ownerEmail", target = "owner")
	Card toEntity(CardCreateRequest request);

	@Mapping(target = "ownerEmail", source = "owner.email")
	CardDto toDto(Card card);
}