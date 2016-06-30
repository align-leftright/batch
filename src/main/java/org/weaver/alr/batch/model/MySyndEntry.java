package org.weaver.alr.batch.model;

import org.weaver.alr.batch.util.DateUtIl;

import com.rometools.rome.feed.synd.SyndEntry;

public class MySyndEntry{
	
	private SyndEntry syndEntry;
	
	private String imageUri;
	
	private String shortBody;
	

	public MySyndEntry(SyndEntry syndEntry){
		this.syndEntry =  syndEntry;
	}
	
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


	public SyndEntry getSyndEntry() {
		return syndEntry;
	}

	public void setSyndEntry(SyndEntry syndEntry) {
		this.syndEntry = syndEntry;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("PublishedDate : "+DateUtIl.getDate(syndEntry.getPublishedDate())+"\n");
		sb.append("Author        : "+syndEntry.getAuthor()+"\n");
		sb.append("PublishedDate : "+syndEntry.getPublishedDate()+"\n");
		sb.append("Title         : "+syndEntry.getTitle()+"\n");
		sb.append("Uri           : "+syndEntry.getUri()+"\n");
		sb.append("imageUri      : "+imageUri+"\n");
		sb.append("shortBody     : "+shortBody+"\n");
		return sb.toString();
	}
	
	
	
}
