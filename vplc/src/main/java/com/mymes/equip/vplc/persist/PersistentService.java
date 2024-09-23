package com.mymes.equip.vplc.persist;

import java.util.ArrayList;
import java.util.List;

import com.mymes.equip.vplc.AbstractInfo;

public interface PersistentService<T extends AbstractInfo, E extends PersistentEntity<T>> {

	default List<T> infos(List<E> list) {
		List<T> iList=new ArrayList<>();
		for(E entity:list) {
			iList.add(entity.info());
		}
		return iList;
	}
}
