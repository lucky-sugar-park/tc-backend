package com.mymes.equip.tc.persist;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.mymes.equip.tc.Condition;
import com.mymes.equip.tc.Condition.CondItem;
import com.mymes.equip.tc.Condition.CondType;
import com.mymes.equip.tc.util.MethodUtils;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
@Slf4j
public class SearchSpec<T extends PersistentEntity> {

	protected Specification<T> with(String propName, CondType condType, Object...values) {
		log.debug("");
		return (root, query, builder) -> {
			switch(condType) {
			case EQUALS:
				return builder.equal(root.get(propName), values[0]);
			case NOT_EQUALS:
				return builder.notEqual(root.get(propName), values[0]);
			case GREATER:
				return builder.ge(root.get(propName), (Number) values[0]);
			case GREATER_THAN:
				return builder.gt(root.get(propName), (Number) values[0]);
			case LESS:
				return builder.le(root.get(propName), (Number) values[0]);
			case LESS_THAN:
				return builder.lt(root.get(propName), (Number) values[0]);
			case BETWEEN:
				return builder.between(root.get(propName), (Date) values[0], (Date) values[1]);	
			case NOT:
				Predicate predicate = builder.equal(root.get(propName), values[0]);
				return builder.not(predicate);
			case IN:
				In<String> in=builder.in(root.get(propName));
				@SuppressWarnings("unchecked") List<String> inList=(List<String>)(values[0]);
				for(String str : inList) in.value(str);
				return in;
			case NOT_IN:
				@SuppressWarnings("unchecked") List<String> ins=(List<String>)(values[0]);
				return builder.not(root.get(propName).in(ins));
			case START_WITH:
				return builder.like(root.get(propName), values[0]+"%");
			case END_WITH:
				return builder.like(root.get(propName), "%"+values[0]);
			case CONTAINS:
				return builder.like(root.get(propName), "%"+values[0]+"%");
			default: 
				return null;
			}
		};
	}
	
	@SuppressWarnings({ "unchecked" })
	public Specification<T> createSpecification(Condition cond) {
		log.debug("SearchSpec.createSpecification(SearchCondition cond) ...");

		Specification<T> spec=null;
		for(CondItem con : cond.getConditions()) {
			String pname=con.getName();
			if(con.getCondType()==CondType.IN || con.getCondType()==CondType.NOT_IN) pname+="s";;
			Method method=null;
			method=MethodUtils.findMethod(this.getClass(), "with", pname);
			if(method==null) continue;
			try {
				if(spec==null) {
					spec=(Specification) method.invoke(this, con.getCondType(), con.getValue());
					continue;
				}
			
				switch(con.getConjType()) {
				case AND:
					spec=spec.and((Specification)method.invoke(this, con.getCondType(), con.getValue()));
					break;
				case OR:
					spec=spec.or((Specification)method.invoke(this, con.getCondType(), con.getValue()));
					break;
				default:
					break;
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return spec;
	}
}
