package com.mymes.equip.vplc.postbox;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostboxManager {

	private final Map<String, Postbox<PostboxMessage>> pboxMap;

	private static class PostBoxManagerHolder {
		private static final PostboxManager INSTANCE = new PostboxManager();
	}
	
	private PostboxManager() {
		pboxMap = new HashMap<>();
	}
	
	public static PostboxManager getInstance() {
		return PostBoxManagerHolder.INSTANCE;
	}

	public Postbox<PostboxMessage> getPostbox(String pboxName, PostboxListener<PostboxMessage> handler, boolean start){
		log.debug("");
		return this.getPostbox(pboxName, handler, null, start);
	}
	
	public Postbox<PostboxMessage> getPostbox(String pboxName, PostboxListener<PostboxMessage> handler, PostboxCallback<PostboxMessage> callback, boolean start) {
		log.debug("");
		Postbox<PostboxMessage> pbox = pboxMap.get(pboxName);
		if(pbox == null) {
			pbox = new Postbox<>(pboxName, handler, callback);
			pboxMap.put(pboxName, pbox);
		}
		if(start) {
			pbox.doStart();
		}
		return pbox;
	}

	public Postbox<PostboxMessage> getPostbox(String pboxName, PostboxListener<PostboxMessage> handler, boolean start, int sleepTime) {
		log.debug("");
		return this.getPostbox(pboxName, handler, null, start, sleepTime);
	}

	public Postbox<PostboxMessage> getPostbox(String pboxName, PostboxListener<PostboxMessage> handler, PostboxCallback<PostboxMessage> callback, boolean start, int sleepTime){
		Postbox<PostboxMessage> pbox = pboxMap.get(pboxName);
		if(pbox == null) {
			pbox = new Postbox<>(pboxName, handler, callback, sleepTime);
			pboxMap.put(pboxName, pbox);
		}
		if(start) {
			pbox.doStart();
		}
		return pbox;
	}
	
	public void removePostbox(String pboxName){
		pboxMap.remove(pboxName);
	}
}