package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.CustomException;
import com.onulstore.config.jwt.TokenProvider;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.CustomErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.web.dto.LoginDto;
import com.onulstore.web.dto.MemberDto;
import com.onulstore.web.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 회원가입
     *
     * @param signupRequest
     * @return 회원가입 정보
     */
    public MemberDto.MemberResponse signup(MemberDto.MemberRequest signupRequest) {
        if (memberRepository.existsByEmail(signupRequest.getEmail())) {
            throw new CustomException(CustomErrorResult.DUPLICATE_USER_ID);
        } else if (memberRepository.existsByPhoneNum(signupRequest.getPhoneNum())) {
            throw new CustomException(CustomErrorResult.DUPLICATE_PHONE_NUMBER);
        } else if (!signupRequest.getPassword().equals(signupRequest.getPasswordConfirm())) {
            throw new CustomException(CustomErrorResult.PASSWORD_MISMATCH);
        }

        Member member = signupRequest.toMember(passwordEncoder);
        return MemberDto.MemberResponse.of(memberRepository.save(member));
    }

    /**
     * 로그인
     *
     * @param loginDto
     * @return token 발급
     */
    public TokenDto login(LoginDto loginDto) {
        if (memberRepository.findByEmail(loginDto.getEmail()).orElse(null) == null) {
            throw new CustomException(CustomErrorResult.NOT_EXIST_USER);
        }

        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        String prevToken = redisTemplate.opsForValue().get("Access Token : " + authentication.getName());

        if (prevToken != null) {
            redisTemplate.delete("Access Token : " + authentication.getName()); // Access Token 삭제

            Long expire = tokenProvider.getExpire(prevToken);
            redisTemplate.opsForValue().set(prevToken, "anotherAccess", expire, TimeUnit.MILLISECONDS);
        }

        redisTemplate.opsForValue()
                .set("Access Token : " + authentication.getName(),
                        tokenDto.getAccessToken(),
                        tokenDto.getAccessTokenExpiresIn(),
                        TimeUnit.MILLISECONDS);

        redisTemplate.opsForValue()
                .set("Refresh Token : " + authentication.getName(),
                        tokenDto.getRefreshToken(),
                        tokenDto.getRefreshTokenExpiresIn(),
                        TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    /**
     * 입점사 회원가입
     *
     * @param sellerRequest
     * @return 회원가입 정보
     */
    public MemberDto.MemberResponse sellerRegistration(MemberDto.SellerRequest sellerRequest) {
        if (memberRepository.existsByEmail(sellerRequest.getEmail())) {
            throw new CustomException(CustomErrorResult.DUPLICATE_USER_ID);
        }

        Member member = sellerRequest.toMember(passwordEncoder);
        return MemberDto.MemberResponse.of(memberRepository.save(member));
    }

    /**
     * 전체 회원 조회(Admin)
     *
     * @return 전체 회원 정보
     */
    @Transactional(readOnly = true)
    public Map<String, List<Member>> viewAllMember() {
        Map<String, List<Member>> resultMap = new HashMap<>();

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));

        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
        }

        List<Member> allMemberList = memberRepository.findAll();
        resultMap.put("viewAllMember", allMemberList);

        return resultMap;
    }

    /**
     * Refresh Token 발급
     *
     * @param tokenRequest
     * @return Refresh Token 발급
     */
    public TokenDto getRefreshToken(TokenDto.TokenRequest tokenRequest) {
        if (!tokenProvider.validateToken(tokenRequest.getRefreshToken())) {
            throw new CustomException(CustomErrorResult.INVALID_REFRESH_TOKEN);
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequest.getAccessToken());

        String refreshToken = redisTemplate.opsForValue().get("Refresh Token : " + authentication.getName());

        if (ObjectUtils.isEmpty(refreshToken)) {
            throw new CustomException(CustomErrorResult.INVALID_REQUEST);
        } else if (!refreshToken.equals(tokenRequest.getRefreshToken())) {
            throw new CustomException(CustomErrorResult.INVALID_REFRESH_TOKEN);
        }

        TokenDto token = tokenProvider.generateToken(authentication);

        redisTemplate.opsForValue()
                .set("Refresh Token : " + authentication.getName(), token.getRefreshToken(),
                        token.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS);

        return token;
    }

    /**
     * 휴대폰 번호로 이메일 찾기
     *
     * @param findRequest
     * @return 회원 이메일 정보
     */
    @Transactional(readOnly = true)
    public MemberDto.FindResponse findEmail(MemberDto.FindRequest findRequest) {
        Member member = memberRepository.findByPhoneNum(findRequest.getPhoneNum())
                .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));

        return MemberDto.FindResponse.ofEmail(member);
    }

}
