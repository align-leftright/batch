package org.weaver.alr.batch;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.weaver.alr.batch.config.MyConfig;
import org.weaver.alr.batch.model.SettingVO;
import org.weaver.alr.batch.model.TaskVO;
import org.weaver.alr.batch.util.FileUtil;
import org.weaver.alr.batch.util.JsonUtil;

public class FeedTest extends MyConfig{

	private static final Logger logger = LoggerFactory.getLogger(FeedTest.class);

	
	@Autowired
	private RSSFeeder rSSFeeder;
	
	private SettingVO setting;
	
	
	@Before
	public void testJson(){
		logger.debug("testJson");
		
		String config = FileUtil.readFile("sample.json");
		logger.debug(config);
		
		setting = (SettingVO) JsonUtil.fromJson(config, SettingVO.class);
		logger.debug(JsonUtil.toJson(setting));
	}
	
//	@Test
	public void test() throws InterruptedException {
		logger.debug("main");
		
		List<TaskVO> tasks =	setting.getTasks();
		for(int i=0 ; i<tasks.size() ; i++){
			
			TaskVO task = tasks.get(i);
			logger.debug("----------------"+i+"------------------------");
			logger.debug(task.getName());
			logger.debug(JsonUtil.toJson(task.getInput()));
			logger.debug(JsonUtil.toJson(task.getOutput()));

			rSSFeeder.run(task.getName(), task.getInput().getUrl(), task.getPipeline());
		}
	}

}








