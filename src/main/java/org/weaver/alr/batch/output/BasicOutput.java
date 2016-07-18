package org.weaver.alr.batch.output;

import java.util.Map;

import org.weaver.alr.batch.common.Constants;
import org.weaver.alr.batch.metadata.News;

public abstract class BasicOutput implements Output{
	
	public void send(Map<String, Object> message) {
		String docId = (String) message.get(Constants.Output.KEY_DOC_ID);
		News news    = (News)message.get(Constants.Output.KEY_DOC);
		process(docId, news);
	}
	
	abstract public void process(String docId, Object doc);

}
