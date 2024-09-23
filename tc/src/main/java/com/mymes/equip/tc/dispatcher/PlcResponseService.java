package com.mymes.equip.tc.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PlcResponseService {
	
	@Autowired
	private PlcResponseRepository plcResponseRepository;

	@CachePut(cacheNames="plcResponse", key="#id")
	public void put(String id, PlcResponseEntity plcResponseEntity) {
//		plcResponseRepository.save(plcResponseEntity);
	}
	
	@Cacheable(cacheNames="plcResponse", key="#id")
	public PlcResponseEntity get(String id) {
//		return plcResponseRepository.findById(id).orElse(null);
		return null;
	}
}
