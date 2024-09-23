package com.mymes.equip.vplc.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionClientHistoryRepository extends JpaRepository<ConnectionClientHistoryEntity, Long>, JpaSpecificationExecutor<ConnectionClientHistoryEntity>{

}
