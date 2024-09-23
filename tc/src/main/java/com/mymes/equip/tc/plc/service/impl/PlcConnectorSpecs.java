package com.mymes.equip.tc.plc.service.impl;

import org.springframework.data.jpa.domain.Specification;

import com.mymes.equip.tc.Condition.CondType;
import com.mymes.equip.tc.persist.SearchSpec;

public class PlcConnectorSpecs extends SearchSpec<PlcConnectorEntity>{

	public Specification<PlcConnectorEntity> withName(CondType condType, String name) {
		return super.with("name", condType, name);
	}

	public Specification<PlcConnectorEntity> withManufacturer(CondType condType, String manufacturer) {
		return super.with("manufacturer", condType, manufacturer);
	}

	public Specification<PlcConnectorEntity> withAdapterClassName(CondType condType, String adapterClassName) {
		return super.with("adapterClassName", condType, adapterClassName);
	}

	public Specification<PlcConnectorEntity> withStatus(CondType condType, String status) {
		return super.with("status", condType, status);
	}
	
	public Specification<PlcConnectorEntity> withHostIp(CondType condType, String hostIp) {
		return super.with("hostIp", condType, hostIp);
	}
	
	public Specification<PlcConnectorEntity> withPorts(CondType condType, String ports) {
		return super.with("ports", condType, ports);
	}
	
	public Specification<PlcConnectorEntity> withMessageFrameFormat(CondType condType, String messageFrameFormat) {
		return super.with("messageFrameFormat", condType, messageFrameFormat);
	}
	
	public Specification<PlcConnectorEntity> withCreatedBy(CondType condType, String createdBy) {
		return super.with("createdBy", condType, createdBy);
	}
	
	public Specification<PlcConnectorEntity> withCreatedDate(CondType condType, String createdDate) {
		return super.with("createdDate", condType, createdDate);
	}
	
	public Specification<PlcConnectorEntity> withUpdatedBy(CondType condType, String updatedBy) {
		return super.with("updatedBy", condType, updatedBy);
	}
	
	public Specification<PlcConnectorEntity> withUpdatedDate(CondType condType, String updatedDate) {
		return super.with("updatedDate", condType, updatedDate);
	}
}
