package com.accountingsystem.configs.jwt;

import com.accountingsystem.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

	private final JwtProperties jwtProperties;

	public JwtUtils(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		SecretKey jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));

		return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtProperties.getExpirationMs()))
				.signWith(jwtAccessSecret)
				.compact();
	}

	public boolean validateJwtToken(String jwt) throws InvalidKeyException {
		try {
			Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(jwt);
			return true;
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			throw new InvalidKeyException("Invalid jwt token");
		}
	}

	public String getUserNameFromJwtToken(String jwt) {
		return Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(jwt).getBody().getSubject();
	}

}
