package com.example.bankcards.util;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.bankcards.enums.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

@Component
@Getter
public class JwtUtils {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.lifetime}")
	private Duration lifetime;
	
	public String generateToken(Long id, UserRole role) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", id);
		claims.put("role", role.name());
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + lifetime.toMillis()))
				.signWith(Keys.hmacShaKeyFor(secret.getBytes()))
				.compact();
	}
	
	public Long getIdFromToken(String token) {
		return getClaimsFromToken(token).get("id", Long.class);
	}
	
	public Claims getClaimsFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
}