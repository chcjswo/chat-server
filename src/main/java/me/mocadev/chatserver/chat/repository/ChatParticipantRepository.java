package me.mocadev.chatserver.chat.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import io.lettuce.core.dynamic.annotation.Param;
import me.mocadev.chatserver.chat.domain.ChatParticipant;
import me.mocadev.chatserver.chat.domain.ChatRoom;
import me.mocadev.chatserver.member.domain.Member;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-07-04
 **/
@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

	List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);

	Optional<ChatParticipant> findByChatRoomAndMember(ChatRoom chatRoom,
													  Member member);

	List<ChatParticipant> findAllByMember(Member member);

	@Query("SELECT cp1.chatRoom FROM ChatParticipant cp1 "
		+ "JOIN ChatParticipant cp2 "
		+ "ON cp1.chatRoom.id = cp2.chatRoom.id "
		+ "WHERE cp1.member.id = :myId "
		+ "AND cp2.member.id = :otherMemberId "
		+ "AND cp1.chatRoom.isGroupChat = 'N'")
	Optional<ChatRoom> findExistingPrivateRoom(@Param("myId") Long myId,
											   @Param("otherMemberId") Long otherMemberId);
}
