package com.mymes.equip.tc.msg.audit.impl;

import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mymes.equip.tc.msg.audit.ExchangedMessageAuditInfo;
import com.mymes.equip.tc.persist.PersistentEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@Builder
@Entity
@Table(name="EXCHANGED_MESSAGE_AUDIT")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
public class ExchangedMessageAuditEntity extends PersistentEntity<ExchangedMessageAuditInfo> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Long id;

	@Column(name="TRX_ID")
	private String transactionId;

	@Column(name="MSG_ID")
	private String messageId;

	@Column(name="NAME")
	private String name;

	@Column(name="MSG_TYPE")
	private String messageType;

	@Column(name="TIMESTAMP")
	private Date timestamp;

	@Column(name="SENDER")
	private String sender;

	@Column(name="RECEIVER")
	private String receiver;

	@Column(name="MSG_JSON")
	private String jsonString;
	
	@Column(name="DESCRIPTION", length=1024)
	private String description;
	
	public ExchangedMessageAuditEntity() {
		super();
	}

	@Override
	public ExchangedMessageAuditInfo info() {
		ExchangedMessageAuditInfo info=new ExchangedMessageAuditInfo();
		info.setId(this.getId());
		info.setTransactionId(this.getTransactionId());
		info.setMessageId(this.getMessageId());
		info.setName(this.getName());
		info.setMessageType(this.getMessageType());
		info.setTimestamp(this.getTimestamp());
		info.setSender(this.getSender());
		info.setReceiver(this.getReceiver());
		Gson gson=new GsonBuilder().setPrettyPrinting().create();
		info.setDataMap(gson.fromJson(jsonString, new TypeToken<Map<String, String>>(){}.getType()));
		info.setDescription(this.getDescription());
		
		info.setCreatedBy(super.getCreatedBy());
		info.setCreatedDate(super.getCreatedDate());
		info.setUpdatedBy(super.getUpdatedBy());
		info.setUpdatedDate(super.getUpdatedDate());

		return info;
	}

	@Override
	public void from(ExchangedMessageAuditInfo info) {
		this.setId(info.getId());
		this.setTransactionId(info.getTransactionId());
		this.setMessageId(info.getMessageId());
		this.setName(info.getName());
		this.setMessageType(info.getMessageType());
		this.setTimestamp(info.getTimestamp());
		this.setSender(info.getSender());
		this.setReceiver(info.getReceiver());
		Gson gson=new GsonBuilder().setPrettyPrinting().create();
		this.setJsonString(gson.toJson(info.getDataMap()));
		this.setDescription(info.getDescription());
		
		super.setCreatedBy(info.getCreatedBy());
		super.setCreatedDate(info.getCreatedDate());
		super.setUpdatedBy(info.getUpdatedBy());
		super.setUpdatedDate(info.getUpdatedDate());
	}

	@Override
	public String toPlainText() {
		return null;
	}
}
