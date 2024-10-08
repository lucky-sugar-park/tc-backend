package com.mymes.equip.tc.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 유효한 자격 증명을 제공하지 않고 접근하려 할 떄 401 UNAUTHORIZED에러를 리턴하기 위함.
 */
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence (
    		HttpServletRequest request, 
    		HttpServletResponse response, 
    		AuthenticationException authException) throws IOException {
    	log.info("Forbidden: {}", authException);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}