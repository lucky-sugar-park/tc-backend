package com.mymes.equip.tc.webhook.impl;

import org.springframework.data.jpa.domain.Specification;

import com.mymes.equip.tc.Condition.CondType;
import com.mymes.equip.tc.persist.SearchSpec;

public class WebHookSpecs extends SearchSpec<WebHookEntity> {

	public Specification<WebHookEntity> withName(CondType condType, String name) {
		return super.with("name", condType, name);
	}
	
	public Specification<WebHookEntity> withUrl(CondType condType, String url) {
		return super.with("url", condType, url);
	}

	public Specification<WebHookEntity> withDescription(CondType condType, String description) {
		return super.with("description", condType, description);
	}
}
