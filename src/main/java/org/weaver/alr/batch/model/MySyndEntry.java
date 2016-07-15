package org.weaver.alr.batch.model;

import org.weaver.alr.batch.common.util.DateUtIl;

import com.rometools.rome.feed.synd.SyndEntry;

public class MySyndEntry{
	
	private SyndEntry syndEntry;
	
	private String imageUrl;
	
	private String shortBody;
	
	private String contentUrl;
	

	public MySyndEntry(SyndEntry syndEntry){
		this.syndEntry =  syndEntry;
	}
	

	public String getShortBody() {
		return shortBody;
	}



	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public void setShortBody(String shortBody) {
		this.shortBody = shortBody;
	}


	public SyndEntry getSyndEntry() {
		return syndEntry;
	}

	public void setSyndEntry(SyndEntry syndEntry) {
		this.syndEntry = syndEntry;
	}
	
	
	


	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("PublishedDate : "+DateUtIl.getDate(syndEntry.getPublishedDate())+"\n");
		sb.append("Author        : "+syndEntry.getAuthor()+"\n");
		sb.append("PublishedDate : "+syndEntry.getPublishedDate()+"\n");
		sb.append("Title         : "+syndEntry.getTitle()+"\n");
		sb.append("Uri           : "+syndEntry.getUri()+"\n");
		sb.append("imageUrl      : "+imageUrl+"\n");
		sb.append("shortBody     : "+shortBody+"\n");
		sb.append("contentUrl    : "+contentUrl+"\n");
		return sb.toString();
	}
	
	
	
}
