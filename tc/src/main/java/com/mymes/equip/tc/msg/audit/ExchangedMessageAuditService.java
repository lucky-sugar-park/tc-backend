package com.mymes.equip.tc.msg.audit;

import java.util.Date;
import java.util.List;

import com.mymes.equip.tc.Condition;
import com.mymes.equip.tc.Condition.PageInfo;
import com.mymes.equip.tc.Condition.RangeInfo;
import com.mymes.equip.tc.persist.PageResponse;
import com.mymes.equip.tc.persist.RangeResponse;

public interface ExchangedMessageAuditService {

	public abstract void writeAudit(ExchangedMessageAuditInfo auditInfo);

	public abstract void deleteMessageAudit(long id);
	
	public abstract void deleteMessageAudits(List<Long> idArr);
	
	public abstract long deleteMessageAudits(Date timestamp);

	public abstract ExchangedMessageAuditInfo findMessageAuditById(long id);
	
	public abstract List<ExchangedMessageAuditInfo> searchMessageAudits();
	
	public abstract List<ExchangedMessageAuditInfo> searchMessageAudits(PageInfo pageInfo);
	
	public abstract PageResponse<ExchangedMessageAuditInfo> searchMessageAuditsToPageResponse(PageInfo pageInfo);

	public abstract List<ExchangedMessageAuditInfo> searchMessageAudits(Condition cond);
	
	public abstract PageResponse<ExchangedMessageAuditInfo> searchMessageAuditsToPageResponse(Condition cond);

	public abstract List<ExchangedMessageAuditInfo> searchMessageAudits(Condition cond, PageInfo pageInfo);
	
	public abstract PageResponse<ExchangedMessageAuditInfo> searchMessageAuditsAToPageResponse(Condition cond, PageInfo pageInfo);
	
	public abstract RangeResponse<ExchangedMessageAuditInfo> searchMessageAuditsByRange(Condition cond);
	
	public abstract RangeResponse<ExchangedMessageAuditInfo> searchMessageAuditsByRange(Condition cond, RangeInfo rangeInfo);
}
