package com.mymes.equip.tc.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 필요한 권한이 존재하지 않는 경우 403 FORBIDDEN ERROR을 리턴하기 위함.
 */
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
    public void handle (
    		HttpServletRequest request, 
    		HttpServletResponse response, 
    		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.info("Access denied: {}", request.getRemoteAddr(), accessDeniedException);
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
