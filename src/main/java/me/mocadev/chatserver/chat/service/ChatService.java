package me.mocadev.chatserver.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-06-23
 **/
@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {

	public boolean isRoomPaticipant(String email, Long roomId){
		return true;
	}
}
