package org.weaver.alr.batch.parse;

import org.jsoup.select.Elements;

public abstract class HtmlParser {
	
	public abstract String parseImage(Elements element);

	public abstract String parseText(Elements element);
	
}