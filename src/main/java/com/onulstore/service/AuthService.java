package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.jwt.RefreshToken;
import com.onulstore.config.jwt.RefreshTokenRepository;
import com.onulstore.config.jwt.TokenProvider;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.UserErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.exception.UserException;
import com.onulstore.web.dto.LoginDto;
import com.onulstore.web.dto.MemberDto;
import com.onulstore.web.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public MemberDto.MemberResponse signup(MemberDto.MemberRequest signupRequest) {
        if (memberRepository.existsByEmail(signupRequest.getEmail())) {
            throw new UserException(UserErrorResult.DUPLICATE_USER_ID);
        }

        Member member = signupRequest.toMember(passwordEncoder);
        return MemberDto.MemberResponse.of(memberRepository.save(member));
    }

    @Transactional
    public TokenDto login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    // 관리자 회원가입
    @Transactional
    public MemberDto.MemberResponse admin(MemberDto.AdminRequest adminRequest) {
        if (memberRepository.existsByEmail(adminRequest.getEmail())) {
            throw new UserException(UserErrorResult.DUPLICATE_USER_ID);
        }

        Member member = adminRequest.toMember(passwordEncoder);
        return MemberDto.MemberResponse.of(memberRepository.save(member));
    }

    // 전체 회원 조회
    public HashMap<String, Object> viewAllMember() {
        HashMap<String, Object> resultMap = new HashMap<>();

        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));

        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
        }

        List<Member> allMemberList = memberRepository.findAll();
        resultMap.put("viewAllMember", allMemberList);

        return resultMap;
    }

    @Transactional
    public TokenDto getRefreshToken(TokenDto.TokenRequest tokenRequest) {
        if (!tokenProvider.validateToken(tokenRequest.getRefreshToken())) {
            throw new UserException(UserErrorResult.INVALID_REFRESH_TOKEN);
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequest.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new UserException(UserErrorResult.LOGOUT_USER));

        if (!refreshToken.getValue().equals(tokenRequest.getRefreshToken())) {
            throw new UserException(UserErrorResult.TOKEN_INFO_NOT_MATCH);
        }

        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

}
