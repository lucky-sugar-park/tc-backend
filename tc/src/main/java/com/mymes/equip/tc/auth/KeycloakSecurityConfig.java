package com.mymes.equip.tc.auth;
//package equip.tc.security;
//
//import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
//import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
//import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
//import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
//import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
//import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
//
//@KeycloakConfiguration
//public class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
//	
//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        http.cors().and().authorizeRequests()                 
//                 .antMatchers(HttpMethod.POST, "/api/**").hasAnyRole("admin")
//                 .antMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("admin")
//                 .antMatchers("/api/**").authenticated()
//                 .anyRequest().permitAll();
//        http.csrf().disable();
//    }
//
//	@Override
//	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//		return new NullAuthenticatedSessionStrategy();
//	}
//	
//	@Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
//        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
//        auth.authenticationProvider(keycloakAuthenticationProvider);
//    }
//	
//	public static class KeycloakConfig {
//        @Bean
//        public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
//             return new KeycloakSpringBootConfigResolver();
//        }
//    }
//}
