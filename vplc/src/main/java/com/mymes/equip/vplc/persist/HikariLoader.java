package com.mymes.equip.vplc.persist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;

@Component
public class HikariLoader implements ApplicationRunner {

	private final HikariDataSource hikariDataSource;
	
	public HikariLoader(HikariDataSource hikariDataSource) {
		this.hikariDataSource=hikariDataSource;
	}

	@Override
	@Autowired
	public void run(ApplicationArguments args) throws Exception {
		hikariDataSource.getConnection();
	}
}

