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

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        Cookie accessToken = new Cookie("accessToken", tokenDto.getAccessToken());
        Cookie refreshToken = new Cookie("refreshToken", tokenDto.getRefreshToken());

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
        log.info(accessToken.getName() + " : " + accessToken.getValue());

        response.sendRedirect("http://onulstore.dlcpop.com/");
    }
}
