package com.techlab.picadito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PicaditoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PicaditoApplication.class, args);
	}

}

