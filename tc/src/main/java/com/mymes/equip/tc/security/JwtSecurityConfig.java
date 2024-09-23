package com.mymes.equip.tc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * JwtTokenProvider과 JwtFilter를 SecurityConfig에 적용
 */
@Configuration
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	
	@Autowired
	private JwtFilter jwtFilter;

    @Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
	private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * JwtFilter를 Security로직에 적용하는 역할을 수행
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(exceptionConfigurer->{
//        	exceptionConfigurer.authenticationEntryPoint(((request, response, authException) -> {
//        		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//    		}));
//    		exceptionConfigurer.accessDeniedHandler(((request, response, accessDeniedException) -> {
//    			response.sendError(HttpServletResponse.SC_FORBIDDEN);
//    		}));
    		exceptionConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint);
    		exceptionConfigurer.accessDeniedHandler(jwtAccessDeniedHandler);
        });
        
    }
}