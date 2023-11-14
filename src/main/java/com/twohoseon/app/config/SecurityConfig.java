package com.twohoseon.app.config;

import com.twohoseon.app.repository.oauth2.CustomAuthorizationRequestRepository;
import com.twohoseon.app.security.JwtAuthenticationFilter;
import com.twohoseon.app.security.OAuth2AuthenticationSuccessHandler;
import com.twohoseon.app.service.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : SecurityConfig
 * @date : 2023/10/07 1:41 PM
 * @modifyed : $
 **/
@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.oauth2Login()
                .tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient)
                .and()
                .authorizationEndpoint().authorizationRequestRepository(customAuthorizationRequestRepository)
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler);

        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class);

        http.authorizeHttpRequests()
                .requestMatchers("/login/**", "/user", "/oauth2/**", "/auth/**", "/h2-console/**", "/swagger-ui", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/api/auth/**", "/api/profiles/isValidNickname", "/images/**", "/error").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }


}
