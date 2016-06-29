package org.weaver.alr.batch.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages={"org.weaver.alr.batch"})
@EnableAutoConfiguration
@PropertySource("classpath:properties/config.properties")
public class ContextConfig {

	
	
}
