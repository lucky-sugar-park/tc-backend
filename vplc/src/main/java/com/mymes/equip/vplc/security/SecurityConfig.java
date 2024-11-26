package com.mymes.equip.vplc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private CorsFilter corsFilter;

	// 일단 모든 request를 통과시키도록 설정함 ( 나중에 인증/권한 체크 해야 함 )
 	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic( configurer->configurer.disable() )
			.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
			.csrf( csrfConfig -> csrfConfig.disable() )
			.cors( corsConfig -> corsConfig.disable() )
			.authorizeHttpRequests ( authorize -> {
        		authorize
        			.anyRequest().permitAll();
        	})
			.headers (
        			headersConfigurer -> {
        				headersConfigurer.frameOptions (
        						HeadersConfigurer.FrameOptionsConfig::sameOrigin
        				);
        			}
        	)
        	.sessionManagement( sessionManagementConfigurer -> {
        		sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        	});
		return http.build();
    }

}
