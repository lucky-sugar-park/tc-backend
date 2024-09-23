package com.mymes.equip.tc.dispatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="")
@Slf4j
public class DispatcherConfig {

	private List<CommandInfo> commands;

	private static Map<String, CommandInfo> commandMap;

	static {
		commandMap = new HashMap<>();
	}

	public List<CommandInfo> getCommands() {
		return this.commands;
	}
	
	public void setCommands(List<CommandInfo> commands) {
		this.commands = commands;
		
		commands.forEach((commandInfo)->{
			log.info("OPCODE : " + commandInfo.getOpcode());
			log.info("COMMAND: " + commandInfo.getCommandClassName());
			commandMap.put(commandInfo.getOpcode(), commandInfo);
		});
	}

	public static String getCommandName(String opcode) {
		if(commandMap.get(opcode)!=null) {
			return commandMap.get(opcode).getCommandClassName();	
		}
		return null;
	}

	public static CommandInfo getCommand(String opcode) {
		return commandMap.get(opcode);
	}	
}
