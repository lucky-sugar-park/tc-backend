package com.mymes.equip.vplc.persist;

import java.util.ArrayList;
import java.util.List;

import com.mymes.equip.vplc.AbstractInfo;
import com.mymes.equip.vplc.Condition.RangeInfo;

import lombok.Data;

@Data
public class RangeResponse<T extends AbstractInfo> {

	private long totalRowsCount;
	
	private RangeInfo rangeInfo;
	
	private List<T> rows;
	
	public RangeResponse() {
		rows=new ArrayList<>();
	}
}
