package org.weaver.alr.batch.pipeline.impl;

import org.jsoup.select.Elements;
import org.weaver.alr.batch.common.util.JsoupUtil;
import org.weaver.alr.batch.pipeline.SyncPipe;

import com.rometools.rome.feed.synd.SyndEntry;

public class SyndToElementPipe extends SyncPipe<SyndEntry, Elements>{

	@Override
	protected Elements use(SyndEntry input) {
		String url = input.getUri();
		Elements elements = JsoupUtil.getAllElements(url);
		return elements;
	}

}
