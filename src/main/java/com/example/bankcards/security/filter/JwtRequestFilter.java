package com.example.bankcards.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.bankcards.exception.InvalidJwtException;
import com.example.bankcards.util.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter{

	private JwtUtils jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		
		String jwt  = null;
		Long userId = null;
		String role = null;
		
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			jwt = authHeader.substring(7);
			try {
				Claims claims = jwtUtil.getClaimsFromToken(jwt);
				userId = claims.get("id", Long.class);
				role = claims.get("role", String.class);
			} catch (SignatureException | ExpiredJwtException ex) {
				throw new InvalidJwtException("Error during jwt parsing: " + ex.getMessage());
			}
		}
		
		if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					userId, 
					null,
					List.of(new SimpleGrantedAuthority(role))
			);
			SecurityContextHolder.getContext().setAuthentication(token);
		}
		
		filterChain.doFilter(request, response);
	}
}