package com.mymes.equip.tc.interfs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mymes.equip.tc.AbstractInfo;
import com.mymes.equip.tc.ToolControlException;
import com.mymes.equip.tc.Types.PropType;
import com.mymes.equip.tc.util.ByteUtil;
import com.mymes.equip.tc.util.ByteUtil.Endian;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropInfo extends AbstractInfo {

	private long interfaceId;
	
	private String oper;

	private String name;
	
	private int order;

	private PropType type;

	@JsonIgnore
	private Object value;
	
	private String strValue;

	/**
	 * length for byte array
	 */
	private int length;

	private String description;

	public PropInfo() {
		super();
	}

	public void setValue(String value) throws ToolControlException {
		if(value==null || "".equals(value)) return;

		switch(type) {
		case BOOLEAN:
			this.value=Boolean.parseBoolean(value);
			break;
		case INT:
			this.value=Integer.parseInt(value);
			break;
		case SHORT:
			this.value=Short.parseShort(value);
			break;
		case LONG:
			this.value=Long.parseLong(value);
			break;
		case DOUBLE:
			this.value=Double.parseDouble(value);
			break;
		case BYTE:
			byte bytes[]=ByteUtil.hexToByteArray(value);
//			if(this.length!=bytes.length) {
//				throw new RuntimeException();
//			}
			this.value=bytes;
			break;
		case STRING:
		default:
			if(this.length!=value.length()) {
				throw new RuntimeException();
			}
			this.value=value;
			break;
		}
		this.strValue=value;
	}

	public Object getValue() {
		return this.value;
//		return getValueAsString();
	}

	@JsonIgnore
	public String getValueAsString() {
		if(value==null) return "";
		
		switch(type) {
		case BOOLEAN:
			return ((Boolean)value).toString();
		case INT:
			return ((Integer)value).toString();
		case SHORT:
			return ((Short)value).toString();
		case LONG:
			return ((Long)value).toString();
		case DOUBLE:
			return ((Double)value).toString();
		case BYTE:
			StringBuffer sb=new StringBuffer();
			byte[] bvalue=(byte[])value;
			for(int i=0;i<bvalue.length;i++) {
				sb.append(Integer.toHexString(bvalue[0]&0xFF).toUpperCase());
			}
			return sb.toString();
		case STRING:
		default:
			return (String)value;
		}
	}
	
	public byte[] extractRealDataAsBytes(Object realData, Endian endian) {
		switch(type) {
		case BOOLEAN:
			boolean vb=(Boolean)realData;
			return vb?new byte[] { 0x01 }: new byte[] { 0x00 };
		case INT:
			int vi=-1;
			if(realData instanceof String) {
				vi=Integer.parseInt((String)realData);
			} else {
				vi=(Integer)realData;
			}
			return ByteUtil.intToBytes(vi, length, endian);
		case SHORT:
			short vs=-1;
			if(realData instanceof String) {
				vs=Short.parseShort((String)realData);
			} else {
				vs=(Short)realData;
			}
			return ByteUtil.shortToBytes(vs, length, endian);
		case LONG:
			long vl=-1;
			if(realData instanceof String) {
				vl=Long.parseLong((String)realData);
			} else {
				vl=(Long)realData;
			}
			return ByteUtil.longToBytes(vl, length, endian);
		case DOUBLE:
			double vd=-1;
			if(realData instanceof String) {
				vd=Double.parseDouble((String)realData);
			} else {
				vd=(Double)realData;
			}
			return ByteUtil.doubleToBytes(vd, length, endian);
		case BYTE:
			return (byte[])realData;
		case STRING:
		default:
			return ((String)realData).getBytes();
		}
	}

	@Override
	public String toPlainText() {
		StringBuffer sb=new StringBuffer();
		sb.append("interfaceId: " + interfaceId + "\n");
		sb.append("name: " + name + "\n");
		sb.append("order: " + order + "\n");		
		sb.append("type: " + type.name() + "\n");
		sb.append("length: " + length + "\n");
		sb.append("value: " + this.getValueAsString() + "\n");
		sb.append("description: " + description);

		return sb.toString();
	}
}
