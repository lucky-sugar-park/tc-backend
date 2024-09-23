package com.mymes.equip.vplc.service.impl;

import com.mymes.equip.vplc.persist.PersistentEntity;
import com.mymes.equip.vplc.service.VirtualPlcHistoryInfo;

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
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Builder
@Entity
@Table(name="VPLC_HISTORY")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class VirtualPlcHistoryEntity extends PersistentEntity<VirtualPlcHistoryInfo> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SEQ_NO")
	private long seqNo;

	@Column(name="PLC_ID")
	private String plcId;

	@Column(name="PLC_NAME")
	private String plcName;

	@Column(name="STATUS")
	private String status;

	@Column(name="HIST_CONTENTS")
	@Lob
	private String historyContents;

	@Override
	public VirtualPlcHistoryInfo info() {
		VirtualPlcHistoryInfo info=new VirtualPlcHistoryInfo();
		info.setSeqNo(this.getSeqNo());
		info.setPlcId(this.getPlcId());
		info.setPlcName(this.getPlcName());
		info.setStatus(this.getStatus());
		info.setHistoryContents(this.getHistoryContents());

		return info;
	}

	@Override
	public void from(VirtualPlcHistoryInfo info) {
		this.setSeqNo(info.getSeqNo());
		this.setPlcId(info.getPlcId());
		this.setPlcName(info.getPlcName());
		this.setStatus(info.getStatus());
		this.setHistoryContents(info.getHistoryContents());
	}

	@Override
	public String toPlainText() {
		log.debug("");

		StringBuffer sb=new StringBuffer();
		sb.append("seqNo: " + this.getSeqNo() + "\n");
		sb.append("plcId: " + this.getPlcId() + "\n");
		sb.append("plcName: " + this.getPlcName() + "\n");
		sb.append("status: " + this.getStatus() + "\n");
		sb.append("historyContents: " + this.getHistoryContents() + "\n");

		return sb.toString();
	}
}
