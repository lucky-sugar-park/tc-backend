//package com.mymes.equip.tc.auth;
//
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
////	@Bean
////	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
////		return authConfiguration.getAuthenticationManager();
////	}
//	
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
////		http
////			.formLogin()
////			.loginPage("")
////			.usernameParameter("userId")
////			.passwordParameter("password");
//////		http.loginProcessingUrl("/login");
////		http.cors().and().authorizeRequests()                 
////			.antMatchers(HttpMethod.POST, "/api/**").hasAnyRole("admin")
////			.antMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("admin")
////			.antMatchers("/api/**").authenticated()
////			.anyRequest().permitAll();
////		http.csrf().disable();
////		return http.build();
//		httpSecurity
////        	.csrf(AbstractHttpConfigurer::disable)
//			.csrf(csrfConfig->csrfConfig.disable())
//        	.formLogin(Customizer.withDefaults())
//        	.authorizeHttpRequests ( authorize -> {
//        		authorize
//        			.requestMatchers(
//        					AntPathRequestMatcher.antMatcher("/api/v1/swagger-ui.html")
//        			).permitAll()
////                	.requestMatchers(
////                			AntPathRequestMatcher.antMatcher("")
////                	).authenticated()
//        			.anyRequest().permitAll();
//        	})
//        	.headers (
//        			headersConfigurer -> {
//        				headersConfigurer.frameOptions (
//        						HeadersConfigurer.FrameOptionsConfig::sameOrigin
//        				);
//        			}
//        	)
//        	.exceptionHandling(exceptionConfigurer -> {
//        		exceptionConfigurer.authenticationEntryPoint(((request, response, authException) -> {
//            
//        		}));
//        		exceptionConfigurer.accessDeniedHandler(((request, response, accessDeniedException) -> {
//
//        		}));
//        	});
//		return httpSecurity.build();
//	}
//	
//	public WebSecurityCustomizer webSecurityCustomizer() {
//		return (web) -> {
//			web
//				.ignoring()
//				.requestMatchers(
//						PathRequest.toStaticResources().atCommonLocations()
//				);
//		};
//	}
//}
