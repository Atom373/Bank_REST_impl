package com.example.bankcards.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bankcards.dto.DetailedBankCardDto;
import com.example.bankcards.entity.BankCard;

@Mapper(componentModel = "spring")
public interface DetailedBankCardMapper {

	@Mapping(target = "balance", expression = "java(card.getBalance().toPlainString())")
	DetailedBankCardDto toDto(BankCard card);
}
