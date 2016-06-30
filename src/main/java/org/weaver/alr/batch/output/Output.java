package org.weaver.alr.batch.output;

import java.util.Map;

public interface Output {
	
	public void send(Map<String, Object> message);
	
}
