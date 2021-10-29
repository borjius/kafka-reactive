package com.kafka.reactive.kafka.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class KafkaReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaReactiveApplication.class, args);
	}

}
