package org.sample.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan(
		basePackages={ "org.camunda", "org.sample.server" }
)
public class SampleServerApp implements CommandLineRunner {
	
    public static void main( String[] args ) {
        SpringApplication.run(SampleServerApp.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		log.info("SampleServerApp is started..." );
	}
	
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
