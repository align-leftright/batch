package org.weaver.alr.batch;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.weaver.alr.batch.common.util.FileUtil;
import org.weaver.alr.batch.common.util.JsonUtil;
import org.weaver.alr.batch.config.ContextConfig;
import org.weaver.alr.batch.model.SettingVO;

public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);


	public static void main(String[] args) {

		logger.info("main------------------");
		System.setProperty("http.proxyHost", "168.219.61.252");
		System.setProperty("http.proxyPort", "8080");
		
		ApplicationContext ctx = SpringApplication.run(ContextConfig.class, args);
		RSSManager rSSManager = ctx.getBean(RSSManager.class);
		logger.info("init------------------");
		
		List<SettingVO> fileList = buildSettingList(ctx, "/channels/*");
		try {
			logger.info("run------------------");
			rSSManager.run(fileList);
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


	static List<SettingVO> buildSettingList(ApplicationContext ctx, String locationPattern){
		logger.info("getChannelFiles------------------------------------------------------------------");
		Resource[] resources;
		
		List<SettingVO> fileList = new LinkedList<SettingVO>();
		
		try {
			resources = (Resource[]) ctx.getResources(locationPattern);
			for(int i=0 ; i<resources.length ; i++){
				logger.info(""+resources[i].getDescription());
			
				String config = FileUtil.readInputStream(resources[i].getInputStream());
				SettingVO settingVO = (SettingVO) JsonUtil.fromJson(config, SettingVO.class);
				fileList.add(settingVO);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("getChannelFiles------------------------------------------------------------------");
		return fileList;
	}









}
