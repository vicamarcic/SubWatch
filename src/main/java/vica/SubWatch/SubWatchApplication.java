package vica.SubWatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SubWatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubWatchApplication.class, args);
	}

}
