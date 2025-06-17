package me.mocadev.chatserver.common.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-06-17
 **/
@Component
public class JwtAuthFilter extends GenericFilter {

	@Value("${jwt.secretKey}")
	private String secretKey;

	@Override
	public void doFilter(final ServletRequest request,
						 final ServletResponse response,
						 final FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		String token = httpServletRequest.getHeader("Authorization");
		try {
			if (token != null) {
				if (!token.startsWith("Bearer ")) {
					throw new AuthenticationServiceException("Bearer 형식이 아닙니다.");
				}
				String jwtToken = token.substring(7);
				Claims claims = Jwts.parserBuilder()
					.setSigningKey(secretKey)
					.build()
					.parseClaimsJws(jwtToken)
					.getBody();
				List<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
				UserDetails userDetails = new User(claims.getSubject(), "", authorities);
				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
			httpServletResponse.setContentType("application/json");
			httpServletResponse.getWriter().write("invalid token");
		}
	}
}
