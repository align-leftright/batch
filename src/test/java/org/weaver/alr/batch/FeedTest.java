package org.weaver.alr.batch;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.weaver.alr.batch.config.MyConfig;

public class FeedTest extends MyConfig{

	private static final Logger logger = LoggerFactory.getLogger(FeedTest.class);

	@Autowired
	private RSSManager rSSManager;

	@Test
	public void test() throws InterruptedException{
		logger.info("test");

//		rSSManager.init(null);
//		rSSManager.run();

	}


}








