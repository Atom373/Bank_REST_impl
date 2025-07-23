package com.example.bankcards.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserAlreadyExistsException;

public interface UserService {
	
	User save(User user) throws UserAlreadyExistsException;
	
	User getById(Long id);
	
	User getByEmail(String email);
	
	List<User> getAll();
	
	Page<User> getAll(Pageable pageable);

	void deleteUserById(Long id);
}