package org.weaver.alr.batch;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.weaver.alr.batch.common.util.FileUtil;
import org.weaver.alr.batch.common.util.JsonUtil;
import org.weaver.alr.batch.common.util.StringUtil;
import org.weaver.alr.batch.model.SettingVO;
import org.weaver.alr.batch.model.TaskVO;
import org.weaver.alr.batch.output.impl.ElasticSearchOutput;

@Component
public class RSSManager {
	
	private static final Logger logger = LoggerFactory.getLogger(RSSManager.class);

	@Autowired
	private ApplicationContext context;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	private ElasticSearchOutput output;

	private List<SettingVO> settingList;
	
	public void init(List<SettingVO> settingList){
		this.settingList = settingList;
	}
	
	public void run() throws InterruptedException {
		logger.info("run");
		if(settingList == null || settingList.size()==0){
			return;
		}
		for(SettingVO setting : settingList){
			run(setting);
		}

	}

	private void run(SettingVO setting) throws InterruptedException{
		List<TaskVO> tasks = setting.getTasks();
		for(int i=0 ; i<tasks.size() ; i++){
			TaskVO task = tasks.get(i);
			logger.info("----------------"+i+"------------------------");
			logger.info(task.getName());
			logger.info(JsonUtil.toJson(task.getInput()));
			logger.info(JsonUtil.toJson(task.getOutput()));

			RSSFeeder rSSFeeder = context.getBean(RSSFeeder.class);
			rSSFeeder.initialize(task.getName(), task.getInput().getUrl(), task.getPipeline(), output);
			taskExecutor.execute(rSSFeeder);
		}
	}
	
	public int getActiveCount(){
		return taskExecutor.getActiveCount();
	}
	
	public void shutdown(){
		taskExecutor.shutdown();
	}
}
