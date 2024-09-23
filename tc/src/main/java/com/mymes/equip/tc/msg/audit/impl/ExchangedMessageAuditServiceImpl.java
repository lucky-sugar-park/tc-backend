package com.mymes.equip.tc.msg.audit.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mymes.equip.tc.Condition;
import com.mymes.equip.tc.Condition.PageInfo;
import com.mymes.equip.tc.Condition.RangeInfo;
import com.mymes.equip.tc.Condition.SortDirection;
import com.mymes.equip.tc.msg.audit.ExchangedMessageAuditEvent;
import com.mymes.equip.tc.msg.audit.ExchangedMessageAuditInfo;
import com.mymes.equip.tc.msg.audit.ExchangedMessageAuditService;
import com.mymes.equip.tc.persist.PageResponse;
import com.mymes.equip.tc.persist.PersistentService;
import com.mymes.equip.tc.persist.RangeResponse;
import com.mymes.equip.tc.persist.Rangeable;
import com.mymes.equip.tc.sse.EventPusher;
import com.mymes.equip.tc.sse.PushEventPayload;
import com.mymes.equip.tc.sse.PushEventPayload.PushEventKind;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service("auditMessageServiceBean")
@Slf4j
public class ExchangedMessageAuditServiceImpl implements ExchangedMessageAuditService, PersistentService<ExchangedMessageAuditInfo, ExchangedMessageAuditEntity>{

	@Autowired
	private ExchangedMessageAuditRepository exchangedMessageAuditRepository;
	
	@Autowired
	private ExchangeMessageAuditRangeRepository exchangeMessageAuditRangeRepository;
	
//	@Autowired
//	private SimpleJpaRepository<ExchangedMessageAuditEntity, Long> simpleJpaRepository;

	@Override
	@Transactional
	public void writeAudit(ExchangedMessageAuditInfo auditInfo) {
		log.debug("");
		ExchangedMessageAuditEntity entity=new ExchangedMessageAuditEntity();
		entity.from(auditInfo);
		entity=exchangedMessageAuditRepository.save(entity);
		
		auditInfo.setId(entity.getId());
		PushEventPayload payload=new ExchangedMessageAuditEvent();
		payload.setEventId(UUID.randomUUID().toString());
		payload.setEventKind(PushEventKind.AUDIT_MESSAGE);
		payload.setData(auditInfo);
		payload.setComment("audit message");
		payload.setReconnectTime(3000L);
		new EventPusher().push(payload);
	}

	@Override
	@Transactional
	public void deleteMessageAudit(long id) {
		log.debug("");
		exchangedMessageAuditRepository.deleteById(id);
	}
	
	@Override
	@Transactional
	public void deleteMessageAudits(List<Long> idArr) {
		log.debug("");
		exchangedMessageAuditRepository.deleteAllByIdInBatch(idArr);
	}
	
	@Override
	@Transactional
	public long flashBySavingPeriod(String targetName, Date timestamp) {
		return this.deleteMessageAudits(timestamp);
	}
	
	@Override
	@Transactional
	public long deleteMessageAudits(Date timestamp) {
		log.debug("");
		long count=exchangedMessageAuditRepository.countByTimestamp(timestamp);
		exchangedMessageAuditRepository.deleteByTimestamp(timestamp);
		return count;
	}

	@Override
	public ExchangedMessageAuditInfo findMessageAuditById(long id) {
		log.debug("");
		ExchangedMessageAuditEntity entity=exchangedMessageAuditRepository.findById(id).get();
		return entity.info();
	}
	

	@Override
	public List<ExchangedMessageAuditInfo> searchMessageAudits() {
		log.debug("");
		return this.infos(exchangedMessageAuditRepository.findAll());
	}

	@Override
	public List<ExchangedMessageAuditInfo> searchMessageAudits(PageInfo pageInfo) {
		log.debug("");
		return infos(findToPage(pageInfo).getContent());
	}

	@Override
	public List<ExchangedMessageAuditInfo> searchMessageAudits(Condition cond) {
		log.debug("");
		ExchangedMessageAuditSpecs specs=new ExchangedMessageAuditSpecs();
		List<ExchangedMessageAuditEntity> exchangedMessageAuditEntityList=null;
		Specification<ExchangedMessageAuditEntity> specification=specs.createSpecification(cond);

		if(cond.getPageInfo()==null) {
			exchangedMessageAuditEntityList=exchangedMessageAuditRepository.findAll(specification);
		} else {
			PageInfo pInfo=cond.getPageInfo();
			Page<ExchangedMessageAuditEntity> page=findToPage(specification, pInfo);
			exchangedMessageAuditEntityList=page.getContent();
		}
		return infos(exchangedMessageAuditEntityList);
	}

	@Override
	public List<ExchangedMessageAuditInfo> searchMessageAudits(Condition cond, PageInfo pageInfo) {
		log.debug("");
		cond.setPageInfo(pageInfo);
		return searchMessageAudits(cond);
	}
	
