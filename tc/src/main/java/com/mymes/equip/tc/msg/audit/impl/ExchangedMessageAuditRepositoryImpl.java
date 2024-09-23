//package com.mymes.equip.tc.msg.audit.impl;
//
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.TypedQuery;
//
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.data.jpa.repository.support.JpaEntityInformation;
//import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
//import org.springframework.data.repository.NoRepositoryBean;
//
//import com.mymes.equip.tc.persist.Rangeable;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@NoRepositoryBean
//public class ExchangedMessageAuditRepositoryImpl extends SimpleJpaRepository<ExchangedMessageAuditEntity, Long> implements ExchangedMessageAuditRepository {
//
//	public ExchangedMessageAuditRepositoryImpl(JpaEntityInformation<ExchangedMessageAuditEntity, Long> entityInformation, EntityManager entityManager) {
//		super(entityInformation, entityManager);
//	}
//
//	public List<ExchangedMessageAuditEntity> findById(Specification<ExchangedMessageAuditEntity> spec, Rangeable rangeable) {
//		log.debug("");
//        TypedQuery<ExchangedMessageAuditEntity> query = getQuery(spec, getDomainClass(), rangeable.getSort());
//
//        query.setFirstResult(rangeable.getStart());
//        query.setMaxResults(rangeable.getLimit());
//
//        return query.getResultList();
//	}
//}
