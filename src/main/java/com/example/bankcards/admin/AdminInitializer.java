package com.example.bankcards.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.bankcards.entity.User;
import com.example.bankcards.enums.UserRole;
import com.example.bankcards.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

	@Value("${app.admin.email}")
	private String adminEmail;
	
	@Value("${bc.admin.password}") // Environment variable
	private String adminPassword;
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder; 
	
	@Override
	public void run(String... args) throws Exception {
		if (userRepository.existsByEmail(adminEmail))
			return;
		User admin = new User();
		admin.setEmail(adminEmail);
		admin.setPassword(passwordEncoder.encode(adminPassword));
		admin.setRole(UserRole.ROLE_ADMIN);
		userRepository.save(admin);
	}

}