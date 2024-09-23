package com.mymes.equip.vplc;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@AllArgsConstructor
public class Condition {

	public enum CondType {
		EQUALS, NOT_EQUALS, GREATER, GREATER_THAN, LESS, LESS_THAN, BETWEEN, IN, NOT, NOT_IN, START_WITH, END_WITH,
		CONTAINS, NONE
	}

	public enum ConjType {
		AND, OR, NONE
	}

	public enum SortDirection {
		ASCENDING, DESCEMDING
	}

	public Condition() {
		conditions = new ArrayList<>();
	}

	private List<CondItem> conditions;

	private PageInfo pageInfo;
	
	private RangeInfo rangeInfo;

	public void addCondition(String name, CondType condType, ConjType conjType, Object value) {
		CondItem item = new CondItem();
		item.setName(name);
		item.setCondType(condType);
		item.setConjType(conjType);
		item.setValue(value);
		conditions.add(item);
	}

	public String toPlainText() {
		StringBuffer sb = new StringBuffer();

		conditions.forEach((cond) -> {
			sb.append("name=" + cond.getName() + ",");
			sb.append("condType=" + cond.getCondType().name() + ",");
			sb.append("conjType=" + cond.getConjType().name() + ",");
			sb.append("value=" + cond.getValue() + "\n");
		});

		return sb.toString();
	}

	@Data
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	static public class CondItem {	
		private String name;
		private CondType condType;
		private ConjType conjType;
		private Object value;
	}

	@Data
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	static public class PageInfo {
		private int page;
		private int size;
		private int sort;
		private SortDirection sortDirection;
		private String[] sortBy;
	}
	
	@Data
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	static public class RangeInfo {
		private int start;
		private int limit;
		private SortDirection sortDirection;
		private String[] sortBy;
	}
}

