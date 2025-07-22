package com.example.bankcards.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.bankcards.util.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter{

	private JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
		
		String jwt  = null;
		Long userId = null;
		String role = null;
		
		jwt = jwtUtils.extractTokenFromRequest(request);
		
		if (jwt != null) {
			try {
				Claims claims = jwtUtils.getClaimsFromToken(jwt);
				userId = claims.get("id", Long.class);
				role = claims.get("role", String.class);
			} catch (SignatureException | ExpiredJwtException ex) {
				log.info("Error during jwt parsing: " + ex.getMessage());
			}
		}
		
		log.debug(jwt);
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