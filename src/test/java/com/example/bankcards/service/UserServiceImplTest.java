package com.example.bankcards.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserAlreadyExistsException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.UserServiceImpl;
import com.example.bankcards.utils.DataUtils;

@ExtendWith(MockitoExtension.class) 
public class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder; 
	
	@InjectMocks
	private UserServiceImpl userService; 
	
	@Test
	public void save_shouldSaveGivenEntity() {
		// given
		User user = DataUtils.getUser();
		when(passwordEncoder.encode(anyString())).thenReturn(user.getPassword());
		when(userRepository.save(any(User.class))).thenReturn(user);
		
		// when 
		User savedUser = userService.save(DataUtils.getUserForSaving());
		
		// then
		assertEquals(user, savedUser);
	}
	
	@Test
	public void save_emailIsNotUnique_shouldThrowException() {
		// given
		User user = DataUtils.getUser();
		when(passwordEncoder.encode(anyString())).thenReturn(user.getPassword());
		when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);
		
		// then
		assertThrows(
				UserAlreadyExistsException.class, 
				() -> userService.save(DataUtils.getUserForSaving())
		);
	}
	
	@Test
	public void getById_shouldReturnEntityById() {
		// given
		User user = DataUtils.getUser();
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		
		// when 
		User retrievedUser = userService.getById(user.getId());
		
		// then
		assertEquals(user, retrievedUser);
	}
}
