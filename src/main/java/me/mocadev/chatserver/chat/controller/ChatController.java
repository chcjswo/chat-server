package me.mocadev.chatserver.chat.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import me.mocadev.chatserver.chat.dto.ChatMessageDto;
import me.mocadev.chatserver.chat.dto.ChatRoomListResponseDto;
import me.mocadev.chatserver.chat.service.ChatService;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-07-04
 **/
@RequiredArgsConstructor
@RequestMapping("/chat")
@RestController
public class ChatController {

	private final ChatService chatService;

	@PostMapping("/room/group/create")
	public ResponseEntity<?> createGroupRoom(@RequestParam String roomName){
		chatService.createGroupRoom(roomName);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/room/group/list")
	public ResponseEntity<?> getGroupChatRooms(){
		List<ChatRoomListResponseDto> chatRooms = chatService.getGroupChatRooms();
		return new ResponseEntity<>(chatRooms, HttpStatus.OK);
	}

	@PostMapping("/room/group/{roomId}/join")
	public ResponseEntity<?> joinGroupChatRoom(@PathVariable Long roomId){
		chatService.addParticipantToGroupChat(roomId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/history/{roomId}")
	public ResponseEntity<?> getChatHistory(@PathVariable Long roomId){
		List<ChatMessageDto> chatMessageDtos = chatService.getChatHistory(roomId);
		return new ResponseEntity<>(chatMessageDtos, HttpStatus.OK);
	}
}
