//package com.mymes.equip.vplc.push;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class EventPusher {
//	
//	public static EventPusher newPusher() {
//		return new EventPusher();
//	}
//
//	public void push(PushEvent event) {
//		log.debug("EventPusher.push(PushEvent event)...");
//		SseEmitters.getSseEmitters().push(event);
//	}
//}
