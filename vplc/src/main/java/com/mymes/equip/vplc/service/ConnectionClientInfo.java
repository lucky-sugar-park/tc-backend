package com.mymes.equip.vplc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class ConnectionClientInfo extends AbstractInfo {
	
	private String connOrDisconn;

	private String clientIp;
	
	private String vplcId;

	private int port;

	private String clientChannelId;
	
	private Date connTimestamp;
	
	private List<ConnectionClientHistoryInfo> connectionClientHistoryList;
	
	public ConnectionClientInfo() {
		super();
		connectionClientHistoryList=new ArrayList<>();
	}
	
	public void addConnectionClientHistory(ConnectionClientHistoryInfo hist) {
		this.connectionClientHistoryList.add(hist);
	}

	@Override
	public String toPlainText() {
		log.debug("");
		return null;
	}
}
