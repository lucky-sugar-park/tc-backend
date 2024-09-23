package com.mymes.equip.vplc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mymes.equip.vplc.Types.PlcStatus;
import com.mymes.equip.vplc.VirtualPlcException;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class VirtualPlcServiceResourceReleaseShutdownHook {
	
	@Autowired
	private VirtualPlcManagementService virtualPlcManagementService;
	
	@Autowired
	private VirtualPlcManipulationService virtualPlcManupulationService;

	@PreDestroy
	public void releaseAllResource() {
		log.info("");
		try {
			List<VirtualPlcInfo> vplcList=virtualPlcManagementService.searchAllVirtualPlc();
			vplcList.forEach(vplc->{
				try {
					if(vplc.getStatus()!=PlcStatus.RELEASED && vplc.getStatus()!=PlcStatus.REGISTERED) {
						log.info("Releasing virtual plc network resourceL, Name: {}", vplc.getName());
						virtualPlcManupulationService.stopVirtualPlc(vplc.getId());	
					}
				} catch (VirtualPlcException e) {
					e.printStackTrace();
				}
			});
		} catch (VirtualPlcException e) {
			log.warn("", e);
		}
	}
}
