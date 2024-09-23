package com.mymes.equip.tc.auth.impl;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwTokenInfo {
	
	private String useId;

	private String accessToken;
	
	private String refreshToken;
	
	private Date refreshTokenExpiredTime;
}
