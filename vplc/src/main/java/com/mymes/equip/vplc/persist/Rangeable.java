package com.mymes.equip.vplc.persist;

import org.springframework.data.domain.Sort;

import lombok.Data;

@Data
public class Rangeable {

	private int start;
	
	private int limit;
	
	private Sort sort;
	
	public Rangeable(int start, int limit, Sort sort) {
		this.start=start;
		this.limit=limit;
		this.sort=sort;
	}
}
