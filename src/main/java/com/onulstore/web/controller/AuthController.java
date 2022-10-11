package com.onulstore.web.controller;

import com.onulstore.service.AuthService;
import com.onulstore.service.CouponService;
import com.onulstore.web.dto.LoginDto;
import com.onulstore.web.dto.MemberDto;
import com.onulstore.web.dto.MemberDto.MemberResponse;
import com.onulstore.web.dto.TokenDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = {"Auth-Controller"})
public class AuthController {

    private final AuthService authService;
    private final CouponService couponService;

    @ApiOperation(value = "회원 가입")
    @PostMapping("/signup")
    public ResponseEntity<MemberDto.MemberResponse> signup(
        @RequestBody MemberDto.MemberRequest requestDto) {
        MemberResponse memberResponse = authService.signup(requestDto);
        couponService.newUser(memberResponse.getEmail());
        return ResponseEntity.ok(memberResponse);
    }

    @ApiOperation(value = "로그인 / 인증 필요")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @ApiOperation(value = "Refresh Token 발급 / 인증 필요")
    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> getRefreshToken(
        @RequestBody TokenDto.TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.getRefreshToken(tokenRequest));
    }

    @ApiOperation(value = "휴대폰 번호로 이메일 찾기 / 인증 필요")
    @PostMapping("/find/email")
    public ResponseEntity<MemberDto.FindResponse> findEmail(
        @RequestBody MemberDto.FindRequest findRequest) {
        return ResponseEntity.ok(authService.findEmail(findRequest));
    }

}
