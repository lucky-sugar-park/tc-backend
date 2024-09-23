package com.mymes.equip.tc.interfs.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HeaderPropRepository extends JpaRepository<HeaderPropEntity, PropId>, JpaSpecificationExecutor<HeaderPropEntity> {

}
