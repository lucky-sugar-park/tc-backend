package com.mymes.equip.tc.auth.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@RedisHash
@NoArgsConstructor
@AllArgsConstructor
public class JwRefreshTokenEntity {

	@Id
	private String refreshToken;

	private String userId;
	
	private String remoteAddress; 
	
	@TimeToLive(unit = TimeUnit.MILLISECONDS)
	private long timeToLive;	
}
