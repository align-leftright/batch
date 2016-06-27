package com.jiho.feed.html.parse;

import java.util.Comparator;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.jiho.feed.html.vo.ArticleImage;

public class HtmlParserByClassName extends HtmlParser{

	HtmlParserByClassName(Comparator<ArticleImage> comparator) {
		super(comparator);
	}

	@Override
	protected Elements searchByKey(Document doc, String key) {
		return doc.getElementsByClass(key);
	}

}
