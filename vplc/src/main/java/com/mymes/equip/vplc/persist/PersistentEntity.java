package com.mymes.equip.vplc.persist;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mymes.equip.vplc.AbstractInfo;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
//@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public abstract class PersistentEntity<T extends AbstractInfo> {

	@CreationTimestamp
	@Column(name="CREATED_DATE", nullable=true)
	@CreatedDate
	protected Date createdDate;

	@UpdateTimestamp
	@Column(name="UPDATED_DATE", nullable=true)
	protected Date updatedDate;

	@Column(name="CREATED_BY", nullable=true)
	protected String createdBy;

	@Column(name="UPDATED_BY", nullable=true)
	protected String updatedBy;

	public String toJson(boolean pretty) {
		Gson gson=pretty?new GsonBuilder().setPrettyPrinting().create():new GsonBuilder().create();
		return gson.toJson(this);
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy=createdBy!=null?createdBy:"SYSTEM";
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate=createdDate!=null?createdDate:new Date();
	}
	
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy=updatedBy!=null?updatedBy:"SYSTEM";
	}
	
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate=updatedDate!=null?updatedDate:new Date();
	}

	public abstract T info();

	public abstract void from(T info);

	public abstract String toPlainText();
}

