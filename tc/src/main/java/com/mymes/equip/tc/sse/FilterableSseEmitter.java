package com.mymes.equip.tc.sse;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilterableSseEmitter {
	
	private final String id;
	
	private final SseEmitter emitter;
	
	private final Set<String> acceptable;
	
	public FilterableSseEmitter(String id, SseEmitter emitter, List<String> acceptableEvents) {
		this.id=id;
		this.emitter=emitter;
		acceptable=new HashSet<>();

		if(acceptableEvents==null) return;

		acceptableEvents.forEach(eventName->{
			acceptable.add(eventName);
		});
	}

	public void send(String eventName, String data, long reconnectTime) throws IOException {
		log.debug(data);
		SseEventBuilder sseEventBuilder=
				SseEmitter.event()
					      .name(eventName)
					      .data(data)
					      .reconnectTime(reconnectTime);
		send(sseEventBuilder);
	}
	
	public void send(SseEventBuilder sseEventBuilder) throws IOException {
		log.info("Sending PushEvent to {}, with: {}", id);
		emitter.send(sseEventBuilder);
	}
	
	public void send(PushEventPayload payload) throws IOException {
		log.debug("");
		String name = payload.getEventKind().getName();
		String data = payload.toJson();

		try {
			if(acceptable.contains("*") || acceptable.contains(name)) {
				this.emitter.send(
						SseEmitter.event()
								  .id(payload.getEventId())
						          .name(name)
						          .data(data)
						          .comment(payload.getComment())
				);
				log.info("Sent PushEvent to {}, with: {}", id, data);
			} else {
				log.debug("No acceptable server's push event name {} to {}, with {}", name, id, data);
			}
		} catch (IOException e) {
			log.warn("Fail sending push event to {}, with: {}", id, data, e);
			throw e;
		}
	}
	
	public SseEmitter getEmitter() {
		return this.emitter;
	}
	
	public void onTimeout(Runnable callback) {
		emitter.onTimeout(callback);
	}
	
	public void onError(Consumer<Throwable> callback) {
		emitter.onError(callback);
	}
	
	public void onCompletion(Runnable callback) {
		emitter.onCompletion(callback);
	}
	
	public void complete() {
		emitter.complete();
	}
	
	public void completeWithError(Throwable ex) {
		emitter.completeWithError(ex);
	}
}
