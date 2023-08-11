package com.ppojin.kafkatester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KafkaTesterApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaTesterApplication.class, args);
	}

}
