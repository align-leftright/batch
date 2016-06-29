package org.weaver.alr.batch.pipeline.impl;

import org.jsoup.select.Elements;
import org.weaver.alr.batch.parse.MyHtmlParser;
import org.weaver.alr.batch.parse.comparator.ComparatorFactory;
import org.weaver.alr.batch.parse.comparator.ComparatorFactory.ImageSortType;
import org.weaver.alr.batch.pipeline.SyncPipe;

public class HtmlParserPipe extends SyncPipe<Elements, String>{

	public static final String TYPE_TEXT = "text";
	public static final String TYPE_IMG  = "image";
	
	public static final String SORT_TYPE_IMAGE_SIZE = "size";
	public static final String SORT_TYPE_FIRST_IMAGE = "order";
	
	private String type;
	private String sortType;
	
	
	public HtmlParserPipe(String type) {
		super();
		this.type = type;
	}
	
	public HtmlParserPipe(String type, String sortType) {
		super();
		this.type = type;
		this.sortType = sortType;
	}

	protected String use(Elements input) {
		
		MyHtmlParser myHtmlParser;
		if(SORT_TYPE_FIRST_IMAGE.equals(sortType)){
			myHtmlParser = new MyHtmlParser(ComparatorFactory.build(ImageSortType.IMAGE_SIZE));
		}else{
			myHtmlParser = new MyHtmlParser(ComparatorFactory.build(ImageSortType.FIRST_IMAGE));
		}
		
		
		if(TYPE_TEXT.equals(type)){
			return myHtmlParser.parseText(input);
		}else if(TYPE_IMG.equals(type)){
			return myHtmlParser.parseImage(input);
		}
		return null;
	}

	public String getType() {
		return type;
	}

	public String getSortType() {
		return sortType;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}


	

}
