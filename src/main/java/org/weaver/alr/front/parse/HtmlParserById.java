package org.weaver.alr.front.parse;

import java.util.Comparator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.weaver.alr.front.html.vo.ArticleImage;

public class HtmlParserById extends HtmlParser{


	HtmlParserById(Comparator<ArticleImage> comparator) {
		super(comparator);
	}

	@Override
	protected Elements searchByKey(Document doc, String key) {
		Element e = doc.getElementById(key);
		Elements elements = new Elements();
		elements.add(e);
		return elements;
	}

}
