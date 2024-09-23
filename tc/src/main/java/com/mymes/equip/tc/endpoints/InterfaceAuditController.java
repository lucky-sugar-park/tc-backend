package com.mymes.equip.tc.endpoints;

import java.util.Date;
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
import com.mymes.equip.tc.msg.audit.ExchangedMessageAuditInfo;
import com.mymes.equip.tc.msg.audit.ExchangedMessageAuditService;
import com.mymes.equip.tc.persist.PageResponse;
import com.mymes.equip.tc.persist.RangeResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="/api/v1/ifsvc/audit")
@Slf4j
public class InterfaceAuditController {

	@Autowired
	private ExchangedMessageAuditService exchangedMessageAuditService;
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void deleteMessageAudit(@PathVariable("id") long id) {
		log.debug("");
		exchangedMessageAuditService.deleteMessageAudit(id);
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void deleteMessageAuditList(@RequestBody List<Long> idArr) {
		log.debug("");
		exchangedMessageAuditService.deleteMessageAudits(idArr);
	}
	
	@RequestMapping(value="/delete/timestamp", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public Map<String, Long> deleteMessageByTimestamp(@RequestBody Date timestamp) {
		log.debug("");
		long count=exchangedMessageAuditService.deleteMessageAudits(timestamp);
		Map<String, Long> map=new HashMap<>();
		map.put("deletedCount", count);
		return map;
	}
	
	@RequestMapping(value="/find/{id}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public ExchangedMessageAuditInfo findMessageAudit(@PathVariable("id") long id) {
		log.debug("");
		return exchangedMessageAuditService.findMessageAuditById(id);
	}
	
	@RequestMapping(value="/search", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public List<ExchangedMessageAuditInfo> searchMessageAudit() {
		log.debug("");
		return exchangedMessageAuditService.searchMessageAudits();
	}
	
	@RequestMapping(value="/search/page", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public PageResponse<ExchangedMessageAuditInfo> searchMessageAudit(@RequestBody Condition cond) {
		log.debug("");
		return exchangedMessageAuditService.searchMessageAuditsToPageResponse(cond);
	}
	
	@RequestMapping(value="/search/range", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public RangeResponse<ExchangedMessageAuditInfo> searchMessageAuditByRange(@RequestBody Condition cond) {
		log.debug("");
		return exchangedMessageAuditService.searchMessageAuditsByRange(cond);
	}
}
