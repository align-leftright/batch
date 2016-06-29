package org.weaver.alr.batch;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.weaver.alr.batch.config.MyConfig;
import org.weaver.alr.batch.model.SettingVO;
import org.weaver.alr.batch.model.TaskVO;
import org.weaver.alr.batch.util.FileUtil;
import org.weaver.alr.batch.util.JsonUtil;

public class FeedTest extends MyConfig{

	@Autowired
	private RSSFeeder rSSFeeder;
	
	private SettingVO setting;
	
	
	@Before
	public void testJson(){
		System.out.println("testJson");
		
		String config = FileUtil.readFile("sample.json");
		System.out.println(config);
		
		setting = (SettingVO) JsonUtil.fromJson(config, SettingVO.class);
		System.out.println(JsonUtil.toJson(setting));
	}
	
	@Test
	public void test() throws InterruptedException {
		System.out.println("main");
		
		List<TaskVO> tasks =	setting.getTasks();
		for(int i=0 ; i<tasks.size() ; i++){
			
			TaskVO task = tasks.get(i);
			System.out.println("----------------"+i+"------------------------");
			System.out.println(task.getName());
			System.out.println(JsonUtil.toJson(task.getInput()));
			System.out.println(JsonUtil.toJson(task.getOutput()));

			rSSFeeder.run(task.getName(), task.getInput().getUrl(), task.getPipeline());
		}
	}

}








