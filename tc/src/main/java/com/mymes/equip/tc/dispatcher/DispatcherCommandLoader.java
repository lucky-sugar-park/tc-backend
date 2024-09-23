package com.mymes.equip.tc.dispatcher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.mymes.equip.tc.ToolControlException;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
@DependsOn({"sessionContextInit"})
public class DispatcherCommandLoader {

	@Autowired
	private DispatcherConfig dispatcherConfig;
	
	@PostConstruct
	public void init() {
		log.debug("DispatcherCommandLoader.init()...");
		try {
			MessageCommandFactory factory = MessageCommandFactory.getFactory();
			List<CommandInfo> commands=dispatcherConfig.getCommands();
			if(commands==null) return;
			
			dispatcherConfig.getCommands().forEach((commandInfo)-> {
				try {
					factory.addCommand(commandInfo.getOpcode(), commandInfo);
					log.info("opcode: {}", commandInfo.getOpcode());
				} catch (ToolControlException e) {
					log.warn("", e);
				}
			});
		} catch (Exception e) {
			log.error("", e);
			System.exit(0);
		}
	}
}
