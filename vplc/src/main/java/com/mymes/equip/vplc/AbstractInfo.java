package com.mymes.equip.vplc;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Data;

@Data
public abstract class AbstractInfo {

	protected static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ssS");

	private String createdBy;
	
	private Date createdDate;
	
	private String updatedBy;
	
	private Date updatedDate;
	
	public abstract String toPlainText();
	
	public String toJson(boolean pretty) {
		Gson gson=pretty?new GsonBuilder().setPrettyPrinting().create():new GsonBuilder().create();
		return gson.toJson(this);
	}
}
