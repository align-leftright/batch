package org.weaver.alr.batch.common.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupUtil {

	
	public static Elements getAllElements(String url){

		Document doc;
		String url2 = url.replaceAll("(?<!http:)//", "/");
		
		try {
			doc = Jsoup.connect(url2).get();
		
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return doc.getAllElements();
	}
	
}
