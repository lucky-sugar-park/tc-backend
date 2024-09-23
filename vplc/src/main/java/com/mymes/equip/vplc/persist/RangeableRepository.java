package com.mymes.equip.vplc.persist;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RangeableRepository <T, ID extends Serializable> {

	public List<T> findAllByRange(Specification<T> spec, Rangeable rangeable);
}
