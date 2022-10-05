package com.onulstore.web.dto;

import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.Provider;
import com.onulstore.domain.member.Member;
import java.util.ArrayList;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberRequest {

        private String email;
        private String password;
        private String passwordConfirm;
        private String firstName;
        private String lastName;
        private String firstKana;
        private String lastKana;
        private String phoneNum;

        public Member toMember(PasswordEncoder passwordEncoder) {
            return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .firstKana(firstKana)
                .lastKana(lastKana)
                .username("CUSTOMER")
                .phoneNum(phoneNum)
                .authority(Authority.ROLE_USER.getKey())
                .provider(Provider.local.getKey())
                .providerId(Provider.local.getTitle())
                .coupons(new ArrayList<>())
                .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberResponse {

        private String email;
        private String firstName;
        private String lastName;
        private String firstKana;
        private String lastKana;
        private String username;
        private String phoneNum;
        private String postalCode;
        private String roadAddress;
        private String buildingName;
        private String detailAddress;

        public static MemberResponse of(Member member) {
            return MemberResponse.builder()
                .email(member.getEmail())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .firstKana(member.getFirstKana())
                .lastKana(member.getLastKana())
                .username(member.getUsername())
                .phoneNum(member.getPhoneNum())
                .postalCode(member.getPostalCode())
                .roadAddress(member.getRoadAddress())
                .buildingName(member.getBuildingName())
                .detailAddress(member.getDetailAddress())
                .build();
        }

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SellerRequest {

        private String email;
        private String password;
        private String username;
        private String phoneNum;
        private String postalCode;
        private String roadAddress;
        private String buildingName;
        private String detailAddress;

        public Member toMember(PasswordEncoder passwordEncoder) {
            return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .firstName("ENTRY_COMPANY")
                .lastName("ENTRY_COMPANY")
                .firstKana("ENTRY_COMPANY")
                .lastKana("ENTRY_COMPANY")
                .username(username)
                .phoneNum(phoneNum)
                .postalCode(postalCode)
                .roadAddress(roadAddress)
                .buildingName(buildingName)
                .detailAddress(detailAddress)
                .authority(Authority.ROLE_SELLER.getKey())
                .provider(Provider.local_admin.getKey())
                .providerId(Provider.local_admin.getTitle())
                .build();
        }
    }

    @Getter
    @Setter
    @ToString
    public static class UpdateRequest {

        private String phoneNum;
        private String postalCode;
        private String roadAddress;
        private String buildingName;
        private String detailAddress;
    }

}
