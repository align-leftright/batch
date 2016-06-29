package org.weaver.alr.batch.parse.comparator;

import java.util.Comparator;

import org.weaver.alr.batch.html.vo.ArticleImage;

public class ComparatorFactory {

	public enum ImageSortType {
		IMAGE_SIZE,
		FIRST_IMAGE
	}

	public static Comparator<ArticleImage> build(){
		return new SizeComparator();
	}

	public static Comparator<ArticleImage> build(ImageSortType type){
		switch (type) {
		case FIRST_IMAGE:
			return new FirstComparator();
		case IMAGE_SIZE:
		default:
			return new SizeComparator();
		}
	}
}
