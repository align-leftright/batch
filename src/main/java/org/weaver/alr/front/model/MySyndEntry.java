package org.weaver.alr.front.model;

import org.springframework.stereotype.Component;

@Component
public class MySyndEntry {
	
	private String imageUri;
	
	private String shortBody;


	public String getImageUri() {
		return imageUri;
	}


	public String getShortBody() {
		return shortBody;
	}


	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}


	public void setShortBody(String shortBody) {
		this.shortBody = shortBody;
	}
	
	
	
	
}
