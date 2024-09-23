package com.mymes.equip.vplc.endpoints;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mymes.equip.vplc.Condition;
import com.mymes.equip.vplc.VirtualPlcException;
import com.mymes.equip.vplc.machine.MemoryInfo;
import com.mymes.equip.vplc.service.ConnectionClientHistoryInfo;
import com.mymes.equip.vplc.service.ConnectionInfo;
import com.mymes.equip.vplc.service.VirtualPlcHistoryInfo;
import com.mymes.equip.vplc.service.VirtualPlcInfo;
import com.mymes.equip.vplc.service.VirtualPlcManagementService;
import com.mymes.equip.vplc.service.VirtualPlcManipulationService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins="*", allowedHeaders="*")
@Version1RestController
@Slf4j
public class VirtualPlcRestController {
	
	@Autowired
	private VirtualPlcManagementService virtualPlcManagementService;
	
	@Autowired
	private VirtualPlcManipulationService virtualPlcManipulationService;
	
	@PostMapping(value="/management/register")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void registerVirtualPlc (@RequestBody VirtualPlcInfo vplcInfo) throws VirtualPlcException {
		log.debug("VirtualPlcController.registerVirtualPlc (@RequestBody VirtualPlcInfo vplcInfo)...");
		virtualPlcManagementService.registerVirtualPlc(vplcInfo);
	}
	
	@PostMapping(value="/management/update")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void updateVirtualPlc(@RequestBody VirtualPlcInfo vplcInfo) throws VirtualPlcException {
		log.debug("");
		virtualPlcManagementService.updateVirtualPlc(vplcInfo);
	}
	
	@PostMapping(value="/management/update/port")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void updateVirtualPlcPort(@RequestBody PortUpdateInfo portUpdateInfo) throws VirtualPlcException {
		log.debug("");
		String vplcId=portUpdateInfo.getVplcId();
		String vplcName=portUpdateInfo.getVplcName();
		int startPort=portUpdateInfo.getStartPort();
		int portCount=portUpdateInfo.getPortCount();
		int portConnLimit=portUpdateInfo.getPortConnLimit();
		if(vplcId!=null) {
			virtualPlcManagementService.updateVirtualPlcPort(vplcId, startPort, portCount, portConnLimit);	
		} else if(vplcName!=null) {
			virtualPlcManagementService.updateVirtualPlcPortByName(vplcName, startPort, portCount, portConnLimit);
		} else {
			log.warn("");
			throw new VirtualPlcException();
		}
	}
	

	@GetMapping("/management/publish/{vplcId}")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void publishVirtualPlc(@PathVariable("vplcId") String vplcId) throws VirtualPlcException {
		log.debug("");
		virtualPlcManagementService.publishVirtualPlc(vplcId);
	}

	@GetMapping("/management/publish/byName/{vplcName}")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void publishVirtualPlcByName(@PathVariable("vplcName") String vplcName) throws VirtualPlcException {
		log.debug("");
		virtualPlcManagementService.publishVirtualPlcByName(vplcName);
	}
	
	@GetMapping("/management/release/{vplcId}")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void releaseVirtualPlc(@PathVariable("vplcId") String vplcId) throws VirtualPlcException {
		log.debug("");
		virtualPlcManagementService.releaseVirtualPlc(vplcId);
	}

	@GetMapping("/management/release/byName/{vplcName}")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void releaseVirtualPlcByName(@PathVariable("vplcName") String vplcName) throws VirtualPlcException {
		log.debug("");
		virtualPlcManagementService.releaseVirtualPlcByName(vplcName);
	}
	
	@GetMapping(value="/management/delete/{vplcId}")
	public void deleteVirtualPlc(@PathVariable("vplcId") String vplcId) throws VirtualPlcException {
		log.debug("");
		virtualPlcManagementService.deleteVirtualPlc(vplcId);
	}
	
	@GetMapping(value="/management/delete/byName/{vplcName}")
	public void deleteVirtualPlcByName(@PathVariable("vplcName") String vplcName) throws VirtualPlcException {
		log.debug("");
		virtualPlcManagementService.deleteVirtualPlcByName(vplcName);
	}
	
