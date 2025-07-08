package me.mocadev.chatserver.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-07-09
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyChatListResponseDto {

	private Long roomId;
	private String roomName;
	private String isGroupChat;
	private Long unReadCount;
}
