package me.mocadev.chatserver.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import me.mocadev.chatserver.common.auth.JwtTokenProvider;
import me.mocadev.chatserver.member.domain.Member;
import me.mocadev.chatserver.member.dto.MemberListResponseDto;
import me.mocadev.chatserver.member.dto.MemberLoginRequestDto;
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
	private final JwtTokenProvider jwtTokenProvider;

	@PostMapping("/create")
	public ResponseEntity<?> memberCreate(@RequestBody MemberSaveRequestDto memberSaveRequestDto) {
		Member member = memberService.create(memberSaveRequestDto);
		return new ResponseEntity<>(member.getId(),HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<?> doLogin(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
		Member member = memberService.login(memberLoginRequestDto);
		String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
		Map<String, Object> loginInfo = new HashMap<>();
		loginInfo.put("id", member.getId());
		loginInfo.put("token", jwtToken);
		return new ResponseEntity<>(loginInfo, HttpStatus.OK);
	}

	@GetMapping("/list")
	public ResponseEntity<?> memberList(){
		List<MemberListResponseDto> dtos = memberService.findAll();
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}
}
