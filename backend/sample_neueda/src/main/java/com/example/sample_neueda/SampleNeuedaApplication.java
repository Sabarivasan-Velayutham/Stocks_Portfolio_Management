package com.example.sample_neueda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SampleNeuedaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleNeuedaApplication.class, args);
	}

}
