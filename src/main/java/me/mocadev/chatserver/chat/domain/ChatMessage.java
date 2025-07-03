package me.mocadev.chatserver.chat.domain;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.mocadev.chatserver.common.domain.BaseTimeEntity;
import me.mocadev.chatserver.member.domain.Member;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-07-03
 **/
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ChatMessage extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoom chatRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(nullable = false, length = 500)
	private String content;

	@OneToMany(mappedBy = "chatMessage", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<ReadStatus> readStatuses = new ArrayList<>();
}
