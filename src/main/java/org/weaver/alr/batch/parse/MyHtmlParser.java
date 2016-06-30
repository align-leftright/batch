package org.weaver.alr.batch.parse;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Comparator;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.weaver.alr.batch.html.vo.ArticleImage;
import org.weaver.alr.batch.parse.comparator.ComparatorFactory;
import org.weaver.alr.batch.util.FileUtil;
import org.weaver.alr.batch.util.ImageReaderUtil;
import org.weaver.alr.batch.util.StringUtil;

import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;

public class MyHtmlParser extends HtmlParser{
	
	private static final int TEXT_MAX_SIZE = 100;	
	
	private Comparator<ArticleImage> comparator;
	
	public MyHtmlParser(){
		this.comparator = ComparatorFactory.build();
	}

	public MyHtmlParser(Comparator<ArticleImage> comparator){
		this.comparator = comparator;
	}
	
	
	public String parseImage(Elements elements){
		if(elements == null || elements.isEmpty()){
			return null;
		}
		TreeSortedSet<ArticleImage> treeSortedSet = new TreeSortedSet<ArticleImage>(comparator);
		Elements imgElements;
		try {
			imgElements = elements.select("img");
		} catch (Exception e) {
			return null;
		}

		for(Element imgElement : imgElements){
			ArticleImage articleImage = convertImage(imgElement);
			if(articleImage != null){
				treeSortedSet.add(articleImage);
			}else{
			}
		}

		if(treeSortedSet.size()>0){
			return  treeSortedSet.getLast().getSrc();
		}

		return null;
	}

	private ArticleImage convertImage(Element imgElement){

		File file = null;
		String src;
		int widthInt;
		int heightInt;
		
		String width  = imgElement.attr("width");
		String height = imgElement.attr("height");

		src    = imgElement.attr("src");
		if(StringUtil.isEmpty(src)){
			src = imgElement.attr("data-src");
		}

		if(StringUtil.isEmpty(src) || !src.contains("http://")){
			return null;
		}

		if( StringUtil.isEmpty(width) || StringUtil.isEmpty(height) ){
			try {
				
				URL url = new URL(src);
				file = FileUtil.creatTempFile(url);
				BufferedImage image = ImageReaderUtil.readImage(file);
				
				widthInt          = image.getWidth();
				heightInt         = image.getHeight();
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}finally{
//				file.delete();
			}
		}else{
			widthInt  = Integer.parseInt(width);
			heightInt = Integer.parseInt(height);
		}

		if(widthInt<=0 || heightInt<=0){
			return null;
		}

		return new ArticleImage(widthInt, heightInt, src);
	}

	
	
	
	public String parseText(Elements article){
		if(article == null || article.isEmpty()){
			return null;
		}
		String articleText;
		try {
			articleText = article.text();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if(!StringUtil.isEmpty(articleText)){
			if(articleText.length() > TEXT_MAX_SIZE){
				articleText = articleText.substring(0, TEXT_MAX_SIZE);
			}
			return articleText;
		}
		return null;
	}
	

}


