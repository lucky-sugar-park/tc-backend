package com.mymes.equip.vplc.service;

import java.util.Date;

import com.mymes.equip.vplc.AbstractInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString
@Builder
@AllArgsConstructor
@Slf4j
public class ConnectionClientHistoryInfo extends AbstractInfo {

	private long seqNo;
	
	private String clientChannelId;
	
	private String vplcId;

	private int port;
	
	private String clientIp;
	
	private String connOrDisconn;
	
	private Date timestamp;
	
	private String description;
	
	public ConnectionClientHistoryInfo () {
		super();
	}

	@Override
	public String toPlainText() {
		log.debug("");
		return null;
	}
}
