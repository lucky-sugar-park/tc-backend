package com.mymes.equip.tc.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mymes.equip.tc.auth.impl.JwTokenProvider;

@Configuration
public class JwtConfig {
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	@Value("${jwt.token-access-expiration-milli-seconds}")
	private long accessTokenAccessExpirationMilliSeconds;
	
	@Value("${jwt.token-refresh-expiration-milli-seconds}")
	private long refreshTokenAccessExpirationMilliSeconds;
	
	@Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public JwtFilter jwtFilter() {
		return new JwtFilter(jwTokenProvider());
	}
	
	@Bean
    public JwTokenProvider jwTokenProvider() {
    	return JwTokenProvider.builder()
        			.secretKey(secretKey)
        			.accessTokenAccessExpirationMilliSeconds(accessTokenAccessExpirationMilliSeconds)
        			.refreshTokenAccessExpirationMilliSeconds(refreshTokenAccessExpirationMilliSeconds)
        			.build();
    }

    @Bean
    public JwtAccessDeniedHandler jwtAccessDeniedHandler() {
    	return new JwtAccessDeniedHandler();
    }
    
    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
    	return new JwtAuthenticationEntryPoint();
    }
}