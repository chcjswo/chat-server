package me.mocadev.chatserver.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-06-18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberListResponseDto {

	private Long id;
	private String name;
	private String email;
}
