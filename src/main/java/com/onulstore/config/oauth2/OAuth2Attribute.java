package com.onulstore.config.oauth2;

import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.member.Member;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {

    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String name;
    private String provider;

    public static OAuth2Attribute of(String provider, String attributeKey,
        Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(attributeKey, attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attribute ofGoogle(String attributeKey,
        Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
            .name((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .provider("google")
            .attributes(attributes)
            .attributeKey(attributeKey)
            .build();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("name", name);
        map.put("email", email);
        return map;
    }

    public Member toEntity() {
        return Member.builder()
            .username(name)
            .email(email)
            .authority(Authority.ROLE_USER.getKey())
            .provider(provider)
            .providerId((String) attributes.get("sub"))
            .build();
    }
}