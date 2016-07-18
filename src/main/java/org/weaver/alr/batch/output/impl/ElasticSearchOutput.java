package org.weaver.alr.batch.output.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.weaver.alr.batch.common.Constants;
import org.weaver.alr.batch.output.BasicOutput;
import org.weaver.alr.batch.service.ElasticSearchService;

@Component
public class ElasticSearchOutput extends BasicOutput{

	@Autowired
	private ElasticSearchService elasticSearchService;	

	public void process(String docId, Object doc) {
		elasticSearchService.putDocument(Constants.ES_INDEX, Constants.ES_TYPE_NEWS, docId, doc);
	}
}
