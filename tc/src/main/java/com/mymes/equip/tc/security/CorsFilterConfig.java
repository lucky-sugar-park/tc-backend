package com.mymes.equip.tc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CorsFilterConfig {
	
	@Bean
	public CorsFilter corsFilter() {
		log.debug("");
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    CorsConfiguration config = new CorsConfiguration();
//	    config.addAllowedOrigin("*");
	    config.addAllowedOriginPattern("http://localhost:3000");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("*");
	    config.setAllowCredentials(true);

	    source.registerCorsConfiguration("/**", config);
//	    source.registerCorsConfiguration("/auth/**", config);

	    return new CorsFilter(source);
	}
}
