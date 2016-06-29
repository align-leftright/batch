package org.weaver.alr.batch.news;

public class Metadata {
	private String id;
	private String channel;
	private String title;
	private String description;
	private String imageUrl;
	private String linkUrl;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	
	@Override
	public String toString() {
		return "Metadata [id=" + id + ", channel=" + channel + ", title="
				+ title + ", description=" + description + ", imageUrl="
				+ imageUrl + ", linkUrl=" + linkUrl + "]";
	}
	
	
	
}