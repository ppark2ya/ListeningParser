package com.myapp.senier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ListeningParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListeningParserApplication.class, args);
	}

}

