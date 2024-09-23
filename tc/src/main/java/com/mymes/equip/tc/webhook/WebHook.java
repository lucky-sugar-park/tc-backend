package com.mymes.equip.tc.webhook;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.mymes.equip.tc.msg.Message;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class WebHook<T extends Message> {
	
	private static RestTemplate restTemplate=new RestTemplate();
	
	private long id;
	
	private String name;

	private String url;
	
	public WebHook() {
	}
	
	public WebHook(WebHookInfo webHookInfo) {
		this.id=webHookInfo.getId();
		this.name=webHookInfo.getName();
		this.url=webHookInfo.getUrl();
	}
	
	public void hook(T message) {
		log.debug("");

		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Message> request=new HttpEntity<>(message, headers);
		
		restTemplate.postForEntity(url, request, Message.class);
	}
}
