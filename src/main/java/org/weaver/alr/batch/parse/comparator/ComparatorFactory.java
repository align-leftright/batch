package org.weaver.alr.batch.parse.comparator;

import java.util.Comparator;

import org.weaver.alr.batch.model.ArticleImageVO;

public class ComparatorFactory {

	public enum ImageSortType {
		IMAGE_SIZE,
		FIRST_IMAGE
	}

	public static Comparator<ArticleImageVO> build(){
		return new SizeComparator();
	}

	public static Comparator<ArticleImageVO> build(ImageSortType type){
		switch (type) {
		case FIRST_IMAGE:
			return new FirstComparator();
		case IMAGE_SIZE:
		default:
			return new SizeComparator();
		}
	}
}
