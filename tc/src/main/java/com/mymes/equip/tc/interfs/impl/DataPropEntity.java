package com.mymes.equip.tc.interfs.impl;

import com.mymes.equip.tc.ToolControlException;
import com.mymes.equip.tc.Types.PropType;
import com.mymes.equip.tc.interfs.PropInfo;
import com.mymes.equip.tc.persist.PersistentEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
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
@Table(name="DATA_PROP")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@Slf4j
public class DataPropEntity extends PersistentEntity<PropInfo> {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PropId propId;
	
	@Column(name="ORDER_SEQ")
	private Integer order;
	
	@ManyToOne(fetch=FetchType.LAZY)
//	@MapsId("interfaceId")
	@JoinColumn(name="INTERFACE_ID", referencedColumnName="ID", nullable=false, updatable=false, insertable=false)
	private InterfaceEntity interfs;
	
	@Column(name="TYPE")
	private String type;
	
	@Column(name="VALUE")
	private String value;
	
	@Column(name="LENGTH")
	private Integer length;
	
	@Column(name="DESCRIPTION", length=1024)
	private String description;
	
	public DataPropEntity() {
		super();
	}
	
	@Override
	public PropInfo info() {
		PropInfo pInfo=new PropInfo();
		pInfo.setName(propId.getName());
		pInfo.setOrder(order!=null?order:0);
		pInfo.setInterfaceId(propId.getInterfaceId());
		pInfo.setType(PropType.valueOf(type));
		try {
			pInfo.setValue(value);
		} catch (ToolControlException e) {
			e.printStackTrace();
		}
		pInfo.setLength(length);
		pInfo.setDescription(description);
		pInfo.setCreatedBy(createdBy);
		pInfo.setCreatedDate(createdDate);
		pInfo.setUpdatedBy(updatedBy);
		pInfo.setUpdatedDate(updatedDate);

		return pInfo;
	}

	@Override
	public void from(PropInfo info) {
		this.propId=new PropId();
		this.propId.setInterfaceId(info.getInterfaceId());
		this.propId.setName(info.getName());
		
		this.setOrder(info.getOrder());
		this.setType(info.getType().name());
		this.setValue(info.getValueAsString());
		this.setLength(info.getLength());
		this.setDescription(info.getDescription());
		this.setCreatedBy(info.getCreatedBy());
		this.setCreatedDate(info.getCreatedDate());
		this.setUpdatedBy(info.getUpdatedBy());
		this.setUpdatedDate(info.getUpdatedDate());
	}

	@Override
	public String toPlainText() {
		log.debug("");
		return null;
	}
}
