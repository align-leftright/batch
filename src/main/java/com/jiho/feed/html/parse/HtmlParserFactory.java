package com.jiho.feed.html.parse;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiho.feed.html.parse.comparator.ComparatorFactory;
import com.jiho.feed.html.parse.comparator.ImageSortType;
import com.jiho.feed.html.vo.ArticleImage;

@Component
public class HtmlParserFactory {

	@Autowired
	private ComparatorFactory comparatorFactory;
	
	public HtmlParser build(HtmlParserType parsertype){
		return build(parsertype, ImageSortType.FIRST_IMAGE);
	}

	public HtmlParser build(HtmlParserType parsertype, ImageSortType imageSortType){

		HtmlParser parser = null;
		Comparator<ArticleImage> comparator = comparatorFactory.build(imageSortType);
		
		switch (parsertype) {
		case ID:
			parser = new HtmlParserById(comparator);
			break;
		case CLASSNAME:
		default:
			parser = new HtmlParserByClassName(comparator);
			break;
		}

		return parser;
	}

}
