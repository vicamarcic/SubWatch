package vica.SubWatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import vica.SubWatch.properties.AppMailProperties;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties(AppMailProperties.class)
public class SubWatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubWatchApplication.class, args);
	}

}
