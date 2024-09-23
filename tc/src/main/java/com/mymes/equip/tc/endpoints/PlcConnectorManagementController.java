package com.mymes.equip.tc.endpoints;

import java.util.Collections;
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
import com.mymes.equip.tc.persist.PageResponse;
import com.mymes.equip.tc.plc.service.PlcConnectionEventHistInfo;
import com.mymes.equip.tc.plc.service.PlcConnectionEventHistoryService;
import com.mymes.equip.tc.plc.service.PlcConnectorInfo;
import com.mymes.equip.tc.plc.service.PlcConnectorManagementService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="/api/v1/plc-connector")
@Slf4j
public class PlcConnectorManagementController {

	@Autowired
	private PlcConnectorManagementService plcConnectorManagementService;
	
	@Autowired
	private PlcConnectionEventHistoryService plcConnectionEventHistoryService;
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void registerPlcConnector(@RequestBody PlcConnectorInfo plcConnectorInfo) {
		log.debug("PlcConnectorManagementController.registerPlcConnector(PlcConnectorInfo plcConnectorInfo)...");
		plcConnectorManagementService.registerPlcConnector(plcConnectorInfo);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void updatePlcConnector(@RequestBody PlcConnectorInfo plcConnectorInfo) {
		log.debug("PlcConnectorManagementController.updatePlcConnector(PlcConnectorInfo plcConnectorInfo)...");
		plcConnectorManagementService.updatePlcConnector(plcConnectorInfo);
	}
	
	@RequestMapping(value="/delete/{plcConnectorId}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void deletePlcConnector(@PathVariable("plcConnectorId") long plcConnectorId) {
		log.debug("PlcConnectorManagementController.deletePlcConnector(long plcConnectorId)...");
		plcConnectorManagementService.deletePlcConnectorById(plcConnectorId);
	}
	
	@RequestMapping(value="/delete/byName/{plcConnectorName}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void deletePlcConnector(@PathVariable("plcConnectorName") String plcConnectorName) {
		log.debug("PlcConnectorManagementController.deletePlcConnector(String plcConnectorName)...");
		plcConnectorManagementService.deletePlcConnectorByName(plcConnectorName);
	}
	
	@RequestMapping(value="/find/{plcConnectorId}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public PlcConnectorInfo findPlcConnector(@PathVariable("plcConnectorId") long plcConnectorId) {
		log.debug("PlcConnectorManagementController.findPlc(long plcConnectorId)...");
		return plcConnectorManagementService.findPlcConnectorById(plcConnectorId);
	}
	
	@RequestMapping(value="/find/byName/{plcConnectorName}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public PlcConnectorInfo findPlcConnectorByName(@PathVariable("plcConnectorName") String plcConnectorName) {
		log.debug("PlcConnectorManagementController.findPlcConnectorByName(String plcConnectorName)...");
		return plcConnectorManagementService.findPlcConnectorByName(plcConnectorName);
	}
	
	@RequestMapping(value="/search/all", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public List<PlcConnectorInfo> searchAllPlcConnectores() {
		log.debug("PlcConnectorManagementController.searchAllPlcConnectores()...");
		return plcConnectorManagementService.searchAllPlcConnectores();
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public List<PlcConnectorInfo> search(@RequestBody Condition cond) {
		log.debug("PlcConnectorManagementController.search(SearchCondition cond)...");
		return plcConnectorManagementService.searchPlcConnectores(cond);
	}
	
	@RequestMapping(value="/control/publish/{plcConnectorId}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public Map<String, Boolean> publishPlcConnector(@PathVariable long plcConnectorId) {
		log.debug("PlcConnectorManagementController.publishPlcConnector(long plcConnectorId)...");
		boolean result=plcConnectorManagementService.publishPlcConnector(plcConnectorId);
		return Collections.singletonMap("success", result);
	}
	
	@RequestMapping(value="/control/publish/byName/{plcConnectorName}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public Map<String, Boolean> publishPlcConnectorByName(@PathVariable String plcConnectorName) {
		log.debug("PlcConnectorManagementController.publishPlcConnectorByName(String plcConnectorName)...");
		boolean result=plcConnectorManagementService.publishPlcConnectorByName(plcConnectorName);
		return Collections.singletonMap("success", result);
	}
	
	@RequestMapping(value="/control/release/{plcConnectorId}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public Map<String, Boolean> releasePlcConnector(@PathVariable long plcConnectorId) {
		log.debug("PlcConnectorManagementController.releasePlcConnector(long plcConnectorId)...");
		boolean result=plcConnectorManagementService.releasePlcConnector(plcConnectorId);
		return Collections.singletonMap("success", result);
	}
	
	@RequestMapping(value="/control/release/byName/{plcConnectorName}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public Map<String, Boolean> releasePlcConnectorByName(@PathVariable String plcConnectorName) {
		log.debug("PlcConnectorManagementController.releasePlcConnectorByName(String plcConnectorName)...");
		boolean result=plcConnectorManagementService.releasePlcConnectorByName(plcConnectorName);
		return Collections.singletonMap("success", result);
	}
	
	@RequestMapping(value="/control/pause/{plcConnectorId}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public Map<String, Boolean> pausePlcConnector(@PathVariable long plcConnectorId) {
		log.debug("PlcConnectorManagementController.pausePlcConnector(long plcConnectorId)...");
		boolean result=plcConnectorManagementService.pausePlcConnector(plcConnectorId);
		return Collections.singletonMap("success", result);
	}
	
	@RequestMapping(value="/control/pause/byName/{plcConnectorName}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public Map<String, Boolean> pausePlcConnectorByName(@PathVariable String plcConnectorName) {
		log.debug("PlcConnectorManagementController.pausePlcConnectorByName(String plcConnectorName)...");
		boolean result=plcConnectorManagementService.pausePlcConnectorByName(plcConnectorName);
		return Collections.singletonMap("success", result);
	}
	
	@RequestMapping(value="/control/resume/{plcConnectorId}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public Map<String, Boolean> resumePlcConnector(@PathVariable long plcConnectorId) {
		log.debug("PlcConnectorManagementController.resumePlcConnector(long plcConnectorId)...");
		boolean result=plcConnectorManagementService.resumePlcConnector(plcConnectorId);
		return Collections.singletonMap("success", result);
	}
	
	@RequestMapping(value="/control/resume/byName/{plcConnectorName}", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public Map<String, Boolean> resumePlcConnectorByName(@PathVariable String plcConnectorName) {
		log.debug("PlcConnectorManagementController.resumePlcConnectorByName(String plcConnectorName)...");
		boolean result=plcConnectorManagementService.resumePlcConnectorByName(plcConnectorName);
		return Collections.singletonMap("success", result);
	}
	
	@RequestMapping(value="/test-connection/{plcConnectorId}", method=RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public Map<String, String> testConnection(@PathVariable long plcConnectorId) {
		log.debug("PlcConnectorManagementController.testConnection(long plcConnectorId)...");
		return plcConnectorManagementService.testConnection(plcConnectorId);
	}
	
	@RequestMapping(value="/test-connection/byName/{plcConnectorName}", method=RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public Map<String, String> testConnectionByName(@PathVariable String plcConnectorName) {
		log.debug("PlcConnectorManagementController.testConnectionByName(String plcConnectorName)...");
		return plcConnectorManagementService.testConnectionByName(plcConnectorName);
	}
	
	@RequestMapping(value="/test-connection/byIpAddress/{ip}/{port}", method=RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public Map<String, String> testConnectionByIpAddress(@PathVariable String ip, @PathVariable int port) {
		log.debug("PlcConnectorManagementController.testConnectionByName(String plcName)...");
		return plcConnectorManagementService.testConnectionByIpAddress(ip, port);
	}
	
	@RequestMapping(value="/search/connection-history", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public PageResponse<PlcConnectionEventHistInfo> searchPlcConnectionEventHistories(@RequestBody Condition cond) {
		log.debug("");
		return plcConnectionEventHistoryService.searchPlcConnectionEventHist(cond);
	}
}
