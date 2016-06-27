package org.weaver.alr.front.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan(basePackages={"com.jiho.feed"})
public class ContextConfig {

	@Bean
	public DataSource mysqlDataSource() throws SQLException {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://10.251.83.180:3306/jiho");
		dataSource.setUsername("root");
		dataSource.setPassword("ecosystem");
		return dataSource;
	}
	
	
	
	
	
	
	
}
