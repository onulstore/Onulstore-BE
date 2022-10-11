package com.onulstore.config.auth;

import com.onulstore.config.oauth2.GoogleUserInfo;
import com.onulstore.config.oauth2.OAuth2UserInfo;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }

        Optional<Member> userOptional =
            memberRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(),
                oAuth2UserInfo.getProviderId());

        Member member;
        if (userOptional.isPresent()) {
            member = userOptional.get();
            member.setEmail(oAuth2UserInfo.getEmail());
            memberRepository.save(member);
        } else {
            member = Member.builder()
                .username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                .email(oAuth2UserInfo.getEmail())
                .authority("ROLE_USER")
                .provider(oAuth2UserInfo.getProvider())
                .providerId(oAuth2UserInfo.getProviderId())
                .firstKana(oAuth2UserInfo.getName())
                .lastKana(oAuth2UserInfo.getName())
                .firstName(oAuth2UserInfo.getName())
                .lastName(oAuth2UserInfo.getName())
                .build();
            memberRepository.save(member);
        }

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}
