package org.weaver.alr.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.messaging.PollableChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.weaver.alr.batch.common.Constants;
import org.weaver.alr.batch.common.util.JsonUtil;
import org.weaver.alr.batch.config.FeedInfo;
import org.weaver.alr.batch.config.IntConfig;
import org.weaver.alr.batch.model.OutputVO;
import org.weaver.alr.batch.model.SettingVO;
import org.weaver.alr.batch.model.TaskVO;
import org.weaver.alr.batch.output.Output;
import org.weaver.alr.batch.output.impl.ElasticSearchOutput;
import org.weaver.alr.batch.output.impl.FileOutput;

@Component
public class RSSManager {
	
	private static final Logger logger = LoggerFactory.getLogger(RSSManager.class);

	@Autowired
	private ApplicationContext context;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	private ElasticSearchOutput esOutput;
	
	@Autowired
	private FileOutput fileOutput;
	
	public void run(List<SettingVO> fileList) throws InterruptedException {
		for(SettingVO settingVO : fileList){
			run(settingVO);
		}
	}
	
	public void run(SettingVO settingVO) throws InterruptedException{
		List<TaskVO> tasks = settingVO.getTasks();
		for(int i=0 ; i<tasks.size() ; i++){
			TaskVO task = tasks.get(i);
			logger.info("----------------"+i+"------------------------");
			logger.info(task.getName());
			logger.info(JsonUtil.toJson(task.getInput()));
			logger.info(JsonUtil.toJson(task.getOutput()));

			RSSFeeder rSSFeeder = context.getBean(RSSFeeder.class);
			Object[] result = createFeedChannel(task.getName(), task.getInput().getUrl());
			PollableChannel feedChannel = (PollableChannel)result[0];
			PropertiesPersistingMetadataStore metadataStore =(PropertiesPersistingMetadataStore)result[1];
			Output output = selectOutput(task.getOutput());
			rSSFeeder.initialize(task.getName(), task.getInput().getUrl(), feedChannel, metadataStore, task.getPipeline(), output);
			taskExecutor.execute(rSSFeeder);
		}
	}
	
	
	private Output selectOutput(OutputVO output){
		
		String type = output.getType();
		if(Constants.Output.TYPE_ES.equals(type)){
			return esOutput;
		}else if(Constants.Output.TYPE_FILE.equals(type)){
			return fileOutput;
		}
		return null;
		
	}
	
	
	
	public int getActiveCount(){
		return taskExecutor.getActiveCount();
	}
	
	
	public void shutdown(){
		taskExecutor.shutdown();
	}
	
	
	private Object[] createFeedChannel(String name, String url){
		logger.info("createFeed");
		logger.info(name);
		logger.info(url);

		ApplicationContext conext = initApplicationContext(name, url);
		PollableChannel feedChannel = conext.getBean(PollableChannel.class);
		PropertiesPersistingMetadataStore metadataStore = conext.getBean(PropertiesPersistingMetadataStore.class);

		Object[] result = {feedChannel, metadataStore};
		return result;
	}

	private synchronized ApplicationContext initApplicationContext(String name, String url){
		IntConfig.queue.add(new FeedInfo(name, url));
		ApplicationContext conext = new AnnotationConfigApplicationContext(IntConfig.class);
		IntConfig.queue.remove();
		return conext;
	}



}
