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

	// @MessageMapping("/{roomId}")
	// @SendTo("/topic/{roomId}")
	// public String sendMessage(@DestinationVariable Long roomId,
	// 						 String message){
	//     log.info("chat message >> {}", message);
	//     return  message;
	// }

	@MessageMapping("/{roomId}")
	public void sendMessage(@DestinationVariable Long roomId,
							  ChatMessageDto chatMessageReqDto) throws JsonProcessingException {
		log.info("chat message >> {}", chatMessageReqDto.getMessage());
		chatService.saveMessage(roomId, chatMessageReqDto);
		messageTemplate.convertAndSend("/topic/" + roomId, chatMessageReqDto);
	}
}
