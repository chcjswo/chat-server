package me.mocadev.chatserver.chat.service;

import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import me.mocadev.chatserver.chat.domain.ChatMessage;
import me.mocadev.chatserver.chat.domain.ChatParticipant;
import me.mocadev.chatserver.chat.domain.ChatRoom;
import me.mocadev.chatserver.chat.domain.ReadStatus;
import me.mocadev.chatserver.chat.dto.ChatMessageDto;
import me.mocadev.chatserver.chat.repository.ChatMessageRepository;
import me.mocadev.chatserver.chat.repository.ChatParticipantRepository;
import me.mocadev.chatserver.chat.repository.ChatRoomRepository;
import me.mocadev.chatserver.chat.repository.ReadStatusRepository;
import me.mocadev.chatserver.member.domain.Member;
import me.mocadev.chatserver.member.repository.MemberRepository;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-06-23
 **/
@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatParticipantRepository chatParticipantRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final ReadStatusRepository readStatusRepository;
	private final MemberRepository memberRepository;

	public boolean isRoomPaticipant(String email,
									Long roomId) {
		return true;
	}

	public void saveMessage(Long roomId,
							ChatMessageDto chatMessageReqDto) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new EntityNotFoundException("room cannot be found"));

		Member sender = memberRepository.findByEmail(chatMessageReqDto.getSenderEmail())
			.orElseThrow(() -> new EntityNotFoundException("member cannot be found"));

		ChatMessage chatMessage = ChatMessage.builder()
			.chatRoom(chatRoom)
			.member(sender)
			.content(chatMessageReqDto.getMessage())
			.build();
		chatMessageRepository.save(chatMessage);

		List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
		for (ChatParticipant c : chatParticipants) {
			ReadStatus readStatus = ReadStatus.builder()
				.chatRoom(chatRoom)
				.member(c.getMember())
				.chatMessage(chatMessage)
				.isRead(c.getMember().equals(sender))
				.build();
			readStatusRepository.save(readStatus);
		}
	}

	public void createGroupRoom(String chatRoomName) {
		Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
			.orElseThrow(() -> new EntityNotFoundException("member cannot be found"));

		ChatRoom chatRoom = ChatRoom.builder()
			.name(chatRoomName)
			.isGroupChat("Y")
			.build();
		chatRoomRepository.save(chatRoom);

		ChatParticipant chatParticipant = ChatParticipant.builder()
			.chatRoom(chatRoom)
			.member(member)
			.build();
		chatParticipantRepository.save(chatParticipant);
	}
}
