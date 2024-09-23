package com.mymes.equip.vplc.postbox;

public interface PostboxListener<T extends PostboxMessage> {

	public abstract boolean onMessage(T message);
}
