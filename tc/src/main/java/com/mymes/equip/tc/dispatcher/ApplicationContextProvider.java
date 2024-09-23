package com.mymes.equip.tc.dispatcher;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("applicationContextProvider")
public class ApplicationContextProvider implements ApplicationContextAware {
	
	private static class AplicationContextHolder {

		private static final ContextResource CONTEXT_PROV = new ContextResource();

		private AplicationContextHolder() {
			super();
		}
    }

	private static final class ContextResource {

		private ApplicationContext context;

		private ContextResource(){
			super();
		}

		private void setContext(ApplicationContext context) {
			this.context = context;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		AplicationContextHolder.CONTEXT_PROV.setContext(applicationContext);
	}
	
	public static ApplicationContext getApplicationContext() {
		return AplicationContextHolder.CONTEXT_PROV.context;
	}
}
