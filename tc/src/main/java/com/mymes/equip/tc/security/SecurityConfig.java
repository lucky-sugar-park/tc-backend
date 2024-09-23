package com.mymes.equip.tc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize 애노테이션 활성화
public class SecurityConfig {
	
	@Autowired
	private CorsFilter corsFilter;
	
	@Autowired
	private JwtSecurityConfig jwtSecurityConfig;
	
	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Bean
    public AuthenticationManager authenticationManager ( AuthenticationConfiguration authenticationConfiguration ) throws Exception {
		ProviderManager providerManager = (ProviderManager) authenticationConfiguration.getAuthenticationManager();
	    providerManager.getProviders().add(this.customAuthenticationProvider);
	    return authenticationConfiguration.getAuthenticationManager();
    }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// 인증실패시 login form redirect 하지 않음 (REST API app이기 때문임, SPA인 경우 스스로 로그린 페이지로 라우팅 해야 함) 
			.httpBasic( configurer->configurer.disable() )
			.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
			// token 방식 적용 - csrf 미적용
			.csrf( csrfConfig -> csrfConfig.disable() )
			.cors( corsConfig -> corsConfig.disable() )
			.authorizeHttpRequests ( authorize -> {
        		authorize
        			.requestMatchers(
        					AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/swagger-ui.html"),
        					AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/auth/**")
        			).permitAll()
                	.requestMatchers(
                			AntPathRequestMatcher.antMatcher("/api/v1/**")
                	).hasAnyRole("ADMIN", "MANAGER", "USER")
//                	.requestMatchers(
//                			AntPathRequestMatcher.antMatcher("/api/v1/ifsvc/**")
//                	).hasAnyRole("CLIENT", "ADMIN")
//        			.anyRequest().hasRole("ADMIN");
//        			.anyRequest().authenticated();
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
		http.with(jwtSecurityConfig, Customizer.withDefaults());
		return http.build();
    }
	
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 리소스 spring security 대상에서 제외
        return (web) ->
                web
                    .ignoring()
                    .requestMatchers(
                            PathRequest.toStaticResources().atCommonLocations()
                    );
    }
}
