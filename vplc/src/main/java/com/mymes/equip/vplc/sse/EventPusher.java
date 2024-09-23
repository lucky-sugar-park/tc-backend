package com.mymes.equip.vplc.sse;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.mymes.equip.vplc.sse.PushEventPayload.PushEventKind;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventPusher {
	
	private static final AtomicLong counter = new AtomicLong();

	public static EventPusher newPusher() {
		return new EventPusher();
	}
	
	/**
	 * 테스트용 함수
	 */
	public void count() {
    	long count=counter.incrementAndGet();
    	SseEventBuilder sseEventBuilder=
    			SseEmitter.event().name(PushEventKind.COUNT.getName()).data(count).reconnectTime(3000L);
    	SseEmitterManager.getManager().getEmitterList().forEach(emitter->{
    		try {
				emitter.send(sseEventBuilder);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	});
    }

	public void push(PushEventPayload payload) {
		log.debug("EventPusher.push(PushEvent event)...");
		SseEmitterManager.getManager().getEmitterList().forEach(emitter->{
    		try {
				emitter.send(payload);
			} catch (IOException e) {
				log.warn("Fail to pushing payload, {}", payload.toJson(), e);
			}
    	});
	}
}
