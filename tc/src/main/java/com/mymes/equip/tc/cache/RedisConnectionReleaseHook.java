package com.mymes.equip.tc.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisConnectionReleaseHook {
	
	@Autowired
	private RedisTemplate<?, ?> redisTemplate;

	@PreDestroy
	public void releaseRedis () {
		log.info("Redis connection resource is releasing...");
		List<RedisClientInfo> clients=redisTemplate.getClientList();
		clients.forEach(cInfo->{
			String addressPort=cInfo.getAddressPort();
			int index=addressPort.indexOf(':');
			redisTemplate.killClient(addressPort.substring(0, index), Integer.parseInt(addressPort.substring(index+1)));	
		});
		log.info("Completed redis connection release.");
	}
}
