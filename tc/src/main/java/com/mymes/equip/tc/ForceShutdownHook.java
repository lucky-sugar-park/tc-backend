package com.mymes.equip.tc;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mymes.equip.tc.util.TimerUtil;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ForceShutdownHook {
	
	// 설정하지 않았다면 5초 적용
	@Value("${server.shutdown.grace-period:5000}")
	private long sleepTime;
	
	@PreDestroy()
	public void shutdownForce() {
		log.info("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFForceShutdownHook");
		Executors.newCachedThreadPool().execute(()->{
			TimerUtil.waitWithSleep(sleepTime);
			System.exit(0);
		});
	}
}
