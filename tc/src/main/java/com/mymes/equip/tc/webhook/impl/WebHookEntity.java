package com.mymes.equip.tc.webhook.impl;

import com.mymes.equip.tc.persist.PersistentEntity;
import com.mymes.equip.tc.webhook.WebHookInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@Builder
@Entity
@Table(name="WEBHOOK")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
public class WebHookEntity extends PersistentEntity<WebHookInfo> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Long id;
	
	@Column(name="NAME", unique=true)
	private String name;
	
	@Column(name="URL")
	private String url;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="SAMPLE_DATA")
	@Lob
	private String sampleData;
	
	@Column(name="DESCRIPTION", length=1024)
	private String description;
	
	public WebHookEntity() {
		super();
	}

	@Override
	public WebHookInfo info() {
		WebHookInfo wInfo=new WebHookInfo();
		wInfo.setId(id);
		wInfo.setName(name);
		wInfo.setUrl(url);
		wInfo.setStatus(status==null?"NONE":status);
		wInfo.setSampleData(sampleData);
		wInfo.setDescription(description);
		wInfo.setCreatedBy(createdBy);
		wInfo.setCreatedDate(createdDate);
		wInfo.setUpdatedBy(updatedBy);
		wInfo.setUpdatedDate(updatedDate);

		return wInfo;
	}

	@Override
	public void from(WebHookInfo info) {
		this.setId(info.getId());
		this.setName(info.getName());
		this.setUrl(info.getUrl());
		this.setStatus(info.getStatus()==null?"NONE":info.getStatus());
		this.setSampleData(info.getSampleData());
		this.setDescription(info.getDescription());
		super.setCreatedBy(info.getCreatedBy());
		super.setCreatedDate(info.getCreatedDate());
		super.setUpdatedBy(info.getUpdatedBy());
		super.setUpdatedDate(info.getUpdatedDate());
	}

	@Override
	public String toPlainText() {
		return new StringBuffer().toString();
	}
}
