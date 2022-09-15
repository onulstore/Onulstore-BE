package com.onulstore.web.controller;

import com.onulstore.service.MemberService;
import com.onulstore.web.dto.MemberDto;
import com.onulstore.web.dto.PasswordDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Api(tags = {"Member-Controller"})
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @ApiOperation(value = "내 정보")
    public ResponseEntity<MemberDto.MemberResponse> getMyMemberInfo() {
        return ResponseEntity.ok(memberService.getMyInfo());
    }

    @PutMapping
    @ApiOperation(value = "프로필 수정")
    public ResponseEntity<String> updateProfile(@RequestBody @Valid MemberDto.updateRequest updateRequest) {
        memberService.updateProfile(updateRequest);
        return ResponseEntity.ok().body("프로필 수정이 완료되었습니다.");
    }

    @DeleteMapping
    @ApiOperation(value = "회원 탈퇴")
    public ResponseEntity<String> deleteProfile() {
        memberService.deleteProfile();
        return ResponseEntity.ok().body("회원 탈퇴가 완료되었습니다.");
    }

    @PutMapping("/password")
    @ApiOperation(value = "회원 비밀번호 수정")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid PasswordDto passwordDto) {
        memberService.updatePassword(passwordDto);
        return ResponseEntity.ok().body("비밀번호 수정이 완료되었습니다.");
    }

}
