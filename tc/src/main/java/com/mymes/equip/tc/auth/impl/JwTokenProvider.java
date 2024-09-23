package com.mymes.equip.tc.auth.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class JwTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";

	private final String ISSUER = "TC_APP";
	
	private final String secretKey;
	
	private final long accessTokenAccessExpirationMilliSeconds;
	
	private final long refreshTokenAccessExpirationMilliSeconds;

    private SecretKey key;

    @Builder
    public JwTokenProvider (
    		String secretKey, 
    		long accessTokenAccessExpirationMilliSeconds, 
    		long refreshTokenAccessExpirationMilliSeconds ) {
    	log.debug("");

    	this.secretKey=secretKey;
    	this.accessTokenAccessExpirationMilliSeconds=accessTokenAccessExpirationMilliSeconds;
    	this.refreshTokenAccessExpirationMilliSeconds=refreshTokenAccessExpirationMilliSeconds;
    	
    	this.key=this.createSigningKey (this.secretKey);
    }

    private SecretKey createSigningKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public JwTokenInfo createTokens(Authentication authentication) {
    	return JwTokenInfo.builder()
    			.accessToken(createAccessToken(authentication))
    			.refreshToken(createRefreshToken(authentication))
    			.refreshTokenExpiredTime(new Date(new Date().getTime() + this.accessTokenAccessExpirationMilliSeconds))
    			.build();
    }
    
    /**
     * Authentication에 있는 권한 정보를 이용해서 토큰을 생성한다.
     * 정보를 바탕으로 권한을 가져오고, 유효시간과 암호화를 통해 토큰을 생성한다.
     */
    public String createAccessToken(Authentication authentication) {
        String authorities = authentication
        		.getAuthorities()
        		.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + this.accessTokenAccessExpirationMilliSeconds);

        String newToken = Jwts.builder()
        		.issuer(ISSUER)
        		.issuedAt(now)
        		// User ID
        		.subject(authentication.getName())
        		// No audience
        		.audience()
        		.and()
                .issuedAt(new Date())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key)
                .expiration(expirationTime)
                .id(UUID.randomUUID().toString())
                .compact();
        return newToken;
    }
    
    public String createRefreshToken(Authentication authentication) {
    	Date now = new Date();
    	Date expirationTime = new Date(now.getTime() + this.accessTokenAccessExpirationMilliSeconds);
    	
    	String refreshToken = Jwts.builder()
    			.issuer(ISSUER)
    			.issuedAt(now)
    			.expiration(expirationTime)
    			.signWith(key)
    			.compact();
    	
    	return refreshToken;
    }

    /**
     * 토큰을 이용해서 권한 정보를 리턴하는 메서드
     * 토큰을 이용해서 클레임을 만들고, 클레임에서 권한 정보를 가져와서 유저 객체를 만든다.
     *
     * claim : JWT의 속성 정보
     */
    public Authentication extractAuthentication(String token){
    	log.debug("");
        Claims claims = parseClaims(token);
        Collection<? extends GrantedAuthority> authorities  = 
        	Arrays.stream (claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User user = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities);
    }
    
    private Claims parseClaims(String accessToken) {
    	log.debug("");
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
    
    /**
     * 토큰의 유효성을 검증하기 위함 -> 파싱 후 검증하고 리턴한다.
     */
    public boolean validateToken(String token, String remoteAddress, boolean checkAddress) {
    	log.debug("");
        try{
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
         // TO-DO: 이 부분에서 client의 remote address까지의 일치 여부를 체크하면 더 안전함
//            return !claims.getExpiration().before(new Date());
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("Wrong JWT Signature.", e);
            return false;
        } catch (ExpiredJwtException e){
        	log.info("Expired token.");
        	throw e;
        } catch (UnsupportedJwtException e){
        	log.info("No supported JWT Token.");
        	return false;
        } catch (MissingClaimException e){
        	log.info("Unknown claim error.");
        	return false;
        }
    }
}
