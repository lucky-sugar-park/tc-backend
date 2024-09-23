package com.mymes.equip.tc.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mymes.equip.tc.auth.impl.JwTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 헤더(Authorization)에 있는 토큰을 꺼내 이상이 없는 경우 SecurityContext에 저장
 * Request 이전에 작동
 */
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
	
	public static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER = "Bearer ";
	
	private final JwTokenProvider jwtTokenProvider;
	
	private ObjectMapper objectMapper;
	
	public JwtFilter(JwTokenProvider jwtTokenProvider){
		super();
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = new ObjectMapper();
    }
	
	private String extractRemoteAddress(HttpServletRequest request) {
		String remoteAddress = request.getHeader("X-FORWARDED-FOR");
		if(remoteAddress == null || "".equals(remoteAddress.trim())) {
			remoteAddress = request.getRemoteAddr();
		}
		return remoteAddress;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		log.debug("");

        String jwt = resolveToken(request);
        String requestURI = request.getRequestURI();
        
        boolean reissueRequired = false;
        
        if(jwt!=null) {
        	log.debug("Token: {}", jwt);
        	try {
        		
        		if(jwtTokenProvider.validateToken(jwt, extractRemoteAddress(request), true)) {
        			Authentication authentication=jwtTokenProvider.extractAuthentication(jwt);
        			SecurityContextHolder.getContext().setAuthentication(authentication);
        			log.debug("Saved authentication information into Security Context : {}, URL : {}", authentication, requestURI);
        		}
        	} catch (ExpiredJwtException e) {
        		log.info("Access Token is expired. Reissue is required for acquiring tokens. URL: {}", requestURI);
        		reissueRequired = true;
        	}
        } else {
            log.debug("No JWT exists. Reissue is required for acquiring tokens. URL : {}", requestURI);
            if(!"/auth/login".equals(requestURI) && !"/auth/reissue".equals(requestURI)) {
            	reissueRequired = true;
            }
        }
        
        if(reissueRequired) {
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    		Map<String, String> error = new HashMap<>();
    		error.put("message", "Access token expired");
    		error.put("code", "EXPIRED_TOKEN");
    		error.put("status", "UNAUTHORIZED");

    		response.setContentType("application/json;charset=UTF-8");
    		response.getWriter().write(objectMapper.writeValueAsString(error));

    		return;
        }
        chain.doFilter(request, response);
	}
	
	/**
     * HttpServletRequest 객체의 Header에서 token을 꺼내는 역할을 수행
     * @param request
     * @return
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
        	return bearerToken.substring(7);
        }

        return null;
    }
}
