package com.mymes.equip.tc.dispatcher;

import lombok.Data;

@Data
public class CommandInfo {

	private String opcode;
	
	private String[] opcodes;
	
	private String commandClassName;
}
