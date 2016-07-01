package org.weaver.alr.batch.parse.comparator;

import java.util.Comparator;

import org.springframework.stereotype.Component;
import org.weaver.alr.batch.model.ArticleImageVO;

@Component
public class FirstComparator implements Comparator<ArticleImageVO>{

	public int compare(ArticleImageVO o1, ArticleImageVO o2) {
		return 1;
	}

}
