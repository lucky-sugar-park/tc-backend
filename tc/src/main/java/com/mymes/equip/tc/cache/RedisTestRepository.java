package com.mymes.equip.tc.cache;

import org.springframework.data.repository.CrudRepository;

public interface RedisTestRepository extends CrudRepository<EmailToken, String>{

}
