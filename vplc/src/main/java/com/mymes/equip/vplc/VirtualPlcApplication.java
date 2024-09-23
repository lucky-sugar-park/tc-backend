package com.mymes.equip.vplc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication (scanBasePackages={
	"com.mymes.equip.vplc"
})
@Slf4j
public class VirtualPlcApplication implements CommandLineRunner {

	public static void main( String[] args ) {
		SpringApplication.run(VirtualPlcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		StringBuilder sb=new StringBuilder();
		sb.append("##    ##  ##  #####    ########  ##   ##    ###    ##         #####    ##         #####\n");
		sb.append("##    ##  ##  ##   ##     ##     ##   ##  ##   ##  ##         ##   ##  ##       ##     \n");
		sb.append("##    ##  ##  ##   ##     ##     ##   ##  ##   ##  ##         ##   ##  ##       ##     \n");
		sb.append("##    ##  ##  #####       ##     ##   ##  #######  ##         #####    ##       ##     \n");
		sb.append("##    ##  ##  ##   ##     ##     ##   ##  ##   ##  ##         ##       ##       ##     \n");
		sb.append(" ##  ##   ##  ##   ##     ##     ##   ##  ##   ##  ##      ## ##       ##       ##     \n");
		sb.append("   ##     ##  ##   ##     ##      #####   ##   ##  ####### ## ##       #######    #####\n");
		
		log.info("Virtual PLC is started...\n{}", sb.toString());
	}
}
