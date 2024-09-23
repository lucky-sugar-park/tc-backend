package com.mymes.equip.tc.auth.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.mymes.equip.tc.auth.AuthService;
import com.mymes.equip.tc.auth.LoginRequest;
import com.mymes.equip.tc.auth.SecurityUser;
import com.mymes.equip.tc.user.UserInfo;
import com.mymes.equip.tc.user.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwTokenProvider jwTokenProvider;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwRefreshTokenRepository jwRefreshTokenRepository;

	@Override
	@Transactional
	public SecurityUser login(LoginRequest loginRequest) {
		log.info("");

		Authentication authentication = this.authenticate(loginRequest.getId(), loginRequest.getPassword());
		JwTokenInfo tokenInfo=this.jwTokenProvider.createTokens(authentication);
		JwRefreshTokenEntity tokenEntity = new JwRefreshTokenEntity();
		tokenEntity.setUserId(loginRequest.getId());
		tokenEntity.setRefreshToken(tokenInfo.getRefreshToken());
		tokenEntity.setRemoteAddress(loginRequest.getRemoteAddress());
		tokenEntity.setTimeToLive(tokenInfo.getRefreshTokenExpiredTime().getTime());

		jwRefreshTokenRepository.save(tokenEntity);
		
		UserInfo uInfo = (UserInfo) authentication.getDetails();

		SecurityUser suser = new SecurityUser(uInfo);
		suser.setJwTokenInfo(tokenInfo);

		uInfo.setLogined(true);
		uInfo.setLoginedIp(loginRequest.getRemoteAddress());
		userService.updateUser(uInfo);

		return suser;
	}

	@Override
	@Transactional
	public SecurityUser reissue(String remoteAddress, String refreshToken) {
		log.info("");
		try {
			boolean valid=jwTokenProvider.validateToken(refreshToken, remoteAddress, true);
			if(!valid) {
				throw new InvalidTokenException("Invalid refresh token.");
			}
		} catch (ExpiredJwtException e) {
			log.info("", e);
			throw e;
		} catch (NoMatchedRemoteAddressException e) {
			log.info("");
			throw e;
		}

		// refresh token rotation 기법적용
		Optional<JwRefreshTokenEntity> optional = jwRefreshTokenRepository.findById(refreshToken);
		if (optional.isEmpty() || !refreshToken.equals(optional.get().getRefreshToken())) {
			throw new InvalidTokenException("Different refresh token.");
	    }

		JwRefreshTokenEntity refreshTokenEntity = optional.get();

		if(new Date().getTime()>refreshTokenEntity.getTimeToLive()) {
			throw new ExpiredRefreshTokenException("Expired refresh token. New user login is required.");
		}

		// create new token (access / refresh)
		UserInfo uInfo = userService.findUserById(refreshTokenEntity.getUserId());
		UsernamePasswordAuthenticationToken authenticationToken = 
        		new UsernamePasswordAuthenticationToken(uInfo.getId(), uInfo.getPassword(), uInfo.getAuthorities());
		JwTokenInfo tokenInfo=this.jwTokenProvider.createTokens(authenticationToken);
		
		// delete existing legacy refresh token
		jwRefreshTokenRepository.delete(refreshTokenEntity);
		
		JwRefreshTokenEntity newTokenEntity = new JwRefreshTokenEntity();
		newTokenEntity.setRefreshToken(tokenInfo.getRefreshToken());
		newTokenEntity.setUserId(uInfo.getId());
		newTokenEntity.setRemoteAddress(remoteAddress);
		newTokenEntity.setTimeToLive(tokenInfo.getRefreshTokenExpiredTime().getTime());

		// save new refresh token
		jwRefreshTokenRepository.save(newTokenEntity);

		SecurityUser suser = new SecurityUser(uInfo);
		suser.setJwTokenInfo(tokenInfo);

		return suser;
	}
	
	private Authentication authenticate(String userId, String password) {
		UsernamePasswordAuthenticationToken authenticationToken = 
        		new UsernamePasswordAuthenticationToken(userId, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        log.info("authentication: {}", authentication);
        return authentication;
	}

	@Override
	public void logout(String userId, String remoteAddress, String refreshToken) {
		log.info("");
		// check validation
		try {
			boolean valid=jwTokenProvider.validateToken(refreshToken, remoteAddress, true);
			if(!valid) {
				throw new InvalidTokenException("Invalid refresh token.");
			}
		} catch (ExpiredRefreshTokenException t) {
			throw new InvalidTokenException("");
		}

		Optional<JwRefreshTokenEntity> optional = jwRefreshTokenRepository.findById(refreshToken);
		jwRefreshTokenRepository.deleteById(refreshToken);
		if (optional.isEmpty() || !refreshToken.equals(optional.get().getRefreshToken())) {
			throw new InvalidTokenException("Different refresh token.");
		}
		// update user information
		// delete refresh token information
		
		userService.updateUser(null);
		
	}
}
