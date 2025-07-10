package me.mocadev.chatserver.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mocadev.chatserver.chat.dto.ChatMessageDto;
import me.mocadev.chatserver.chat.service.ChatService;
import me.mocadev.chatserver.chat.service.RedisPubSubService;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-06-23
 **/
@Slf4j
@RequiredArgsConstructor
@Controller
public class StompController {

	private final SimpMessageSendingOperations messageTemplate;
	private final ChatService chatService;
	private final RedisPubSubService redisPubSubService;
	private final ObjectMapper objectMapper;

	// @MessageMapping("/{roomId}")
	// @SendTo("/topic/{roomId}")
	// public String sendMessage(@DestinationVariable Long roomId,
	// 						 String message){
	//     log.info("chat message >> {}", message);
	//     return  message;
	// }

	@MessageMapping("/{roomId}")
	public void sendMessage(@DestinationVariable Long roomId,
							ChatMessageDto chatMessageRequestDto) throws JsonProcessingException {
		log.info("chat message >> {}", chatMessageRequestDto.getMessage());
		chatService.saveMessage(roomId, chatMessageRequestDto);
		chatMessageRequestDto.setRoomId(roomId);
		// messageTemplate.convertAndSend("/topic/" + roomId, chatMessageRequestDto);
		redisPubSubService.publish("chat", objectMapper.writeValueAsString(chatMessageRequestDto));
	}
}
