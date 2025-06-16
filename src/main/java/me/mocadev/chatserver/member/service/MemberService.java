package me.mocadev.chatserver.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import me.mocadev.chatserver.member.domain.Member;
import me.mocadev.chatserver.member.domain.Role;
import me.mocadev.chatserver.member.dto.MemberSaveRequestDto;
import me.mocadev.chatserver.member.repository.MemberRepository;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-06-10
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public Member create(MemberSaveRequestDto memberSaveRequestDto) {
		if(memberRepository.findByEmail(memberSaveRequestDto.getEmail()).isPresent()){
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}
		Member newMember = Member.builder()
			.name(memberSaveRequestDto.getName())
			.email(memberSaveRequestDto.getEmail())
			.password(memberSaveRequestDto.getPassword())
			.build();

		return memberRepository.save(newMember);
	}
}
