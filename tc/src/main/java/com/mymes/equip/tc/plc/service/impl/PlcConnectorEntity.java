package com.mymes.equip.tc.plc.service.impl;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mymes.equip.tc.Types.FrameFormat;
import com.mymes.equip.tc.Types.PlcConnectorInfoStatus;
import com.mymes.equip.tc.Types.PlcConnectorOperStatus;
import com.mymes.equip.tc.persist.PersistentEntity;
import com.mymes.equip.tc.plc.service.PlcConnectorInfo;

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
import lombok.extern.slf4j.Slf4j;

@ToString
@Builder
@Entity
@Table(name="PLC_CONNECTOR")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@Slf4j
public class PlcConnectorEntity extends PersistentEntity<PlcConnectorInfo> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Long id;

	@Column(name="NAME", unique=true, nullable=false)
	private String name;

	@Column(name="MANUFACTURER")
	private String manufacturer;
	
	@Column(name="ADAPTER_CLASS_NAME")
	private String adapterClassName;
	
	@Column(name="ASYNC_REQ_USE")
	private Boolean asyncRequestUse;
	
	@Column(name="ASYNC_TIMER_START_DELAY")
	private Long asyncTimerStartDelay;
	
	@Column(name="ASYNC_TIMER_RUN_INTERVAL")
	private Long asyncTimerRunInterval;
	
	@Column(name="IS_PUBLISHED")
	private Boolean published;
	
	// PlcInfoStatus
	@Column(name="STATUS_1")
	private String status1;
	
	// PlcOperStatus
	@Column(name="STATUS_2")
	private String status2;

	@Column(name="HOST_IP")
	private String hostIp;

	@Column(name="PORTS")
	private String ports;
	
	@Column(name="CONN_LIMIT")
	private Integer connLimitPerPort;

	@Column(name="CONNECTION_VIPS")
	private String connectionVips;

	@Column(name="FRAME_FORMAT")
	private String messageFrameFormat;
	
	@Column(name="REQ_HEADER_TEMPLATE")
	@Lob
	private String commProtocolHeadersReqTemplate;
	
	@Column(name="RES_HEADER_TEMPLATE")
	@Lob
	private String commProtocolHeadersResTemplate;

	@Column(name="MEM_TYPES")
	private String memTypes;

	@Column(name="DESCRIPTION", length=1024)
	private String description;
	
	public PlcConnectorEntity() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PlcConnectorInfo info() {
		PlcConnectorInfo info=new PlcConnectorInfo();
		info.setId(this.id);
		info.setName(this.name);
		info.setManufacturer(this.manufacturer);
		info.setAdapterClassName(this.adapterClassName);
		info.setAsyncRequestUse(this.asyncRequestUse);
		info.setAsyncTimerStartDelay(this.asyncTimerStartDelay==null?1500:this.asyncTimerStartDelay);
		info.setAsyncTimerRunInterval(this.asyncTimerRunInterval==null?5000:this.asyncTimerRunInterval);
		info.setPublished(this.published!=null?this.published:false);
		info.setStatus1(PlcConnectorInfoStatus.valueOf(this.status1));
		info.setStatus2(PlcConnectorOperStatus.valueOf(this.status2));
		info.setHostIp(this.hostIp);
		
		String portArr[]=this.ports.split(",");
		for(String port: portArr) {
			info.addPort(Integer.parseInt(port.trim()));
		}
		
		info.setConnLimitPerPort(this.getConnLimitPerPort()!=null?this.getConnLimitPerPort():-1);
		
		String vipsArr[]=this.connectionVips.split(",");
		for(String vip: vipsArr) {
			info.addVip(vip.trim());
		}
		
		info.setMessageFrameFormat(FrameFormat.toFrameFormat(messageFrameFormat));
		ObjectMapper om=new ObjectMapper();
		Map<String, String> reqHeaderTemplate=null;
		Map<String, String> resHeaderTemplate=null;
		try {
			reqHeaderTemplate = om.readValue(this.commProtocolHeadersReqTemplate, Map.class);
			resHeaderTemplate = om.readValue(this.commProtocolHeadersResTemplate, Map.class);
		} catch (JsonProcessingException e) {
			log.warn("");
		}
		info.setCommProtocolHeadersReqTemplate(reqHeaderTemplate);
		info.setCommProtocolHeadersResTemplate(resHeaderTemplate);
		
		String memTypeArr[]=this.memTypes.split(",");
		for(String memType:memTypeArr) {
			info.addMemType(memType.trim());
		}
		
		info.setDescription(this.description);
		info.setCreatedBy(super.getCreatedBy());
		info.setCreatedDate(super.getCreatedDate());
		info.setUpdatedBy(super.getUpdatedBy());
		info.setUpdatedDate(super.getUpdatedDate());
		return info;
	}

	@Override
	public void from(PlcConnectorInfo info) {
		this.setId(info.getId());
		if(info.getName()!=null && !"".equals(info.getName())) {
			this.setName(info.getName());
		}
		if(info.getManufacturer()!=null && !"".equals(info.getManufacturer())) {
			this.setManufacturer(info.getManufacturer());
		}
		if(info.getAdapterClassName()!=null && !"".equals(info.getAdapterClassName())) {
			this.setAdapterClassName(info.getAdapterClassName());
		}
		this.setAsyncRequestUse(info.isAsyncRequestUse());
		this.setAsyncTimerStartDelay(info.getAsyncTimerStartDelay()<=0?500:info.getAsyncTimerStartDelay());
		this.setAsyncTimerRunInterval(info.getAsyncTimerRunInterval()<=0?500:info.getAsyncTimerRunInterval());
		
		this.setPublished(info.isPublished());
		if(info.getStatus1()!=null && !"".equals(info.getStatus1().name())) {
			this.setStatus1(info.getStatus1().name());
		}
		if(info.getStatus2()!=null && !"".equals(info.getStatus2().name())) {
			this.setStatus2(info.getStatus2().name());
		}
		if(info.getHostIp()!=null && !"".equals(info.getHostIp())) {
			this.setHostIp(info.getHostIp());
		}

		StringBuilder sb=new StringBuilder();
		if(info.getPorts().size()>0) {
			for(int port:info.getPorts()) {
				sb.append(port+",");
			}
			String portStr=sb.toString();
			this.setPorts(portStr.substring(0,portStr.length()-1));
		}
		
		if(info.getConnLimitPerPort()>0) {
			this.setConnLimitPerPort(info.getConnLimitPerPort()>0?info.getConnLimitPerPort():-1);
		}
		
		if(info.getConnectionVips().size()>0) {
			sb=new StringBuilder();
			for(String vip:info.getConnectionVips()) {
				sb.append(vip+",");
			}
			String vipStr=sb.toString();
			this.setConnectionVips(vipStr.substring(0, vipStr.length()-1));
		}
		
		if(info.getMessageFrameFormat()!=null) { 
			this.setMessageFrameFormat(info.getMessageFrameFormat().getName());
		}
		
		ObjectMapper om=new ObjectMapper();
		if(info.getCommProtocolHeadersReqTemplate()!=null && info.getCommProtocolHeadersReqTemplate().size()>0) {
			try {
				this.setCommProtocolHeadersReqTemplate(om.writeValueAsString(info.getCommProtocolHeadersReqTemplate()));
			} catch (JsonProcessingException e) {
				log.warn("");
			}
		}
		
		if(info.getCommProtocolHeadersResTemplate()!=null && info.getCommProtocolHeadersResTemplate().size()>0) {
			try {
				this.setCommProtocolHeadersResTemplate(om.writeValueAsString(info.getCommProtocolHeadersResTemplate()));
			} catch (JsonProcessingException e) {
				log.warn("");
			}
		}
		
		if(info.getMemTypes()!=null && info.getMemTypes().size()>0) {
			sb=new StringBuilder();
			for(String memType:info.getMemTypes()) {
				sb.append(memType+",");
			}
			String memTypeArr=sb.toString();
			this.setMemTypes(memTypeArr.substring(0, memTypeArr.length()-1));
		}
		
		if(info.getDescription()!=null && !"".equals(info.getDescription())) {
			this.setDescription(info.getDescription());
		}
		this.setCreatedBy(info.getCreatedBy());
		this.setCreatedDate(info.getCreatedDate());
		this.setUpdatedBy(info.getUpdatedBy());
		this.setUpdatedDate(info.getUpdatedDate());
	}

	@Override
	public String toPlainText() {
		return null;
	}
}
