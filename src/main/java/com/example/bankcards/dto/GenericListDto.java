package com.example.bankcards.dto;

import java.util.List;

import lombok.Data;

@Data
public class GenericListDto<T> {

	private final List<T> content;
}
