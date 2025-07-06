package me.mocadev.chatserver.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-07-07
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomListResponseDto {

	private Long roomId;
	private String roomName;
}