	private Page<ExchangedMessageAuditEntity> findToPage(Specification<ExchangedMessageAuditEntity> specification, PageInfo pageInfo) {
		log.debug("");
		if(pageInfo==null) return null;
		
		Sort.Direction dir=pageInfo.getSortDirection()==SortDirection.ASCENDING?Sort.Direction.ASC:Sort.Direction.DESC;
		PageRequest prequest=PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), dir, pageInfo.getSortBy());
		return exchangedMessageAuditRepository.findAll(specification, prequest);
	}
	
	private Page<ExchangedMessageAuditEntity> findToPage(PageInfo pageInfo) {
		log.debug("");
		Sort.Direction dir=pageInfo.getSortDirection()==SortDirection.ASCENDING?Sort.Direction.ASC:Sort.Direction.DESC;
		PageRequest prequest=PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), dir, pageInfo.getSortBy());
		return exchangedMessageAuditRepository.findAll(prequest);
	}

	@Override
	public PageResponse<ExchangedMessageAuditInfo> searchMessageAuditsToPageResponse(PageInfo pageInfo) {
		log.debug("");
		PageResponse<ExchangedMessageAuditInfo> pageResp=new PageResponse<>();
		Page<ExchangedMessageAuditEntity> page=findToPage(pageInfo);
		pageResp.setPageInfo(pageInfo);
		pageResp.setTotalRowsCount(page.getTotalElements());
		pageResp.setTotalPages(page.getTotalPages());
		pageResp.setRows(infos(page.getContent()));
		
		return pageResp;
	}

	@Override
	public PageResponse<ExchangedMessageAuditInfo> searchMessageAuditsToPageResponse(Condition cond) {
		log.debug("");
		ExchangedMessageAuditSpecs specs=new ExchangedMessageAuditSpecs();
		List<ExchangedMessageAuditEntity> exchangedMessageAuditEntityList=null;
		Specification<ExchangedMessageAuditEntity> specification=specs.createSpecification(cond);

		PageResponse<ExchangedMessageAuditInfo> pageResp=new PageResponse<>();
		if(cond.getPageInfo()==null) {
			exchangedMessageAuditEntityList=exchangedMessageAuditRepository.findAll(specification);
			pageResp.setTotalPages(exchangedMessageAuditEntityList.size());
			pageResp.setPageInfo(null);
		} else {
			PageInfo pInfo=cond.getPageInfo();
			Page<ExchangedMessageAuditEntity> page=findToPage(specification, pInfo);
			exchangedMessageAuditEntityList=page.getContent();
			pageResp.setTotalRowsCount(page.getTotalElements());
			pageResp.setTotalPages(page.getTotalPages());
			pageResp.setPageInfo(pInfo);
		}
		pageResp.setRows(infos(exchangedMessageAuditEntityList));
		return pageResp;
	}

	@Override
	public PageResponse<ExchangedMessageAuditInfo> searchMessageAuditsAToPageResponse(Condition cond, PageInfo pageInfo) {
		log.debug("");
		cond.setPageInfo(pageInfo);
		return searchMessageAuditsToPageResponse(cond);
	}

	@Override
	public RangeResponse<ExchangedMessageAuditInfo> searchMessageAuditsByRange(Condition cond) {
		log.debug("");
		ExchangedMessageAuditSpecs specs=new ExchangedMessageAuditSpecs();
		List<ExchangedMessageAuditEntity> exchangedMessageAuditEntityList=null;
		Specification<ExchangedMessageAuditEntity> specification=specs.createSpecification(cond);
		
		RangeResponse<ExchangedMessageAuditInfo> rangeResp=new RangeResponse<>();
		if(cond.getRangeInfo()==null) {
			exchangedMessageAuditEntityList=exchangedMessageAuditRepository.findAll(specification);
			rangeResp.setTotalRowsCount(exchangedMessageAuditEntityList.size());
			rangeResp.setRangeInfo(null);
		} else {
			RangeInfo rInfo=cond.getRangeInfo();
			Sort.Direction dir=rInfo.getSortDirection()==SortDirection.ASCENDING?Sort.Direction.ASC:Sort.Direction.DESC;
			Rangeable rangeRequest=new Rangeable(rInfo.getStart(), rInfo.getLimit(), Sort.by(dir, rInfo.getSortBy()));
			exchangedMessageAuditEntityList=exchangeMessageAuditRangeRepository.findAllByRange(specification, rangeRequest);
			rangeResp.setTotalRowsCount(exchangedMessageAuditRepository.count(specification));
			rangeResp.setRangeInfo(rInfo);
			rangeResp.setRows(infos(exchangedMessageAuditEntityList));
		}
		return rangeResp;
	}

	@Override
	public RangeResponse<ExchangedMessageAuditInfo> searchMessageAuditsByRange(Condition cond, RangeInfo rangeInfo) {
		log.debug("");
		cond.setRangeInfo(rangeInfo);
		return searchMessageAuditsByRange(cond);
	}
}
