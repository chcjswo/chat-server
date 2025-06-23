package me.mocadev.chatserver.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-06-23
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {

	private Long roomId;
	private String message;
	private String senderEmail;
}
