package com.mymes.equip.vplc.service.impl;

import java.util.Date;

import com.mymes.equip.vplc.persist.PersistentEntity;
import com.mymes.equip.vplc.service.ConnectionClientInfo;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Builder
@Entity
@Table(name="CONNECTED_CLIENT")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@Slf4j
public class ConnectionClientEntity extends PersistentEntity<ConnectionClientInfo> {
	
	@EmbeddedId
	private ConnectionClientEntityId connectionClientEntityId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="VPLC_ID", referencedColumnName="VPLC_ID", nullable=false, updatable=false, insertable=false),
		@JoinColumn(name="PORT", referencedColumnName="PORT", nullable=false, updatable=false, insertable=false)
	})
	private ConnectionEntity connectionEntity;
	
	@Column(name="CLIENT_CHANNEL_ID")
	private String clientChannelId;

	@Column(name="CONNED_TIMESTAMP")
	private Date connedTimestamp;
	
	public ConnectionClientEntity () {
		super();
	}

	@Override
	public ConnectionClientInfo info() {
		ConnectionClientInfo info=new ConnectionClientInfo();
		info.setClientIp(this.getConnectionClientEntityId().getClientIp());
		info.setVplcId(this.getConnectionClientEntityId().getVplcId());
		info.setPort(this.getConnectionClientEntityId().getPort());
		info.setClientChannelId(this.getClientChannelId());
		info.setConnTimestamp(this.getConnedTimestamp());
		
		info.setCreatedBy(this.getCreatedBy());
		info.setCreatedDate(this.getCreatedDate());
		info.setUpdatedBy(this.getUpdatedBy());
		info.setUpdatedDate(this.getUpdatedDate());
		
		return info;
	}

	@Override
	public void from(ConnectionClientInfo info) {
		ConnectionClientEntityId connectionClientEntityId=new ConnectionClientEntityId();
		connectionClientEntityId.setVplcId(info.getVplcId());
		connectionClientEntityId.setPort(info.getPort());
		connectionClientEntityId.setClientIp(info.getClientIp());

		this.setConnectionClientEntityId(connectionClientEntityId);
		this.setClientChannelId(info.getClientChannelId());
		this.setConnedTimestamp(info.getConnTimestamp()==null?new Date():info.getConnTimestamp());
		
		super.setCreatedBy(info.getCreatedBy());
		super.setCreatedDate(info.getCreatedDate());
		super.setUpdatedBy(info.getUpdatedBy());
		super.setUpdatedDate(info.getUpdatedDate());
	}

	@Override
	public String toPlainText() {
		log.debug("");
		return new StringBuffer().toString();
	}
}
