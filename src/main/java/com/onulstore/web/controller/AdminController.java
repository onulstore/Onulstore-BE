package com.onulstore.web.controller;

import com.onulstore.service.AuthService;
import com.onulstore.web.dto.MemberDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Api(tags = {"Admin-Controller"})
public class AdminController {

    private final AuthService authService;

    @ApiOperation(value = "회원 가입")
    @PostMapping("/signup")
    public ResponseEntity<MemberDto.MemberResponse> admin(@RequestBody MemberDto.AdminRequest requestDto) {
        return ResponseEntity.ok(authService.admin(requestDto));
    }

    @ApiOperation(value = "전체 회원 조회")
    @GetMapping("/find")
    public ResponseEntity<HashMap<String, Object>> viewAllMember() {
        return ResponseEntity.ok().body(authService.viewAllMember());
    }

}
