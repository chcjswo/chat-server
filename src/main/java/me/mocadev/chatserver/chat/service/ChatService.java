package me.mocadev.chatserver.chat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import me.mocadev.chatserver.chat.dto.ChatRoomListResponseDto;
import me.mocadev.chatserver.chat.dto.MyChatListResponseDto;
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
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new EntityNotFoundException("room cannot be found"));
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("member cannot be found"));

		List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
		for (ChatParticipant c : chatParticipants) {
			if (c.getMember().equals(member)) {
				return true;
			}
		}
		return false;
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

	public List<ChatRoomListResponseDto> getGroupChatRooms() {
		List<ChatRoom> chatRooms = chatRoomRepository.findByIsGroupChat("Y");
		List<ChatRoomListResponseDto> dtos = new ArrayList<>();
		for (ChatRoom c : chatRooms) {
			ChatRoomListResponseDto dto = ChatRoomListResponseDto
				.builder()
				.roomId(c.getId())
				.roomName(c.getName())
				.build();
			dtos.add(dto);
		}
		return dtos;
	}

	public void addParticipantToGroupChat(Long roomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot be found"));
		Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
			.orElseThrow(() -> new EntityNotFoundException("member cannot be found"));
		if (chatRoom.getIsGroupChat().equals("N")) {
			throw new IllegalArgumentException("그룹채팅이 아닙니다.");
		}
		Optional<ChatParticipant> participant = chatParticipantRepository.findByChatRoomAndMember(chatRoom, member);
		if (participant.isEmpty()) {
			addParticipantToRoom(chatRoom, member);
		}
	}

	private void addParticipantToRoom(ChatRoom chatRoom,
									  Member member) {
		ChatParticipant chatParticipant = ChatParticipant.builder()
			.chatRoom(chatRoom)
			.member(member)
			.build();
		chatParticipantRepository.save(chatParticipant);
	}

	public List<ChatMessageDto> getChatHistory(Long roomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new EntityNotFoundException("room cannot be found"));
		Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
			.orElseThrow(() -> new EntityNotFoundException("member cannot be found"));
		List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
		boolean check = false;
		for (ChatParticipant c : chatParticipants) {
			if (c.getMember().equals(member)) {
				check = true;
			}
		}
		if (!check) {
			throw new IllegalArgumentException("본인이 속하지 않은 채팅방입니다.");
		}

		List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByCreatedTimeAsc(chatRoom);
		List<ChatMessageDto> chatMessageDtos = new ArrayList<>();
		for (ChatMessage c : chatMessages) {
			ChatMessageDto chatMessageDto = ChatMessageDto.builder()
				.message(c.getContent())
				.senderEmail(c.getMember().getEmail())
				.build();
			chatMessageDtos.add(chatMessageDto);
		}
		return chatMessageDtos;
	}

	public void messageRead(Long roomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new EntityNotFoundException("room cannot be found"));
		Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
			.orElseThrow(() -> new EntityNotFoundException("member cannot be found"));
		List<ReadStatus> readStatuses = readStatusRepository.findByChatRoomAndMember(chatRoom, member);
		for (ReadStatus r : readStatuses) {
			r.updateIsRead(true);
		}
	}

	public List<MyChatListResponseDto> getMyChatRooms() {
		Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
			.orElseThrow(() -> new EntityNotFoundException("member cannot be found"));
		List<ChatParticipant> chatParticipants = chatParticipantRepository.findAllByMember(member);
		List<MyChatListResponseDto> chatListResDtos = new ArrayList<>();
		for (ChatParticipant c : chatParticipants) {
			Long count = readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(c.getChatRoom(), member);
			MyChatListResponseDto dto = MyChatListResponseDto.builder()
				.roomId(c.getChatRoom().getId())
				.roomName(c.getChatRoom().getName())
				.isGroupChat(c.getChatRoom().getIsGroupChat())
				.unReadCount(count)
				.build();
			chatListResDtos.add(dto);
		}
		return chatListResDtos;
	}
}
