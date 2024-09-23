package com.mymes.equip.tc.persist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mymes.equip.tc.AbstractInfo;

import ch.qos.logback.core.joran.conditional.Condition;

public interface PersistentService<T extends AbstractInfo, E extends PersistentEntity<T>> {

	default List<T> infos(List<E> list) {
		List<T> iList=new ArrayList<>();
		for(E entity:list) {
			iList.add(entity.info());
		}
		return iList;
	}
	
	public default long flashBySavingPeriod(String targetName, Date timestamp) {
		return -1;
	}
	
	public default long flashBySingleConstantTimestamp(String targetName, Date timestamp) {
		return -1;
	}
	
	public default long flashByRangeConstantTimestamp(String targetName, Date from, Date to) {
		return -1;
	}
	
	public default long flashByConstantStrings(String targetName, List<String> values) {
		return -1;
	}
	
	public default long flashByConstantNumbers(String targetName, List<Long> values) {
		return -1;
	}
	
	public default long flashByCondition(String targetName, Condition condition) {
		return -1;
	}
}