	@GetMapping(value="/management/find/{vplcId}")
	public VirtualPlcInfo findVirtualPlc(@PathVariable("vplcId") String vplcId) throws VirtualPlcException {
		log.debug("");
		return virtualPlcManagementService.findVirtualPlc(vplcId);
	}
	
	@GetMapping(value="/management/find/byName/{vplcName}")
	@ResponseBody
	public VirtualPlcInfo findVirtualPlcByName(@PathVariable("vplcName") String vplcName) throws VirtualPlcException {
		log.debug("");
		return virtualPlcManagementService.findVirtualPlcByName(vplcName);
	}
	
	@GetMapping("/management/search")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public List<VirtualPlcInfo> searchAllVirtualPlcs() throws VirtualPlcException {
		log.debug("");
		return virtualPlcManagementService.searchAllVirtualPlc();
	}
	
	@PostMapping(value="/management/search")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public List<VirtualPlcInfo> searchVirtualPlc(@RequestBody Condition cond) throws VirtualPlcException {
		log.debug("");
		return virtualPlcManagementService.searchVirtualPlc(cond);
	}
	
	@PostMapping(value="/management/search/histories")
	@ResponseBody
	public List<VirtualPlcHistoryInfo> searchVirtualPlcHistory(@RequestBody Condition cond) throws VirtualPlcException {
		log.debug("");
		return virtualPlcManagementService.searchVirtualPlcHistory(cond);
	}
	
	@PostMapping(value="/management/search/connection/client/histories")
	@ResponseBody
	public List<ConnectionClientHistoryInfo> searchConnectionClientHistory(@RequestBody Condition cond) throws VirtualPlcException {
		log.debug("");
		return virtualPlcManagementService.searchConnectionClientHistory(cond);
	}
	
	@GetMapping(value="/management/search/connection/{vplcId}")
	@ResponseBody
	public List<ConnectionInfo> searchConnections(@PathVariable("vplcId") String vplcId) throws VirtualPlcException {
		log.debug("");
		return virtualPlcManagementService.searchConnections(vplcId);
	}
	
	@GetMapping(value="/management/search/connection/byPlcName/{vplcName}")
	@ResponseBody
	public List<ConnectionInfo> searchConnectionsByVplcName(@PathVariable("vplcName") String vplcName) throws VirtualPlcException {
		log.debug("");
		return virtualPlcManagementService.searchConnectionsByVplcName(vplcName);
	}
	
	//======================= for manipulation =========================

	@GetMapping("/control/start/{vplcId}")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public VirtualPlcInfo startVirtualPlc(@PathVariable("vplcId") String vplcId) throws VirtualPlcException {
		log.debug("");
		virtualPlcManipulationService.startVirtualPlc(vplcId);
		VirtualPlcInfo vplcInfo=virtualPlcManagementService.findVirtualPlc(vplcId);
		return vplcInfo;
	}

	@GetMapping("/control/start/byName/{vplcName}")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public VirtualPlcInfo startVirtualPlcByName(@PathVariable("vplcName") String vplcName) throws VirtualPlcException {
		log.debug("");
		virtualPlcManipulationService.startVirtualPlcByName(vplcName);
		return virtualPlcManagementService.findVirtualPlcByName(vplcName);
	}

	@GetMapping("/control/stop/{vplcId}")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public VirtualPlcInfo stopVirtualPlc(@PathVariable("vplcId") String vplcId) throws VirtualPlcException {
		log.debug("");
		virtualPlcManipulationService.stopVirtualPlc(vplcId);
		return virtualPlcManagementService.findVirtualPlc(vplcId);
	}

	@GetMapping("/control/stop/byName/{vplcName}")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public VirtualPlcInfo stopVirtualPlcByName(@PathVariable("vplcName") String vplcName) throws VirtualPlcException {
		log.debug("");
		virtualPlcManipulationService.stopVirtualPlcByName(vplcName);
		return virtualPlcManagementService.findVirtualPlcByName(vplcName);
	}

