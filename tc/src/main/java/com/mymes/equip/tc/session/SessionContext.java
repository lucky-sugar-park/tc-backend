package com.mymes.equip.tc.session;

import org.springframework.context.ApplicationContext;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SessionContext extends AbstractContext {
	
	private Session session;
	
	private Context context;

	private static SessionContext globalSessionContext;

	private ApplicationContext applicationContext;
	
	SessionContext() {
		super();
	}
	
	public static SessionContext getGlobalSessionContext() {
		if(globalSessionContext == null) {
			globalSessionContext = new SessionContext();
		}
		return globalSessionContext;
	}
	
	public Object getBean(String beanName) {
		try {
			return applicationContext.getBean(beanName);
		} catch (Throwable th) {
			return null;
		}
	}
	
	public Object getBean(Class<?> type) {
		return applicationContext.getBean(type);
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
