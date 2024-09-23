package com.mymes.equip.tc.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

	public UserInfo findUserById(String id) throws UsernameNotFoundException; 
	
	public UserInfo findUserByEmail(String email) throws UsernameNotFoundException;
	
	public void updateUser(UserInfo uInfo);
}
