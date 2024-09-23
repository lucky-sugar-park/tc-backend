package com.mymes.equip.tc.plc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mymes.equip.tc.AbstractInfo;
import com.mymes.equip.tc.Types.FrameFormat;
import com.mymes.equip.tc.Types.PlcConnectorInfoStatus;
import com.mymes.equip.tc.Types.PlcConnectorOperStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor
public class PlcConnectorInfo extends AbstractInfo {
	
	private long id;

	private String name;

	private String manufacturer;
	
	private String adapterClassName;
	
	private boolean asyncRequestUse;
	
	private long asyncTimerStartDelay;
	
	private long asyncTimerRunInterval;
	
	private boolean published;
	
	/**
	 * REGISTERED | PUBLISHED | RELEASED | UNKNOWN
	 */
	private PlcConnectorInfoStatus status1;
	
	/**
	 * CONNECTION_TEST_OK | CONNECTION_TEST_ERROR | CONNECTION_TEST_NONE | RUNNING | STOPPED | PAUSED | ERROR | UNKNOWN
	 */
	private PlcConnectorOperStatus status2;

	private String hostIp;

	private List<Integer> ports;
	
	private int connLimitPerPort;

	private List<String> connectionVips;

	private FrameFormat messageFrameFormat;

	private Map<String, String> commProtocolHeadersReqTemplate;
	
	private Map<String, String> commProtocolHeadersResTemplate;
	
	private List<String> memTypes;

	private String description;

	public PlcConnectorInfo() {
		this.ports=new ArrayList<>();
		connectionVips=new ArrayList<>();
		commProtocolHeadersReqTemplate=new HashMap<>();
		memTypes=new ArrayList<>();
	}

	public void addPort(int port) {
		ports.add(port);
	}

	public void addVip(String vip) {
		connectionVips.add(vip);
	}

	public void addHeader(String name, String value) {
		commProtocolHeadersReqTemplate.put(name, value);
	}
	
	public String getHeader(String name) {
		return commProtocolHeadersReqTemplate.get(name);
	}
	
	public void addMemType(String memType) {
		memTypes.add(memType);
	}

	@Override
	public String toPlainText() {
		return new StringBuffer().toString();
	}
}
