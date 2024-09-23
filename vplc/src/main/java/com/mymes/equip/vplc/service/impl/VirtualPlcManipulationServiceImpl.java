package com.mymes.equip.vplc.service.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mymes.equip.vplc.Types.FrameFormat;
import com.mymes.equip.vplc.Types.MemoryType;
import com.mymes.equip.vplc.Types.PlcStatus;
import com.mymes.equip.vplc.VirtualPlcException;
import com.mymes.equip.vplc.machine.MemoryInfo;
import com.mymes.equip.vplc.machine.VirtualProgrammableController;
import com.mymes.equip.vplc.machine.VirtualProgrammableControllerBuilder;
import com.mymes.equip.vplc.machine.VirtualProgrammableControllerManager;
import com.mymes.equip.vplc.persist.PersistentService;
import com.mymes.equip.vplc.service.VirtualPlcInfo;
import com.mymes.equip.vplc.service.VirtualPlcManagementService;
import com.mymes.equip.vplc.service.VirtualPlcManipulationService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VirtualPlcManipulationServiceImpl implements VirtualPlcManipulationService, PersistentService<VirtualPlcInfo, VirtualPlcEntity> {

	private VirtualProgrammableControllerManager vplcManager;
	
	@Autowired
	private VirtualPlcManagementService vplcManagementService;
	
	public VirtualPlcManipulationServiceImpl() {
		vplcManager=VirtualProgrammableControllerManager.getManager();
	}
	
	private void addVirtualProgrammableController(VirtualPlcInfo vplcInfo) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.addVirtualProgrammableController(VirtualPlcInfo vplcInfo)...");

		VirtualProgrammableController vplc=new VirtualProgrammableControllerBuilder()
				.withId(vplcInfo.getId())
				.withName(vplcInfo.getName())
				.withIpAddress(vplcInfo.getIpAddress())
				.withStatus(PlcStatus.PUBLISHED)
				.withStartPort(vplcInfo.getStartPort())
				.withFrameFormat(vplcInfo.getFrameFormat())
				.withPortCount(vplcInfo.getPortCount())
				.withConnLimit(vplcInfo.getPortConnLimit())
				.withMemoryTypes(vplcInfo.getMemInfos())
				.withManufacturer(vplcInfo.getManufacturer())
				.withDescription(vplcInfo.getDescription())
				.build();
		vplcManager.addVirtualProgrammableController(vplc.getId(), vplc);
	}
	
	@Override
	@Transactional
	public void publishVirtualPlc(VirtualPlcInfo vplcInfo) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.publishVirtualPlc(VirtualPlcInfo vplcInfo)...");
		this.addVirtualProgrammableController(vplcInfo);
	}
	
	@Override
	@Transactional
	public void publishVirtualPlc(String vplcId) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.publishVirtualPlc(String vplcId)...");
		if(vplcManager.getVirtualProgrammableController(vplcId)!=null) {
			throw new VirtualPlcException();
		}
		VirtualPlcInfo vplcInfo=updateVirtualPlcStatus(vplcId, null, PlcStatus.PUBLISHED);
		this.addVirtualProgrammableController(vplcInfo);
	}

	@Override
	@Transactional
	public void publishVirtualPlcByName(String vplcName) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.publishVirtualPlcByName(String vplcName)...");
		VirtualPlcInfo vplcInfo=updateVirtualPlcStatus(null, vplcName, PlcStatus.PUBLISHED);
		this.addVirtualProgrammableController(vplcInfo);
	}
	
	private void startVirtualProgrammableController(String vplcId) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.startVirtualProgrammableController(String vplcId)...");
		vplcManager.getVirtualProgrammableController(vplcId).start();
	}
	
	@Override
	@Transactional
	public void startVirtualPlc(String vplcId) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.startVirtualPlc(String vplcId)...");
		updateVirtualPlcStatus(vplcId, null, PlcStatus.RUNNING);
		this.startVirtualProgrammableController(vplcId);
	}

	@Override
	@Transactional
	public void startVirtualPlcByName(String vplcName) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.startVirtualPlcByName(String vplcName)...");
		VirtualPlcInfo vplcInfo=updateVirtualPlcStatus(null, vplcName, PlcStatus.RUNNING);
		this.startVirtualProgrammableController(vplcInfo.getId());
	}

	private void stopVirtualProgrammableController(String vplcId) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.stopVirtualProgrammableController(String vplcId)...");
		vplcManager.getVirtualProgrammableController(vplcId).stop();
	}
	
	@Override
	@Transactional
	public void stopVirtualPlc(String vplcId) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.stopVirtualPlc(String vplcId)...");
		updateVirtualPlcStatus(vplcId, null, PlcStatus.STOPPED);
		this.stopVirtualProgrammableController(vplcId);
	}	

	@Override
	@Transactional
	public void stopVirtualPlcByName(String vplcName) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.stopVirtualPlcByName(String vplcName)...");
		VirtualPlcInfo vplcInfo=updateVirtualPlcStatus(null, vplcName, PlcStatus.STOPPED);
		this.stopVirtualProgrammableController(vplcInfo.getId());
	}
	
	private void pauseVirtualProgrammableController(String vplcId) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.pauseVirtualProgrammableController(String vplcId)...");
		vplcManager.getVirtualProgrammableController(vplcId).pause();
	}

	@Override
	@Transactional
	public void pauseVirtualPlc(String vplcId) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.pauseVirtualPlc(String vplcId)...");
		updateVirtualPlcStatus(vplcId, null, PlcStatus.PAUSED);
		this.pauseVirtualProgrammableController(vplcId);
	}

	@Override
	@Transactional
	public void pauseVirtualPlcByName(String vplcName) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.pauseVirtualPlcByName(String vplcName)...");
		VirtualPlcInfo vplcInfo=updateVirtualPlcStatus(null, vplcName, PlcStatus.PAUSED);
		this.pauseVirtualProgrammableController(vplcInfo.getId());
	}
	
	private void resumeVirtualProgrammableController(String vplcId) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.resumeVirtualProgrammableController(String vplcId)...");
		vplcManager.getVirtualProgrammableController(vplcId).resume();
	}

	@Override
	@Transactional
	public void resumeVirtualPlc(String vplcId) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.resumeVirtualPlc(String vplcId)...");
		updateVirtualPlcStatus(vplcId, null, PlcStatus.RUNNING);
		this.resumeVirtualProgrammableController(vplcId);
	}

	@Override
	@Transactional
	public void resumeVirtualPlcByName(String vplcName) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.resumeVirtualPlcByName(String vplcName)...");
		VirtualPlcInfo vplcInfo=updateVirtualPlcStatus(null, vplcName, PlcStatus.RUNNING);
		this.resumeVirtualProgrammableController(vplcInfo.getId());
	}
	
	public void releaseVirtualPlc(String plcId) throws VirtualPlcException {
		log.debug("");
		vplcManager.releaseVirtualProgrammableController(plcId);
	}
	
	@Transactional
	private VirtualPlcInfo updateVirtualPlcStatus(String vplcId, String vplcName, PlcStatus status) throws VirtualPlcException {
		log.debug("VirtualPlcManipulationServiceImpl.updateVirtualPlc(String vplcId, String vplcName, PlcStatus status)...");
		VirtualPlcInfo vplcInfo=null;
		try {
			if(vplcId!=null) {
				vplcInfo=vplcManagementService.findVirtualPlc(vplcId);
			} else if(vplcName!=null) {
				vplcInfo=vplcManagementService.findVirtualPlcByName(vplcName);
			} else {
				throw new VirtualPlcException();
			}

			vplcInfo.setStatus(status);
			vplcManagementService.updateVirtualPlcStatus(vplcId, vplcName, status, true);
			
			return vplcInfo;
		} catch(NoSuchElementException e) {
			throw new VirtualPlcException("", e);
		}
	}

	@Override
	public MemoryInfo read(String vplcId, String memoryType, String frameFormat, int startAddress, int wordCount) throws VirtualPlcException {
		log.debug("");
		MemoryType memType=MemoryType.toMemoryType(memoryType);
		byte[] bytes=vplcManager.getVirtualProgrammableController(vplcId).read(startAddress, wordCount, memType);
		MemoryInfo mInfo=new MemoryInfo();
		mInfo.setVplcId(vplcId);
		mInfo.setFrameFormat(FrameFormat.valueOf(frameFormat));
		mInfo.setStartAddress(startAddress);
		mInfo.setMemoryType(memType);
		mInfo.setCapacity(200000);
		mInfo.setWords(bytes);

		return mInfo;
	}

	@Override
	public MemoryInfo readByVplcName(String vplcName, String memoryType, String frameFormat, int startAddress, int wordCount) throws VirtualPlcException {
		return null;
	}

	@Override
	public MemoryInfo write(String vplcId, String memoryType, String frameFormat, int startAddress, int wordCount, String data) throws VirtualPlcException {
		log.debug("");
		MemoryType memType=MemoryType.toMemoryType(memoryType);
		VirtualProgrammableController vplc=vplcManager.getVirtualProgrammableController(vplcId);
		vplc.write(startAddress, wordCount, data.getBytes(), memType);
		byte[] bytes=vplc.read(startAddress, 20, memType);
		MemoryInfo mInfo=new MemoryInfo();
		mInfo.setVplcId(vplcId);
		mInfo.setFrameFormat(FrameFormat.valueOf(frameFormat));
		mInfo.setStartAddress(startAddress);
		mInfo.setMemoryType(memType);
		mInfo.setCapacity(200000);
		mInfo.setWords(bytes);
		
		return mInfo;
	}

	@Override
	public MemoryInfo writeByVplcName(String vplcName, String memoryType, String frameFormat, int startAddress, int wordCount, String data) throws VirtualPlcException {
		return null;
	}

	@Override
	public MemoryInfo clearAll(String vplcId, String memoryType, String frameFormat, int currentPosition) throws VirtualPlcException {
		log.debug("");
		MemoryType memType=MemoryType.toMemoryType(memoryType);
		VirtualProgrammableController vplc=vplcManager.getVirtualProgrammableController(vplcId);
		vplc.clear(memType);
		byte[] bytes=vplc.read(currentPosition, 20, memType);
		MemoryInfo mInfo=new MemoryInfo();
		mInfo.setVplcId(vplcId);
		mInfo.setFrameFormat(FrameFormat.valueOf(frameFormat));
		mInfo.setStartAddress(currentPosition);
		mInfo.setMemoryType(memType);
		mInfo.setCapacity(200000);
		mInfo.setWords(bytes);
		
		return mInfo;
	}

	@Override
	public MemoryInfo clearAllByVplcName(String vplcName, String memoryType, String frameFormat, int currentPosition) throws VirtualPlcException {
		return null;
	}
}
