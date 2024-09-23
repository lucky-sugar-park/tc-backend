package com.mymes.equip.tc.msg;

public interface ReplyCallback<T extends Message> {

	public void callbackReply(T reply);
	
	public void callbackFailure(Exception e);
}
