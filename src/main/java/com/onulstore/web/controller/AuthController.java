package com.onulstore.web.controller;

import com.onulstore.service.AuthService;
import com.onulstore.web.dto.LoginDto;
import com.onulstore.web.dto.MemberDto;
import com.onulstore.web.dto.TokenDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @ApiOperation(value = "회원 가입")
    @PostMapping("/signup")
    public ResponseEntity<MemberDto.MemberResponse> signup(@RequestBody MemberDto.MemberRequest requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @ApiOperation(value = "Refresh Token 발급")
    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> getRefreshToken(@RequestBody TokenDto.TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.getRefreshToken(tokenRequest));
    }

}
