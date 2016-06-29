package org.weaver.alr.batch.parse.comparator;

import java.util.Comparator;

import org.springframework.stereotype.Component;
import org.weaver.alr.batch.html.vo.ArticleImage;

@Component
public class SizeComparator implements Comparator<ArticleImage>{

	public int compare(ArticleImage o1, ArticleImage o2) {
		int area1 = o1.getHeight() * o2.getWidth();
		int area2 = o2.getHeight() * o2.getWidth();

		if(area1>area2){
			return 1;
		}else if(area1<area2){
			return -1;
		}else{
			return 1;
		}
	}


}