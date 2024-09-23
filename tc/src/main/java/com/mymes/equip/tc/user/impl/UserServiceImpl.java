package com.mymes.equip.tc.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mymes.equip.tc.persist.PersistentService;
import com.mymes.equip.tc.user.UserInfo;
import com.mymes.equip.tc.user.UserService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService, PersistentService<UserInfo, UserEntity>, UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.findUserById(username);
	}
	
	@Override
	@Transactional
	public UserInfo findUserById(String id) throws UsernameNotFoundException {
		log.debug("");
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("No existing user")).info();
	}

	@Override
	@Transactional
	public UserInfo findUserByEmail(String email) throws UsernameNotFoundException {
		log.debug("");
		UserEntity user=userRepository.findByEmail(email).get();
		if(user==null) {
			throw new UsernameNotFoundException(email + " user not found");
		}
		return user.info();
	}

	@Override
	public void updateUser(UserInfo uInfo) {
		log.debug("");
	}
}
