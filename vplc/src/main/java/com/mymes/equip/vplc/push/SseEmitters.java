//package com.mymes.equip.vplc.push;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.concurrent.atomic.AtomicLong;
//
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Component
//@Slf4j
//public class SseEmitters {
//	
//	private static final AtomicLong counter = new AtomicLong();
//	
//	private static SseEmitters sseEmitters;
//	
//	private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
//	  
//	public SseEmitters() {
//		sseEmitters=this;
//	}
//	
//	public static SseEmitters getSseEmitters() {
//		return sseEmitters;
//	}
//
//    public SseEmitter add(SseEmitter emitter) {  
//        this.emitters.add(emitter);
//        log.info("new emitter added: {}", emitter); 
//        log.info("emitter list size: {}", emitters.size());  
//        emitter.onCompletion(() -> {  
//            log.info("onCompletion callback");
//            // 네트워크 에러 등의 이유로 연결이 만료되면 리스트에서 삭제
//            this.emitters.remove(emitter);
//        });  
//        emitter.onTimeout(() -> {  
//            log.info("onTimeout callback");  
//            emitter.complete();  
//        }); 
// 
//        return emitter;  
//    }
//    
//    public void count() {
//    	long count=counter.incrementAndGet();
//    	SseEventBuilder sseEventBuilder=SseEmitter.event().name("count").data(count).reconnectTime(3000L);
//    	emitters.forEach(emitter->{
//    		try {
//    			log.info("Sending SseEmitter's count: {}", count);
//    			emitter.send(sseEventBuilder);
//    		} catch (IOException e) {
//    			log.warn("Fail sending push event to {}, with: {}", emitter.toString(), count, e);
//    		}
//    	});
//    }
//    
//    public void push(PushEvent event) {
//    	log.debug("SseEmitters.push(PushEvent event)...");
//    	if(event==null) return;
// 
//    	SseEventBuilder sseEventBuilder=
//    			SseEmitter.event()
//    					  .name(event.getEventKind().name())
//    			          .data(event.toJson(), MediaType.APPLICATION_JSON)
//    			          .reconnectTime(3000L);
//    	emitters.forEach(emitter->{
//    		try {
//    			log.info("Sending PushEvent to {}, with: {}", emitter.toString(), event.toJson());
//    			emitter.send(sseEventBuilder);
//    		} catch (IOException e) {
//    			log.warn("Fail sending push event to {}, with: {}", emitter.toString(), event.toJson(), e);
//    		}
//    	});
//    }
//}
//
