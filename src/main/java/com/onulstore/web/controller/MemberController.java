package com.onulstore.web.controller;

import com.onulstore.service.MemberService;
import com.onulstore.web.dto.MemberDto;
import com.onulstore.web.dto.PasswordDto;
import com.onulstore.web.dto.ProductDto.ProductResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Api(tags = {"Member-Controller"})
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @ApiOperation(value = "내 정보 / 인증 필요")
    public ResponseEntity<MemberDto.MemberResponse> getMyMemberInfo() {
        return ResponseEntity.ok(memberService.getMyInfo());
    }

    @PutMapping
    @ApiOperation(value = "프로필 수정 / 인증 필요")
    public ResponseEntity<String> updateProfile(
        @RequestBody @Valid MemberDto.UpdateRequest updateRequest) {
        memberService.updateProfile(updateRequest);
        return ResponseEntity.ok("프로필 수정이 완료되었습니다.");
    }

    @DeleteMapping
    @ApiOperation(value = "회원 탈퇴 / 인증 필요")
    public ResponseEntity<String> deleteProfile() {
        memberService.deleteProfile();
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    @PutMapping("/password")
    @ApiOperation(value = "회원 비밀번호 수정 / 인증 필요")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid PasswordDto passwordDto) {
        memberService.updatePassword(passwordDto);
        return ResponseEntity.ok("비밀번호 수정이 완료되었습니다.");
    }

    @GetMapping("/latest")
    @ApiOperation(value = "최근 본 상품 / 인증 필요")
    public ResponseEntity<ArrayList<ProductResponse>> latestProduct(HttpServletRequest request) {
        return ResponseEntity.ok(memberService.latestProduct(request));
    }

}
