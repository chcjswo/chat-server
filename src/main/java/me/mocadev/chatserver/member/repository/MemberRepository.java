package me.mocadev.chatserver.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import me.mocadev.chatserver.member.domain.Member;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-06-10
 **/
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(String email);
}
