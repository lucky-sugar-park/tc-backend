package com.mymes.equip.vplc.persist;
//package com.mymes.equip.tc.persist;
//
//import java.io.Serializable;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.TypedQuery;
//
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.data.jpa.repository.support.JpaEntityInformation;
//import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
//import org.springframework.stereotype.Repository;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Repository
//public class RangeableRepositoryImpl <T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements RangeableRepository<T, ID> {
//
//	public RangeableRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
//        super(entityInformation, entityManager);
//    }
//	
//	@Override
//    public List<T> findAll(Specification<T> spec, Rangeable rangeable) {
//		log.debug("");
//        TypedQuery<T> query = getQuery(spec, getDomainClass(), rangeable.getSort());
//
//        query.setFirstResult(rangeable.getStart());
//        query.setMaxResults(rangeable.getLimit());
//
//        return query.getResultList();
//    }
//}
