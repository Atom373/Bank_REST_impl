package com.example.bankcards.service.impl;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bankcards.entity.User;
import com.example.bankcards.enums.UserRole;
import com.example.bankcards.exception.UserAlreadyExistsException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder; 
	
	@Override
	public User save(User user) throws UserAlreadyExistsException {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setRole(UserRole.ROLE_USER);
		try {
			return userRepository.save(user);
		} catch (DataIntegrityViolationException ex) {
			throw new UserAlreadyExistsException("This email is already in use");
		}
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id).orElseThrow( 
					() -> new UserNotFoundException("User with id = %s not found".formatted(id)) 
				);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow( 
				() -> new UserNotFoundException("User with email = %s not found".formatted(email)) 
			);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

}
