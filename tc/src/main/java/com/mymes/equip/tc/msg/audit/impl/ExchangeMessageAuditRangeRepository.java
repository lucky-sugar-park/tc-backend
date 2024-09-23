package com.mymes.equip.tc.msg.audit.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.mymes.equip.tc.persist.Rangeable;
import com.mymes.equip.tc.persist.RangeableRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ExchangeMessageAuditRangeRepository extends SimpleJpaRepository<ExchangedMessageAuditEntity, Long> implements RangeableRepository<ExchangedMessageAuditEntity, Long> {
	
	
	@Autowired
	public ExchangeMessageAuditRangeRepository(EntityManager entityManager) {
		super(JpaEntityInformationSupport.getEntityInformation(ExchangedMessageAuditEntity.class, entityManager), entityManager);
	}

	@Override
	public List<ExchangedMessageAuditEntity> findAllByRange(Specification<ExchangedMessageAuditEntity> spec,
			Rangeable rangeable) {
		log.debug("");
		TypedQuery<ExchangedMessageAuditEntity> query = getQuery(spec, getDomainClass(), rangeable.getSort());

		query.setFirstResult(rangeable.getStart());
		query.setMaxResults(rangeable.getLimit());

		return query.getResultList();
	}
}
