package com.example.bankcards.service;

import java.util.List;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserAlreadyExistsException;

public interface UserService {
	
	User save(User user) throws UserAlreadyExistsException;
	
	User findById(Long id);
	
	User findByEmail(String email);
	
	List<User> findAll();

}