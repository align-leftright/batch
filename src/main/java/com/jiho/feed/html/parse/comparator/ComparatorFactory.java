package com.jiho.feed.html.parse.comparator;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiho.feed.html.vo.ArticleImage;

@Component
public class ComparatorFactory {

	@Autowired
	private FirstComparator firstImgComparator;

	@Autowired
	private SizeComparator imgSizeComparator;

	public Comparator<ArticleImage> build(){
		return imgSizeComparator;
	}

	public Comparator<ArticleImage> build(ImageSortType type){
		switch (type) {
		case FIRST_IMAGE:
			return firstImgComparator;
		case IMAGE_SIZE:
		default:
			return imgSizeComparator;
		}
	}
}
