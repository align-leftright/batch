package org.weaver.alr.batch;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.weaver.alr.batch.config.ContextConfig;

@SpringBootApplication
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	private static String path=null;
	
	public static void main(String[] args) {
		
		logger.info("main------------------");
		logger.info("String[] args");
		for(int i=0 ; i<args.length ; i++){
			logger.info(args[i]);
		}
		if(args.length == 1){
			path = args[0];
		}
		
		ApplicationContext ctx = SpringApplication.run(ContextConfig.class, args);
		RSSManager rSSManager = ctx.getBean(RSSManager.class);
		
		logger.info("init------------------");
		rSSManager.init(path);
		try {
			logger.info("run------------------");
			rSSManager.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public void printBeans(ApplicationContext ctx){
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			logger.info(beanName);
		}
	}

}
