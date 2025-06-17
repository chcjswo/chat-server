package me.mocadev.chatserver.common.auth;

import static io.jsonwebtoken.SignatureAlgorithm.*;
import static java.util.Base64.*;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-06-18
 **/
@Component
public class JwtTokenProvider {

	private final String secretKey;
	private final int expiration;
	private Key SECRET_KEY;

	public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey,
							@Value("${jwt.expiration}") int expiration) {
		this.secretKey = secretKey;
		this.expiration = expiration;
		this.SECRET_KEY = new SecretKeySpec(getDecoder().decode(secretKey), HS512.getJcaName());
	}

	public String createToken(String email,
							  String role) {
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("role", role);
		Date now = new Date();
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + expiration * 60 * 1000L))
			.signWith(SECRET_KEY)
			.compact();
	}
}
