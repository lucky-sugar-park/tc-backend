package org.sample.server;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Query {

	private List<Qunit> queries;
	
	public Query () {
		queries=new ArrayList<>();
	}
	
	public void add(Qunit qunit) {
		queries.add(qunit);
	}
	
	@Data
	public class Qunit {
		
		private String name;
		
		private String value;
		
		private String operator;
	}
}
