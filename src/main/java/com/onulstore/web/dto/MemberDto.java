package com.onulstore.web.dto;

import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberRequest {

        private String email;
        private String password;
        private String username;
        private String phoneNum;
        private String roadAddress;
        private String buildingName;
        private String detailAddress;

        public Member toMember(PasswordEncoder passwordEncoder) {
            return Member.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .username(username)
                    .phoneNum(phoneNum)
                    .roadAddress(roadAddress)
                    .buildingName(buildingName)
                    .detailAddress(detailAddress)
                    .authority(Authority.ROLE_USER)
                    .activated(true)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberResponse {

        private String email;
        private String username;
        private String phoneNum;
        private String roadAddress;
        private String buildingName;
        private String detailAddress;
        private Authority authority;

        public static MemberResponse of(Member member) {
            return MemberResponse.builder()
                    .email(member.getEmail())
                    .username(member.getUsername())
                    .phoneNum(member.getPhoneNum())
                    .roadAddress(member.getRoadAddress())
                    .buildingName(member.getBuildingName())
                    .detailAddress(member.getDetailAddress())
                    .authority(Authority.ROLE_USER)
                    .build();
        }

    }

}