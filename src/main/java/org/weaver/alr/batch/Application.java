package org.weaver.alr.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableAutoConfiguration
@Import(org.weaver.alr.batch.config.ContextConfig.class)
public class Application {

	public static void main(String[] args) {
		System.out.println("Application main");
		SpringApplication.run(Application.class, args);
	}

}
