package com.mymes.equip.tc.auth.impl;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface JwRefreshTokenRepository extends CrudRepository<JwRefreshTokenEntity, String>{
	
	public Optional<JwRefreshTokenEntity> findByUserId();

}