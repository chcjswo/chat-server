package me.mocadev.chatserver.chat.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import me.mocadev.chatserver.chat.domain.ChatRoom;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-07-04
 **/
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	List<ChatRoom> findByIsGroupChat(String isGroupChat);
}
