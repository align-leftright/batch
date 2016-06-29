package org.weaver.alr.batch.html.vo;

public class Article{

	String text;
	String src;

	public Article(String text, String src) {
		super();
		this.text = text;
		this.src = src;
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("text   : "+text);
		sb.append("\n");
		sb.append("imgSrc : "+src);
		return sb.toString();
	}
	
}
