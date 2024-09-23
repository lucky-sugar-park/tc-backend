package com.mymes.equip.tc.msg.audit;

import com.mymes.equip.tc.sse.PushEventPayload;

public class ExchangedMessageAuditEvent extends PushEventPayload {

	public ExchangedMessageAuditEvent() {
	}

	@Override
	public String toJson() {
		ExchangedMessageAuditInfo info=(ExchangedMessageAuditInfo) super.getData();
		return info.toJson(false);
	}
}
