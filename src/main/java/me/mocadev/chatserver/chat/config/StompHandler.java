package me.mocadev.chatserver.chat.config;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mocadev.chatserver.chat.service.ChatService;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-06-23
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

	@Value("${jwt.secretKey}")
	private String secretKey;
	private final ChatService chatService;

	@Override
	public Message<?> preSend(Message<?> message,
							  MessageChannel channel) {
		final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		if (StompCommand.CONNECT == accessor.getCommand()) {
			log.info("connect요청시 토큰 유효성 검증");
			String bearerToken = accessor.getFirstNativeHeader("Authorization");
			String token = Objects.requireNonNull(bearerToken).substring(7);
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
			log.info("토큰 검증 완료");
		}

		if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
			log.info("subscribe 검증");
			String bearerToken = accessor.getFirstNativeHeader("Authorization");
			String token = bearerToken.substring(7);
			Claims claims = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
			String email = claims.getSubject();
			String roomId = accessor.getDestination().split("/")[2];
			if (!chatService.isRoomPaticipant(email, Long.parseLong(roomId))) {
				throw new AuthenticationServiceException("해당 room에 권한이 없습니다.");
			}
		}

		return message;
	}
}
