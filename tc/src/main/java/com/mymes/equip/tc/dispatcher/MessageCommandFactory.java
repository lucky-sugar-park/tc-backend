package com.mymes.equip.tc.dispatcher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

import org.springframework.context.ApplicationContext;

import com.mymes.equip.tc.ToolControlException;
import com.mymes.equip.tc.interfs.InterfaceInfo;
import com.mymes.equip.tc.interfs.InterfaceService;
import com.mymes.equip.tc.session.SessionContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageCommandFactory {

	private static MessageCommandFactory factory;
	
	private Map<String, MessageCommand> commandMap;
	
	private Map<String, CommandInfo> commandInfoMap;
	
	private boolean loaded=false;
	
	private static final String EXTJAR=".jar";
	
	private static final String EXTCLASS=".class";
	
	private static final String ROOT_PACKAGE="com.mymes.equip.tc";
	
	private Set<String> classNameSet=new HashSet<String>();
	
	private final Set<Class<?>> commandClasses = new HashSet<>();
	
	private ApplicationContext applicationContext;
	
	private MessageCommandFactory() throws ToolControlException {
		commandMap = new HashMap<>();
		commandInfoMap = new HashMap<>();
		applicationContext=SessionContext.getGlobalSessionContext().getApplicationContext();
		if(loaded==false) {
			findClasses();
			loadCommand();
			loaded = true;
		}
	}
	
	public MessageCommand getMessageCommand(String opcode) throws ToolControlException {
		MessageCommand command=commandMap.get(opcode);
		if(command==null) {
			String commandClassName=DispatcherConfig.getCommandName(opcode);
			if(commandClassName==null) {
				InterfaceService ifservice=(InterfaceService)applicationContext.getBean(InterfaceService.class);
				InterfaceInfo iInfo=ifservice.findInterfaceByName(opcode);
				commandClassName=iInfo.getCommandClassName();
				if(commandClassName==null || "".equals(commandClassName)) {
					if(iInfo.isUseGenericCommandClass()) {
						commandClassName="com.mymes.equip.tc.dispatcher.GenericMessageCommand";
					} else {
						throw new ToolControlException();
					}
				}
			}
			command=instance(commandClassName);
			commandMap.put(opcode, command);
		}
		return command;
	}
	
	public CommandInfo getMessageCommandInfo(String opcode) throws ToolControlException {
		return commandInfoMap.get(opcode);
	}
	
	public static MessageCommandFactory getFactory () throws ToolControlException {
		if(factory==null) {
			factory=new MessageCommandFactory ();
		}
		return factory;
	}
	
	public MessageCommand instance(String className) throws ToolControlException {
		MessageCommand command=(MessageCommand)SessionContext.getGlobalSessionContext().getBean(className);
		log.debug("Command registered at spring as bean name: {}, bean: {}", className, command);
		if(command==null) {
			try {
				command=(MessageCommand)instance(Class.forName(className));
			} catch (ClassNotFoundException e) {
				log.warn("", e);
			} catch (ToolControlException e) {
				log.warn("", e);
			} catch (Exception e) {
				command=new GenericMessageCommand();
			}
		}
		return command;
	}
	
	public void addCommand(String opcode, CommandInfo commandInfo) throws ToolControlException{
		MessageCommand command = instance(commandInfo.getCommandClassName());
		if(command != null) {
			commandMap.put(opcode, command);
			commandInfoMap.put(opcode, commandInfo);
		}
	}
	
	private Object instance(Class<?> clazz) throws ToolControlException  {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException e) {
			log.warn("", e);
			throw new ToolControlException();
		} catch (IllegalAccessException e) {
			log.warn("", e);
			throw new ToolControlException();
		} catch (IllegalArgumentException e) {
			log.warn("", e);
			throw new ToolControlException();
		} catch (InvocationTargetException e) {
			log.warn("", e);
			throw new ToolControlException();
		} catch (NoSuchMethodException e) {
			log.warn("", e);
			throw new ToolControlException();
		} catch (SecurityException e) {
			log.warn("", e);
			throw new ToolControlException();
		}
	}
	
	private void loadCommand() throws ToolControlException {
		log.debug("");
		for (Class<?> clazz : commandClasses) {
			if(clazz.getAnnotation(Command.class)==null) continue;
			
			String[] opcodes=clazz.getAnnotation(Command.class).opcodes();

			CommandInfo commandInfo = new CommandInfo();
			commandInfo.setOpcodes(opcodes);
			commandInfo.setCommandClassName(clazz.getName());
			
			MessageCommand command;
			command = (MessageCommand)instance(clazz);
			log.info("Loading message command [{}]", command.getClass().getName());
			for(int i = 0; i < opcodes.length; i++) {
				commandMap.put(opcodes[i], command);
				commandInfoMap.put(opcodes[i], commandInfo);
			}
		}
	}
	
	private void findClasses(){
		log.debug("");
		String classpath = System.getProperty("java.class.path");
		String[] paths = classpath.split(System.getProperty("path.separator"));
		
		File file;
		
		for(String path : paths){
			file = new File(path);
			if(!file.isDirectory() && path.endsWith(EXTJAR)){
				findJarClasses(file);
			}else{
				String rootDirName = file.getAbsolutePath() + File.separator;
				findHierachyClasses(file, rootDirName);
			}
		}
		
		for(String className : classNameSet){
			if(className.indexOf("$") < 0){
				try{
					Class<?> clazz = (Class<?>)Class.forName(className);
					for(Class<?> interfaze : clazz.getInterfaces()){
						if(interfaze.equals(MessageCommand.class)){
							commandClasses.add(clazz);
							break;
						}
					}
				}catch(Throwable t){
					System.out.println();;
				}
			}
		}
		classNameSet = null;
	}
	
	private void findJarClasses(File file) {
		JarFile jar = null;
		try {
			jar = new JarFile(file);
		} catch (ZipException e) {
			System.out.println("");
		} catch (IOException e) {
			System.out.println("");
		}
        if (jar != null) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                int extIndex = name.lastIndexOf(EXTCLASS);
                if (extIndex > 0) {
                	String replacedName = name.substring(0, extIndex).replace("/", ".");
                	if (replacedName.startsWith(ROOT_PACKAGE)) {
                		classNameSet.add(replacedName);
                	}
                }
            }
        }
	}
	
	private void findHierachyClasses(File file, String rootDirName) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                findHierachyClasses(child, rootDirName);
            }
        } else {
        	String name = file.getAbsolutePath().replace(rootDirName, "");
        	int extIndex = name.lastIndexOf(EXTCLASS);
            if (extIndex > 0) {
            	String replacedName = name.substring(0, extIndex).replace(File.separator, ".");
            	if (replacedName.startsWith(ROOT_PACKAGE)) {
            		classNameSet.add(replacedName);
            	}
            } else if (name.endsWith(EXTJAR)) {
            	findJarClasses(file);
            }
        }
    }
}
