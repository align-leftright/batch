package org.weaver.alr.batch;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.weaver.alr.batch.common.Constants;
import org.weaver.alr.batch.config.MyConfig;
import org.weaver.alr.batch.news.Metadata;
import org.weaver.alr.batch.service.ElasticSearchService;

public class ESTest extends MyConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ESTest.class);
	
	@Autowired
	private ElasticSearchService elasticSearchService;	

	@Test
	public void test() throws InterruptedException {
		logger.debug("main");
		Metadata m = elasticSearchService.getDocument(Constants.ES_INDEX,Constants.ES_TYPE_NEWS, "1", Metadata.class);
		logger.debug(m.toString());
	}

}
