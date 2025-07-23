package com.example.bankcards.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankcards.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Page<User> findAll(Pageable pageable);

	Optional<User> findByEmail(String email);
	
	Boolean existsByEmail(String email);
}