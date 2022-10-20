package com.onulstore.config.oauth2;

import com.onulstore.config.jwt.TokenProvider;
import com.onulstore.web.dto.TokenDto;

import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        String uri;
        uri = UriComponentsBuilder.fromUriString("https://onulstore.netlify.app/")
                .queryParam("Access Token", tokenDto.getAccessToken())
                .queryParam("Refresh Token", tokenDto.getRefreshToken())
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }
}
