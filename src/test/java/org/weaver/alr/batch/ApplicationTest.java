package org.weaver.alr.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.weaver.alr.batch.config.ContextConfig;
import org.weaver.alr.batch.model.SettingVO;

public class ApplicationTest {
	
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationTest.class);
	
	public static void main(String[] args){
		
		logger.info("main------------------");
		
		Application.initSystem();
		
		ApplicationContext ctx = SpringApplication.run(ContextConfig.class, args);
		RSSManager rSSManager = ctx.getBean(RSSManager.class);
		logger.info("init------------------");
		
		List<SettingVO> fileList = Application.buildSettingList(ctx, "/channels/tvN.json");
		try {
			logger.info("run------------------");
			rSSManager.run(fileList);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
	

}
