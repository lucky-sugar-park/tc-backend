package com.mymes.equip.tc.cache;

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
public class EmailToken {

	@Id
	private String id;
	
	private String random;
	
	@TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long timeToLive;

}
