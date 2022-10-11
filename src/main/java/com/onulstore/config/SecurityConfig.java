package com.onulstore.config;

import com.onulstore.config.auth.PrincipalOauth2UserService;
import com.onulstore.config.jwt.JwtAccessDeniedHandler;
import com.onulstore.config.jwt.JwtAuthenticationEntryPoint;
import com.onulstore.config.jwt.JwtSecurityConfig;
import com.onulstore.config.jwt.TokenProvider;
import com.onulstore.config.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CorsFilter corsFilter;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()

            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            .addFilter(corsFilter)
            .addFilter(new AnonymousAuthenticationFilter("anonymous"))
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            .and()
            .authorizeRequests()
            .antMatchers("/", "/**", "/auth/**", "/products", "/v2/api-docs",
                "/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/index.html",
                "/webjars/**", "/swagger/**")
            .permitAll()
            .anyRequest().authenticated()

            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)

            .and()
            .apply(new JwtSecurityConfig(tokenProvider))

            .and()
            .formLogin().disable()
            .oauth2Login()
            .successHandler(oAuth2SuccessHandler)
            .userInfoEndpoint().userService(principalOauth2UserService);

        return http.build();
    }
}
