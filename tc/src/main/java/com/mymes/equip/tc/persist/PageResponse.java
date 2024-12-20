package com.mymes.equip.tc.persist;

import java.util.ArrayList;
import java.util.List;

import com.mymes.equip.tc.AbstractInfo;
import com.mymes.equip.tc.Condition.PageInfo;

import lombok.Data;

@Data
public class PageResponse<T extends AbstractInfo> {
	
	private long totalRowsCount;
	
	private int totalPages;
	
	private PageInfo pageInfo;

	private List<T> rows;
	
	public PageResponse() {
		rows=new ArrayList<>();
	}
}
