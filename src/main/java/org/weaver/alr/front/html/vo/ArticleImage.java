package org.weaver.alr.front.html.vo;

public class ArticleImage {

	int width;
	int height;
	String src;

	public ArticleImage(){

	}

	public ArticleImage(int width, int height, String src) {
		super();
		this.width = width;
		this.height = height;
		this.src = src;
	}

	@Override
	public String toString() {
		return "("+width+", "+height+") "+src;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
	
	
	
	
}
