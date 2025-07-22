package com.example.bankcards.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;
	private final UserMapper userMapper;
	
	@GetMapping
	public List<UserDto> getAllUsers() {
		return userService
					.findAll()
					.stream()
					.map(userMapper::toDto)
					.toList();
	}
	
}
