package com.mymes.equip.tc.session;

import org.springframework.context.ApplicationContext;

public class Context extends AbstractContext {

	public static ThreadLocal<Context> currentContext = new ThreadLocal<Context>();
	
	private SessionContext sessionContext;
	
	private ApplicationContext applicationContext;
	
	private Context() {
		super();
		this.sessionContext = new SessionContext();
	}
	
	public Context(SessionContext sessionContext){
		super();
		this.sessionContext = sessionContext;
	}
	
	public static Context getCurrentContext() { 
		return currentContext.get(); 
	}
	
	public static void setCurrentContext(Context context){ 
		currentContext.set(context); 
	}
	
	public static void setCurrentContext() {
		currentContext.set(new Context()); 
	}
	
	public SessionContext getSessionContext() { 
		return sessionContext; 
	}
	
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
		this.sessionContext.setContext(this);
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
	
	public StackTraceElement getCurrentOperCallerElement() {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		return trace[CALLER_INDEX];
	}
	
	public String getCurrentOperation(){
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		return trace[CURRENT_INDEX].getMethodName();
	}

	public String getCurrentOperCallerClazz(){
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		return trace[CALLER_INDEX].getClassName();
	}

	public String getCurrentOperCallerOperation(){
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		return trace[CALLER_INDEX].getMethodName();
	}

	private final int CURRENT_INDEX = 1;
	private final int CALLER_INDEX  = 2;
}
