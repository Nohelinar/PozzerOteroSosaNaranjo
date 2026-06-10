package com.tuti.desi.pozzeroterososanaranjo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PozzerOteroSosaNaranjoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PozzerOteroSosaNaranjoApplication.class, args);
	}

}
