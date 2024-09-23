package com.mymes.equip.tc.auth;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import com.mymes.equip.tc.auth.impl.JwTokenInfo;
import com.mymes.equip.tc.user.UserInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SecurityUser extends User {

	private static final long serialVersionUID = 1L;
	
	private JwTokenInfo jwTokenInfo;
	
	private UserInfo userInfo;

	public SecurityUser(UserInfo userInfo) {
		super(
			userInfo.getId(), 
			"{noop}"+userInfo.getPassword(),
			AuthorityUtils.createAuthorityList(userInfo.getUserRoles().get(0).toString())
		);
		this.userInfo=userInfo;
	}
}
