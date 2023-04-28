package com.accountingsystem.configs.jwt;

import com.accountingsystem.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationMs}")
	private int jwtExpirationMs;

	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		SecretKey jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

		return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(jwtAccessSecret)
				.compact();
	}

	public boolean validateJwtToken(String jwt) throws InvalidKeyException {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
			return true;
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			throw new InvalidKeyException("Invalid jwt token");
		}
	}

	public String getUserNameFromJwtToken(String jwt) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
	}

}
