package com.jiho.feed.html.parse;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.jiho.feed.html.vo.Article;
import com.jiho.feed.html.vo.ArticleImage;
import com.jiho.feed.util.StringUtil;

public abstract class HtmlParser{
	
	private static final int TEXT_MAX_SIZE = 100;	

	private Comparator<ArticleImage> comparator;
	
	HtmlParser(Comparator<ArticleImage> comparator){
		this.comparator = comparator;
	}
	
	private ArticleImage convertArticleImage(Element imgElement){

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
				BufferedImage image = ImageIO.read(url);
				widthInt          = image.getWidth();
				heightInt         = image.getHeight();
			}catch (IOException e) {
				e.printStackTrace();
				return null;
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
	
	
	private String parseArticleImage(Elements article){
		if(article == null || article.isEmpty()|| article.size()<=1){
			return null;
		}
		TreeSortedSet<ArticleImage> treeSortedSet = new TreeSortedSet<ArticleImage>(comparator);
		Elements imgElements;
		try {
			imgElements = article.select("img");
		} catch (Exception e) {
			return null;
		}

		for(Element imgElement : imgElements){
			ArticleImage articleImage = convertArticleImage(imgElement);
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

	private String parseArticleText(Elements article){
		if(article == null || article.isEmpty() || article.size()<=1){
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

	public Article search(final String uri, final String key){
		Document doc = null;
		try {
			doc = Jsoup.connect(uri).get();

			Elements articleElements = searchByKey(doc, key);
			String articleText = parseArticleText(articleElements);
			String articleSource = parseArticleImage(articleElements);

			return new Article(articleText, articleSource);

		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	

	abstract protected Elements searchByKey(Document doc, String key);


//	private void printTree(TreeSortedSet<ArticleImage> treeSortedSet){
//
//		for(ArticleImage articleImage : treeSortedSet){
//			System.out.println(articleImage);
//		}
//
//	}

}


