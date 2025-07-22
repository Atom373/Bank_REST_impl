package com.example.bankcards.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bankcards.controller.payload.BankCardCreateRequest;
import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.service.UserService;

@Mapper(componentModel = "spring", uses = { UserService.class })
public interface BankCardMapper {

	@Mapping(target = "balance", expression = "java(new java.math.BigDecimal(request.balance()))")
	@Mapping(source = "ownerEmail", target = "owner")
	BankCard toEntity(BankCardCreateRequest request);

	@Mapping(target = "ownerEmail", source = "owner.email")
	@Mapping(target = "balance", expression = "java(card.getBalance().toPlainString())")
	BankCardDto toDto(BankCard card);
}