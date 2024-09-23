package com.mymes.equip.tc.auth;

import com.mymes.equip.tc.user.UserInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

	private Boolean result;
	
	private UserInfo userInfo;
}
