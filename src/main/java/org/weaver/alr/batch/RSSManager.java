package org.weaver.alr.batch;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.weaver.alr.batch.common.util.FileUtil;
import org.weaver.alr.batch.common.util.JsonUtil;
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
	
	

	public void init(){
		logger.debug("init");
		settingList = new LinkedList<SettingVO>();
		List<File> fileList = FileUtil.listFilesForFolder(FileUtil.getFile("channels"));
		for(File file : fileList){
			String config = FileUtil.readFile(file);
			SettingVO setting = (SettingVO) JsonUtil.fromJson(config, SettingVO.class);
			settingList.add(setting);
		}
	}
	
	
	public void run() throws InterruptedException {
		logger.debug("test");
		System.out.println("settingList size : "+settingList.size());
		for(SettingVO setting : settingList){
			run(setting);
		}
		
//		for (;;) {
//			int count = taskExecutor.getActiveCount();
//			System.out.println("Active Threads : " + count);
//			try {
//				Thread.sleep(1000*60*30);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			if (count == 0) {
//				taskExecutor.shutdown();
//				break;
//			}
//		}

	}

	private void run(SettingVO setting) throws InterruptedException{
		List<TaskVO> tasks = setting.getTasks();
		for(int i=0 ; i<tasks.size() ; i++){
			TaskVO task = tasks.get(i);
			logger.debug("----------------"+i+"------------------------");
			logger.debug(task.getName());
			logger.debug(JsonUtil.toJson(task.getInput()));
			logger.debug(JsonUtil.toJson(task.getOutput()));

			RSSFeeder rSSFeeder = context.getBean(RSSFeeder.class);
			rSSFeeder.initialize(task.getName(), task.getInput().getUrl(), task.getPipeline(), output);
			taskExecutor.execute(rSSFeeder);
		}
	}
}
