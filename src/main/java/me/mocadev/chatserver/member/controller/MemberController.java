package me.mocadev.chatserver.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import me.mocadev.chatserver.member.domain.Member;
import me.mocadev.chatserver.member.dto.MemberSaveRequestDto;
import me.mocadev.chatserver.member.service.MemberService;

/**
 * @author mc.jeon
 * @version 1.0.0
 * @since 2025-06-10
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/create")
	public ResponseEntity<?> memberCreate(@RequestBody MemberSaveRequestDto memberSaveRequestDto) {
		Member member = memberService.create(memberSaveRequestDto);
		return new ResponseEntity<>(member.getId(),HttpStatus.CREATED);
	}
}
