package com.example.bankcards.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bankcards.dto.DetailedCardDto;
import com.example.bankcards.entity.Card;

@Mapper(componentModel = "spring")
public interface DetailedCardMapper {

	@Mapping(target = "balance", expression = "java(card.getBalance().toPlainString())")
	DetailedCardDto toDto(Card card);
}
