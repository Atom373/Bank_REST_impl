package com.example.bankcards.mapper;

import org.mapstruct.Mapper;

import com.example.bankcards.controller.payload.UserCreateRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserDto toDto(User user);
	
	User toEntity(UserCreateRequest request);
}
