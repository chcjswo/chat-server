package me.mocadev.chatserver.chat.domain;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.mocadev.chatserver.common.domain.BaseTimeEntity;

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
public class ChatRoom extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Builder.Default
	private String isGroupChat = "N";

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
	private List<ChatParticipant> chatParticipants = new ArrayList<>();

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<ChatMessage> chatMessages = new ArrayList<>();

	public void setChatParticipants(List<ChatParticipant> chatParticipants) {
		this.chatParticipants = chatParticipants;
	}
}
