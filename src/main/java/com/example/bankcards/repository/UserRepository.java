package com.example.bankcards.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankcards.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);
	
	Boolean existsByEmail(String email);
}