package com.mymes.equip.tc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import lombok.extern.slf4j.Slf4j;

//@EntityScan(basePackages={"com.mymes.equip.tc"})
@SpringBootApplication (
	scanBasePackages={	"com.mymes.equip.tc" }
)
@EnableJpaRepositories(
		basePackages={
				"com.mymes.equip.tc.auth",
				"com.mymes.equip.tc.dispatcher",
				"com.mymes.equip.tc.interfs", 
				"com.mymes.equip.tc.msg", 
				"com.mymes.equip.tc.schedule", 
				"com.mymes.equip.tc.plc",
				"com.mymes.equip.tc.user",
				"com.mymes.equip.tc.webhook",
		}
	)
// Jwt 인증방식 적용으로 HttpSession 불필요 (Redis를 통한 세션공유 불필요함)
// @EnableRedisHttpSession
@Slf4j
public class ToolControlApplication implements CommandLineRunner {
	
	public static void main( String[] args ) {
		log.debug("ToolControlApplication is starting...");
		SpringApplication.run(ToolControlApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				log.info("");
			}
		});
		
		Executors.newCachedThreadPool().execute(()->{
////			listenCommand();
		});
		
	}
	
	private boolean stop;
	private void listenCommand() {
		log.info("");
		while(!stop) {
			try {
				ServerSocket serverSocket=new ServerSocket(9999);
				Socket socket=serverSocket.accept();
				SocketAddress address=socket.getRemoteSocketAddress();

				if(!"localhost".equals(address.toString()) || !"127.0.0.1".equals(address.toString())) {
					continue;
				}

				BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				OutputStream output=socket.getOutputStream();
				PrintWriter writer=new PrintWriter(output, true);
				
				String command=reader.readLine();
				switch(command.toUpperCase()) {
				case "STOP":
					writer.println("Stopping Tool Control Server...");
					stop=true;
					socket.close();
					serverSocket.close();
					break;
				case "STATUS":
					writer.println("OK!");
					break;
				default:
					writer.println("Wrong command [" + command + "], only stop or status can be used.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Terminate Tool Control Server
		log.info("Terminated Tool Control Server by local command from Admininstrator.");
		System.exit(0);
	}
}
