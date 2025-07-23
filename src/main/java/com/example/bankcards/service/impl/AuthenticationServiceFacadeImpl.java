package com.example.bankcards.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bankcards.controller.payload.AuthRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.AuthenticationServiceFacade;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceFacadeImpl implements AuthenticationServiceFacade {

	private final JwtUtils jwtUtils;
	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	
	@Override
	@Transactional(readOnly = true)
	public String authenticate(AuthRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.email(),
						request.password()
				)
		); 	
		User user = userService.getByEmail(request.email());
		String jwt = jwtUtils.generateToken(user.getId(), user.getRole());
		return jwt;
	}

}