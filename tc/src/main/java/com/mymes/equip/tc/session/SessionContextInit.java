package com.mymes.equip.tc.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component("sessionContextInit")
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionContextInit {
	
	private ApplicationContext applicationContext;
	
	@Autowired
	public SessionContextInit (ApplicationContext applicationContext) {
		this.applicationContext=applicationContext;
	}

	@PostConstruct
	public void init() {
		log.info("SessionContextInit.init()...");
		log.info("ApplicationContext would be set to used globally as a session context.");
		
		SessionContext.getGlobalSessionContext().setApplicationContext(applicationContext);
	}
}
