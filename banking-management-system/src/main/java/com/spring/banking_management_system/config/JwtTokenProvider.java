package com.spring.banking_management_system.config;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
	private String JwtSecret;

	@Value("${app.jwt-expiration}")
	private long jwtExpirationDate;

	public String generateToken(Authentication authentication) {

		String username = authentication.getName();

		Date currentDate = new Date();

		Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

		return Jwts.builder().setSubject(username)

				.setIssuedAt(expireDate)

				.setExpiration(expireDate)

				.signWith(key()).compact();

	}

	private Key key() {

		byte[] bytes = Decoders.BASE64.decode(JwtSecret);

		return Keys.hmacShaKeyFor(bytes);

	}

	public String getUsername(String token) {

		Claims claims = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();

		return claims.getSubject();

	}

	public boolean validateToken(String token) {
	    try {
	        Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
	        return true;
	    } catch (ExpiredJwtException ex) {
	        throw new RuntimeException("-----TOKEN IS EXPIRED----- "+ex.getMessage());
	    } catch (JwtException | IllegalArgumentException e) {
	        throw new RuntimeException("------INVALID TOKEN------ " + e.getMessage());
	    }
	}



}