	@GetMapping("/control/pause/{vplcId}")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public VirtualPlcInfo pauseVirtualPlc(@PathVariable("vplcId") String vplcId) throws VirtualPlcException {
		log.debug("");
		virtualPlcManipulationService.pauseVirtualPlc(vplcId);
		return virtualPlcManagementService.findVirtualPlc(vplcId);
	}

	@GetMapping("/control/pause/byName/{vplcName}")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public VirtualPlcInfo pauseVirtualPlcByName(@PathVariable("vplcName") String vplcName) throws VirtualPlcException {
		log.debug("");
		virtualPlcManipulationService.pauseVirtualPlcByName(vplcName);
		return virtualPlcManagementService.findVirtualPlcByName(vplcName);
	}

	@GetMapping("/control/resume/{vplcId}")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public VirtualPlcInfo resumeVirtualPlc(@PathVariable("vplcId") String vplcId) throws VirtualPlcException {
		log.debug("");
		virtualPlcManipulationService.resumeVirtualPlc(vplcId);
		return virtualPlcManagementService.findVirtualPlc(vplcId);
	}

	@GetMapping("/control/resume/byName/{vplcName}")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public VirtualPlcInfo resumeVirtualPlcByName(@PathVariable("vplcName") String vplcName) throws VirtualPlcException {
		log.debug("");
		virtualPlcManipulationService.resumeVirtualPlcByName(vplcName);
		return virtualPlcManagementService.findVirtualPlcByName(vplcName);
	}

	@PostMapping("/manipulation/read")
	@CrossOrigin
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public MemoryInfo read(@RequestBody VplcReadRequest readReq) throws VirtualPlcException {
		log.debug("");

		String vplcId=readReq.getVplcId();
		String vplcName=readReq.getVplcName();
		String memoryType=readReq.getMemoryType();
		String frameFormat=readReq.getFrameFormat();
		int startAddress=readReq.getStartAddress();
		int wordCount=readReq.getWordCount();
		
		if(vplcId!=null) {
			return virtualPlcManipulationService.read(vplcId, memoryType, frameFormat, startAddress, wordCount);
		} else if(vplcName!=null) {
			return virtualPlcManipulationService.readByVplcName(vplcName, memoryType, frameFormat, startAddress, wordCount);
		} else {
			throw new VirtualPlcException();
		}
	}

	@PostMapping("/manipulation/write")
	@ResponseBody
	public MemoryInfo write(@RequestBody VplcWriteRequest writeReq) throws VirtualPlcException {
		log.debug("");

		String vplcId=writeReq.getVplcId();
		String vplcName=writeReq.getVplcName();
		String memoryType=writeReq.getMemoryType();
		String frameFormat=writeReq.getFrameFormat();
		int startAddress=writeReq.getStartAddress();
		int wordCount=writeReq.getWordCount();
		String data=writeReq.getData();
		
		if(vplcId!=null) {
			return virtualPlcManipulationService.write(vplcId, memoryType, frameFormat, startAddress, wordCount, data);
		} else if(vplcName!=null) {
			return virtualPlcManipulationService.writeByVplcName(vplcName, memoryType, frameFormat, startAddress, wordCount, data);
		} else {
			throw new VirtualPlcException();
		}
	}
	
	@PostMapping("/manipulation/clear/all")
	@ResponseBody
	public MemoryInfo clearAll(@RequestBody VplcClearAllRequest clearAllReq) throws VirtualPlcException {
		log.debug("");
		String vplcId=clearAllReq.getVplcId();
		String vplcName=clearAllReq.getVplcName();
		String memoryType=clearAllReq.getMemoryType();
		String frameFormat=clearAllReq.getFrameFormat();
		int currentPosition=clearAllReq.getCurrentPosition();
		
		if(vplcId!=null) {
			return virtualPlcManipulationService.clearAll(vplcId, memoryType, frameFormat, currentPosition);
		} else if(vplcName!=null) {
			return virtualPlcManipulationService.clearAllByVplcName(vplcName, memoryType, frameFormat, currentPosition);
		} else {
			throw new VirtualPlcException();
		}
	}
}
