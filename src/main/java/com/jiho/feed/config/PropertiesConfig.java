package com.jiho.feed.config;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;


@Component
public class PropertiesConfig {

	private static String CONFIG_PATH = "/properties/test.properties";

	public synchronized void createResourceManagement(){
		createResourceManagement(CONFIG_PATH);
	}

	public synchronized void createResourceManagement(String url) {
		Resource resource = new ClassPathResource(url);
		
		try {
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			Enumeration<?> em = props.keys();			

			while(em.hasMoreElements()){
				String key = (String)em.nextElement();
				System.out.println(key+" : "+props.getProperty(key));
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
