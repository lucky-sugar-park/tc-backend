package com.mymes.equip.vplc.service.impl;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ConnectionEntityId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name="VPLC_ID")
	private String vplcId;
	
	@Column(name="PORT")
	private Integer port;
}
