package com.mymes.equip.tc.plc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mymes.equip.tc.Condition;
import com.mymes.equip.tc.Condition.PageInfo;
import com.mymes.equip.tc.Condition.SortDirection;
import com.mymes.equip.tc.persist.PageResponse;
import com.mymes.equip.tc.persist.PersistentService;
import com.mymes.equip.tc.plc.service.PlcConnectionEventHistInfo;
import com.mymes.equip.tc.plc.service.PlcConnectionEventHistoryService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service("plcConnectionEventHistoryServiceBean")
@Slf4j
public class PlcConnectionEventHistoryServiceImpl 
	implements PlcConnectionEventHistoryService, 
			   PersistentService<PlcConnectionEventHistInfo, PlcConnectionEventHistEntity> {
	
	@Autowired
	private PlcConnectionEventHistRepository plcConnectionEventHistRepository;

	@Override
	public void savePlcConnectionEventHistory(PlcConnectionEventHistInfo pcehInfo) {
		log.debug("");
		PlcConnectionEventHistEntity entity=new PlcConnectionEventHistEntity();
		entity.from(pcehInfo);
		plcConnectionEventHistRepository.save(entity);
	}

	@Override
	public PageResponse<PlcConnectionEventHistInfo> searchPlcConnectionEventHist(Condition cond) {
		log.debug("");
		PlcConnectionEventHistSpecs specs=new PlcConnectionEventHistSpecs();
		List<PlcConnectionEventHistEntity> plcConnectionEventHistEntityList=null;
		Specification<PlcConnectionEventHistEntity> specification=specs.createSpecification(cond);
		
		long count=plcConnectionEventHistRepository.count(specification);
		if(cond.getPageInfo()==null) {
			plcConnectionEventHistEntityList=plcConnectionEventHistRepository.findAll(specification);
		} else {
			PageInfo pInfo=cond.getPageInfo();
			Sort.Direction dir=pInfo.getSortDirection()==SortDirection.ASCENDING?Sort.Direction.ASC:Sort.Direction.DESC;
			PageRequest prequest=PageRequest.of(pInfo.getPage(), pInfo.getSize(), dir, pInfo.getSortBy());
			plcConnectionEventHistEntityList=plcConnectionEventHistRepository.findAll(specification, prequest).getContent();
		}
		
		List<PlcConnectionEventHistInfo> infoList=new ArrayList<>();
		plcConnectionEventHistEntityList.forEach(entity->{
			infoList.add(entity.info());
		});
		PageResponse<PlcConnectionEventHistInfo> response = new PageResponse<>();
		response.setPageInfo(cond.getPageInfo());
		response.setTotalRowsCount(count);
		response.setRows(infoList);
		return response;
	}

	@Override
	public long deleteByTimestamp(Date timestamp) {
		log.debug("");
		long delCount=plcConnectionEventHistRepository.countByTimestamp(timestamp);
		plcConnectionEventHistRepository.deleteByTimestamp(timestamp);
		return delCount;
	}
	
	@Override
	@Transactional
	public long flashBySavingPeriod(String targetName, Date timestamp) {
		return this.deleteByTimestamp(timestamp);
	}
}
