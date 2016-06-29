package org.weaver.alr.batch.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

	@Value("${es.host}")
	private String esHost;
	
	@Value("${es.port}")
	private Integer esPort;

	@Bean
	Client esClient() throws UnknownHostException {
		return TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost), esPort));
	}
}
