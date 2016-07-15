package org.weaver.alr.batch.common.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsoupUtil {

	private static final Logger logger = LoggerFactory.getLogger(JsoupUtil.class);
	
	public static Elements getAllElements(String url){

		Document doc;
		String url2 = url.replaceAll("(?<!http:)//", "/");
		
		try {
			doc = Jsoup.connect(url2).get();
		
		} catch (IOException e) {
			
			logger.error(url);
			logger.error(e.toString());
			
			e.printStackTrace();
			return null;
		}
		
		return doc.getAllElements();
	}
	
}
