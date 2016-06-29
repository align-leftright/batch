package org.weaver.alr.batch.parse.comparator;

import java.util.Comparator;

import org.springframework.stereotype.Component;
import org.weaver.alr.batch.html.vo.ArticleImage;

@Component
public class FirstComparator implements Comparator<ArticleImage>{

	public int compare(ArticleImage o1, ArticleImage o2) {
		return 1;
	}

}
