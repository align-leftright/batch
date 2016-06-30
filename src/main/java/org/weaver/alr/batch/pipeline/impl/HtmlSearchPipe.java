package org.weaver.alr.batch.pipeline.impl;

import org.jsoup.select.Elements;
import org.weaver.alr.batch.pipeline.SyncPipe;

public class HtmlSearchPipe extends SyncPipe<Elements, Elements>{
	
	public static final String TYPE_ID = "id";
	public static final String TYPE_CLASS = "class";
	
	private String type;
	private String key;

	public HtmlSearchPipe(String type, String key) {
		super();
		this.type = type;
		this.key = key;
	}

	protected Elements use(Elements input) {
		if(TYPE_ID.equals(type)){
			return input.select("#"+key);
		}else if(TYPE_CLASS.equals(type)){
			return input.select("."+key);
		}else{
			return input;
		}
	}

	public String getType() {
		return type;
	}

	public String getKey() {
		return key;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	

}
