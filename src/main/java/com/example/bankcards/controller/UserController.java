package com.example.bankcards.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.controller.payload.UserCreateRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.service.UserService;

import jakarta.validation.Valid;
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
	
	@PostMapping
	public ResponseEntity<?> createNewUser(@Valid @RequestBody UserCreateRequest request) {
		User createdUser = userService.save(userMapper.toEntity(request));
		return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(Map.of("id", createdUser.getId()));
	}
	
}
