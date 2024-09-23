package org.sample.server;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class WebHookController {

	@PostMapping(value="/test-1")
	@CrossOrigin
	public void receiveData1(@RequestBody TcMessageResponse data) {
		log.info("WebHook-1 data arrived ...");
		Gson gson=new GsonBuilder().setPrettyPrinting().create();
		log.info("Data from PLC: {}\n", gson.toJson(data));
	}
	
	@PostMapping(value="/test-2")
	@CrossOrigin
	public void receiveData2(@RequestBody TcMessageResponse data) {
		log.info("WebHook-2 data arrived ...");
		Gson gson=new GsonBuilder().setPrettyPrinting().create();
		log.info("Data from PLC: {}\n", gson.toJson(data));
	}
	
	@PostMapping(value="/test-3")
	@CrossOrigin
	public void receiveData3(@RequestBody TcMessageResponse data) {
		log.info("WebHook-3 data arrived ...");
		Gson gson=new GsonBuilder().setPrettyPrinting().create();
		log.info("Data from PLC: {}\n", gson.toJson(data));
	}
	
	@PostMapping(value="/test-4")
	@CrossOrigin
	public void receiveData4(@RequestBody TcMessageResponse data) {
		log.info("WebHook-4 data arrived ...");
		Gson gson=new GsonBuilder().setPrettyPrinting().create();
		log.info("Data from PLC: {}\n", gson.toJson(data));
	}
	
	@PostMapping(value="/test-5")
	@CrossOrigin
	public void receiveData5(@RequestBody TcMessageResponse data) {
		log.info("WebHook-5 data arrived ...");
		Gson gson=new GsonBuilder().setPrettyPrinting().create();
		log.info("Data from PLC: {}\n", gson.toJson(data));
	}
}
