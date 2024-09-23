package com.mymes.equip.tc.endpoints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mymes.equip.tc.Condition;
import com.mymes.equip.tc.webhook.WebHookInfo;
import com.mymes.equip.tc.webhook.WebHookManagementService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="/api/v1/webhook")
@Slf4j
public class WebHookManagementController {

	@Autowired
	private WebHookManagementService webHookManagementService;
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void registerWebHook(@RequestBody WebHookInfo webHookInfo) {
		log.debug("");
		webHookManagementService.registerWebHook(webHookInfo);
	}
	
	@RequestMapping(value="/check-url", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public Map<String, String> checkWebHookUrl(@RequestBody Map<String, String> data) {
		log.debug("");
		String url=data.get("url");
		String sampleData=data.get("sampleData");
		Map<String, String> result=new HashMap<>();
		webHookManagementService.checkUrl(url, sampleData, result);
		return result;
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void updateWebHook(@RequestBody WebHookInfo webHookInfo) {
		log.debug("");
		webHookManagementService.updateWebHook(webHookInfo);
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void deleteWebHook(@PathVariable("id") long id) {
		log.debug("");
		webHookManagementService.deleteWebHook(id);
	}
	
	@RequestMapping(value="/delete/byName/{name}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void deleteWebHookByName(@PathVariable("name") String name) {
		log.debug("");
		webHookManagementService.deleteWebHook(name);
	}
	
	@RequestMapping(value="/find/{id}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public WebHookInfo findWebHook(@PathVariable("id") long id) {
		log.debug("");
		return webHookManagementService.findWebHookById(id);
	}
	
	@RequestMapping(value="/find/byName/{name}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public WebHookInfo findWebHookByName(@PathVariable("name") String name) {
		log.debug("");
		return webHookManagementService.findWebHookByName(name);
	}
	
	@RequestMapping(value="/search/all", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public List<WebHookInfo> searchAllWebHook() {
		log.debug("");
		return webHookManagementService.searchAllWebHook();
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public List<WebHookInfo> searchAllWebHook(@RequestBody Condition cond) {
		log.debug("");
		return webHookManagementService.searchWebHook(cond);
	}
}
