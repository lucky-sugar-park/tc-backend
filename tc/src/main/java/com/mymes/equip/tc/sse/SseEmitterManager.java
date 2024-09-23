package com.mymes.equip.tc.sse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SseEmitterManager {

	private static SseEmitterManager manager;
	
	private final List<FilterableSseEmitter> emitterList;
	
	private final Map<String, FilterableSseEmitter> emitterMap;

	private SseEmitterManager() {
		emitterList= new CopyOnWriteArrayList<>();
		emitterMap = new HashMap<>();
	}

	public static SseEmitterManager getManager() {
		if(manager==null) {
			manager=new SseEmitterManager();
		}
		return manager;
	}
	
	public void add(String userId, FilterableSseEmitter emitter) {
		emitterList.add(emitter);
		emitterMap.put(userId, emitter);
		
		log.info("New emitter added: {}", emitter); 
        log.info("Emitter list size: {}", emitterList.size());  
        emitter.onCompletion(() -> {  
            log.info("onCompletion callback");
            // 네트워크 에러 등의 이유로 연결이 만료되면 리스트에서 삭제
            this.emitterList.remove(emitter);
        });  
        emitter.onTimeout(() -> {  
            log.info("onTimeout callback");  
            emitter.complete();  
        }); 
	}
	
	public List<FilterableSseEmitter> getEmitterList() {
		return this.emitterList;
	}
	
	public FilterableSseEmitter getSseEmitter(String userId) {
		return emitterMap.get(userId);
	}
}
