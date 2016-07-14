package org.weaver.alr.batch.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ComponentScan(basePackages={"org.weaver.alr.batch"}, 
				excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.weaver.alr.batch.config.*"))
//@EnableAutoConfiguration(exclude = WebMvcAutoConfiguration.class)
@PropertySource("classpath:properties/config.properties")
public class ContextConfig {

	@Value("${es.host}")
	private String esHost;
	
	@Value("${es.port}")
	private Integer esPort;
	
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(5);
		pool.setMaxPoolSize(10);
		pool.setWaitForTasksToCompleteOnShutdown(true);
		return pool;
	}

	@Bean
	Client esClient() throws UnknownHostException {
		return TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost), esPort));
	}
	
	
}
