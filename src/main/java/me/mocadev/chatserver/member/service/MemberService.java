package me.mocadev.chatserver.member.service;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import me.mocadev.chatserver.member.domain.Member;
import me.mocadev.chatserver.member.domain.Role;
import me.mocadev.chatserver.member.dto.MemberListResponseDto;
import me.mocadev.chatserver.member.dto.MemberLoginRequestDto;
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
	private final PasswordEncoder passwordEncoder;

	public Member create(MemberSaveRequestDto memberSaveRequestDto) {
		if(memberRepository.findByEmail(memberSaveRequestDto.getEmail()).isPresent()){
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}
		Member newMember = Member.builder()
			.name(memberSaveRequestDto.getName())
			.email(memberSaveRequestDto.getEmail())
			.password(passwordEncoder.encode(memberSaveRequestDto.getPassword()))
			.build();

		return memberRepository.save(newMember);
	}

	public Member login(final MemberLoginRequestDto memberLoginRequestDto) {
		Member member = memberRepository.findByEmail(memberLoginRequestDto.getEmail())
			.orElseThrow(()->new EntityNotFoundException("존재하지 않는 이메일입니다."));
		if(!passwordEncoder.matches(memberLoginRequestDto.getPassword(), member.getPassword())){
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		return member;
	}

	public List<MemberListResponseDto> findAll() {
		List<Member> members = memberRepository.findAll();
		List<MemberListResponseDto> memberListResDtos = new ArrayList<>();
		for (Member m : members){
			MemberListResponseDto memberListResDto = new MemberListResponseDto();
			memberListResDto.setId(m.getId());
			memberListResDto.setEmail(m.getEmail());
			memberListResDto.setName(m.getName());
			memberListResDtos.add(memberListResDto);
		}
		return memberListResDtos;
	}
}
