package com.mymes.equip.tc.endpoints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.mymes.equip.tc.sse.EventPusher;
import com.mymes.equip.tc.sse.FilterableSseEmitter;
import com.mymes.equip.tc.sse.PushEventPayload.PushEventKind;
import com.mymes.equip.tc.sse.SseEmitterManager;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins="*", allowedHeaders="*")
@RestController
@Slf4j
public class ServerSentEventController {

	@GetMapping(value="/connect/{userId}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<SseEmitter> connect(@PathVariable("userId") String userId) {
		log.debug("");
		List<String> acceptable=new ArrayList<>();
		acceptable.add("*");
		FilterableSseEmitter femitter = new FilterableSseEmitter(userId, new SseEmitter(60*1000L), acceptable);
        SseEmitterManager.getManager().add(userId, femitter);
        try {
        	femitter.send(SseEmitter.event()  
                   .name(PushEventKind.CONNECTED.getName())   // 이벤트 이름 지정
                   .data("Connected with " + userId + "!"));  // 503에러 방지를 위한 더미 데이터
        } catch (IOException e) {  
            throw new RuntimeException(e);  
        }  
        return ResponseEntity.ok(femitter.getEmitter());  
	}
	
	@PostMapping(value="/connect/{userId}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<SseEmitter> connect(@RequestBody List<String> acctableList) {
		return null;
	}
	
	@GetMapping("/count")
	public ResponseEntity<Void> countClient() {
		new EventPusher().count();
		return ResponseEntity.ok().build();
	}
}
