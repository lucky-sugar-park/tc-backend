package com.mymes.equip.tc.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class RedisTestService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private RedisTestRepository redisTestRepository;
	
	@PostConstruct
	public void initialize() {
		EmailToken emailToken=new EmailToken();
		emailToken.setId("ispark");
		emailToken.setRandom("Eamil for ispark");
		emailToken.setTimeToLive(1000L);
		redisTestRepository.save(emailToken);
		EmailToken result=redisTestRepository.findById("ispark").get();
		System.out.println("################====>" + result.getRandom());
	
		// redisTemplate에 정의된 serializer, deserializer에 맞게 해야 함 (그렇지 않으면 에러 발생)
//		redisTemplate.opsForValue().set("kkk", "hahahaha", 10000L, TimeUnit.MILLISECONDS);
//		System.out.println(redisTemplate.opsForValue().get("kkk"));
		redisTemplate.opsForValue().set("kkk", emailToken, 10000L, TimeUnit.MILLISECONDS);
		System.out.println("################====>" + redisTemplate.opsForValue().get("kkk"));
	}
}
