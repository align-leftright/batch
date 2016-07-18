package org.weaver.alr.batch.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextBuilder {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationContextBuilder.class);
	
	public static synchronized ApplicationContext build(String name, String url){
		
		IntConfig.feedName=name;
		try {
			IntConfig.feedUrl=new URL(url);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
			return null;
		}
		
		ApplicationContext conext = new AnnotationConfigApplicationContext(IntConfig.class);

		return conext;
	}
}
