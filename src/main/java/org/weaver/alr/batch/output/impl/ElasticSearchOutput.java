package org.weaver.alr.batch.output.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.weaver.alr.batch.output.Output;
import org.weaver.alr.batch.service.ElasticSearchService;

@Component
public class ElasticSearchOutput implements Output{


	private static final Logger logger = LoggerFactory.getLogger(ElasticSearchOutput.class);

	public static final String KEY_INDEX = "index";
	public static final String KEY_DOC_TYPE = "docType";
	public static final String KEY_DOC_ID = "docId";
	public static final String KEY_DOC = "doc";

	@Autowired
	private ElasticSearchService elasticSearchService;	


	public void send(Map<String, Object> message) {
		
		if(message == null || message.size() == 0){
			logger.debug("send fail : message is empty");
			return;
		}
		

		String index   = (String)message.get(KEY_INDEX);
		String docType = (String)message.get(KEY_DOC_TYPE);
		Object doc     = message.get(KEY_DOC);
		String docId   = (String)message.get(KEY_DOC_ID);
		elasticSearchService.putDocument(index, docType, docId, doc);
		logger.debug("send ok");

	}

}
