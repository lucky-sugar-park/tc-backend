package com.mymes.equip.tc.persist;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mymes.equip.tc.AbstractInfo;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

/**
 * @Inheritance는 Super class도 테이블이 있고, 서브 클래스도 따로 클래스가 있을 경우에 사용함
 * 재미 있는 것은 springboot 2.x 버전에서는 문제 없이 구동되었었는데, 3.x에서는 구동실패함
 * 
 * @author insoo67.park
 * @param <T>
 */
@Data
//@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public abstract class PersistentEntity<T extends AbstractInfo> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_DATE", nullable=true)
	@CreatedDate
	@CreationTimestamp
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
