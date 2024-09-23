package com.mymes.equip.vplc.postbox;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Postbox<T extends PostboxMessage> extends Thread {
	
	public enum PostboxStatus {
		STANDBY, RUNNING, PAUSED 
	}

	private String pboxName;

	private PostboxStatus status;

	private Deque<T> innerQueue;
	
	private PostboxListener<T> handler;
	
	private PostboxCallback<T> callback;
	
	private long sleepTime=10;
	
	public Postbox(String pboxName, PostboxListener<T> handler) {
		this(pboxName, handler, null);
	}
	
	public Postbox(String pboxName,	PostboxListener<T> handler, int sleepTime) {
		this(pboxName, handler, null, sleepTime);
	}

	public Postbox(String pboxName, PostboxListener<T> handler, PostboxCallback<T> callback) {
		this.pboxName = pboxName;
		innerQueue = new ConcurrentLinkedDeque<T>();
		this.handler = handler;
		this.callback = callback;
		this.status=PostboxStatus.STANDBY;
	}

	public Postbox(String pboxName, PostboxListener<T> handler, PostboxCallback<T> callback, int sleepTime) {
		this.pboxName = pboxName;
		innerQueue = new ConcurrentLinkedDeque<T>();
		this.handler = handler;
		this.callback = callback;
		this.sleepTime = sleepTime;
		this.status=PostboxStatus.STANDBY;
	}
	
	@Override
	public void run() {
		super.run();
		status=PostboxStatus.RUNNING;
		// 상태가 standby (stop) 상태이면 Thread를 끝냄
		// doStart가 호출되면 다시 시작하개 됨
		while (status!=PostboxStatus.STANDBY) {
			// 상태가 paused 이면 메시지를 처리하지 않음 (paused 상태에서는 메시지가 post되지 않기 때문에 overflow 염려는 없음)  
			if (!innerQueue.isEmpty() && status!=PostboxStatus.PAUSED) {
				if (Thread.interrupted()) return;

				T message = null;
				try {
					message = innerQueue.remove();
					if (!this.handler.onMessage(message) && this.callback!=null) {
						callback.callback(this, message, null);
					}
				} catch (Throwable t) {
					if (callback != null) {
						callback.callback(this, message, t);
					}
				}
			}
			doSleep(sleepTime);
		}
	}


	public void postMessage(T message) {
		log.debug("");
		if(this.status==PostboxStatus.RUNNING) {
			innerQueue.add(message);
		}
	}

	public void doStart() {
		if(this.status!=PostboxStatus.STANDBY) return;

		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(this);
		service.shutdown();
	}

	public void doPause() {
		this.status=PostboxStatus.PAUSED;
	}

	public void doResume() {
		this.status=PostboxStatus.RUNNING;
	}

	public void doStandby() {
		this.status=PostboxStatus.STANDBY;
	}

	public String getPostboxName() { return pboxName; }

	public Deque<T> getInnerQueue() { return innerQueue; }

	public int getQueueMessageCount() { return innerQueue.size(); }

	public PostboxListener<T> getHandler() { return this.handler; }
	
	public PostboxStatus getStatus() { return this.status; }
	
	private void doSleep(long sleepTime) {
		if(sleepTime > 0) {
			synchronized (innerQueue) {
				try {
					innerQueue.wait(sleepTime);
				} catch (InterruptedException e) {
					log.warn("");
				}
			}
		}

	}
}
