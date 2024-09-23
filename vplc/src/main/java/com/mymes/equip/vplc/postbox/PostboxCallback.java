package com.mymes.equip.vplc.postbox;

public interface PostboxCallback<T extends PostboxMessage> {

	public abstract void callback(Postbox<T> postbox, T message, Throwable cause);
}
