package me.mocadev.chatserver.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-06-10
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveRequestDto {

	private String name;
	private String email;
	private String password;
}
