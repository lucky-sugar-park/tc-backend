//package com.mymes.equip.tc.auth;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import lombok.extern.slf4j.Slf4j;
//
//import com.mymes.equip.tc.user.UserService;
//
//@Component
//@Slf4j
//public class AuthInterceptor implements HandlerInterceptor {
//
//	private final UserService userService;
//	private final Auth auth;
//	
//	@Autowired
//	public AuthInterceptor(UserService userService, Auth auth) {
//		this.userService=userService;
//		this.auth=auth;
//	}
//
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		log.debug("");
//		return true;
//	}
//}
