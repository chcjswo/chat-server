package me.mocadev.chatserver.chat.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import me.mocadev.chatserver.chat.domain.ChatRoom;
import me.mocadev.chatserver.chat.domain.ReadStatus;
import me.mocadev.chatserver.member.domain.Member;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-07-04
 **/
@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {

	List<ReadStatus> findByChatRoomAndMember(ChatRoom chatRoom,
											 Member member);

	Long countByChatRoomAndMemberAndIsReadFalse(ChatRoom chatRoom,
												Member member);
}
