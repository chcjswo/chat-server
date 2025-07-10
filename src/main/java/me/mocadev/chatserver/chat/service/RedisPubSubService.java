package me.mocadev.chatserver.chat.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.mocadev.chatserver.chat.dto.ChatMessageDto;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-07-10
 **/
@RequiredArgsConstructor
@Service
public class RedisPubSubService implements MessageListener {

	private final StringRedisTemplate stringRedisTemplate;
	private final SimpMessageSendingOperations messageTemplate;
	private final ObjectMapper objectMapper;

	public void publish(String channel,
						String message) {
		stringRedisTemplate.convertAndSend(channel, message);
	}

	@Override
	public void onMessage(Message message,
						  byte[] pattern) {
		String payload = new String(message.getBody());
		try {
			ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);
			messageTemplate.convertAndSend("/topic/" + chatMessageDto.getRoomId(), chatMessageDto);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
