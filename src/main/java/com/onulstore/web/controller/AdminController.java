package com.onulstore.web.controller;

import com.onulstore.domain.member.Member;
import com.onulstore.service.AuthService;
import com.onulstore.web.dto.MemberDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Api(tags = {"Admin-Controller"})
public class AdminController {

    private final AuthService authService;

    @ApiOperation(value = "입점사 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<MemberDto.MemberResponse> sellerRegistration(@RequestBody MemberDto.SellerRequest request) {
        return ResponseEntity.ok(authService.sellerRegistration(request));
    }

    @ApiOperation(value = "전체 회원 조회")
    @GetMapping("/find")
    public ResponseEntity<Map<String, List<Member>>> viewAllMember() {
        return ResponseEntity.ok().body(authService.viewAllMember());
    }

}
