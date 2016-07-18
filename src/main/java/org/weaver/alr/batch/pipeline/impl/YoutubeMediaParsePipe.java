package org.weaver.alr.batch.pipeline.impl;

import java.util.HashMap;
import java.util.Map;

import org.weaver.alr.batch.common.Constants;
import org.weaver.alr.batch.pipeline.SyncPipe;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndMedia;

public class YoutubeMediaParsePipe extends SyncPipe<SyndEntry, Map<String, Object>>{

	@Override
	protected Map<String, Object> use(SyndEntry input) {
		SyndMedia media = input.getMedia();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Constants.Pipeline.SHORT_TEXT, media.getDescription());
		result.put(Constants.Pipeline.IMAGE_URL, media.getThumbnailUrl());
		result.put(Constants.Pipeline.CONTENT_URL, media.getContentUrl());
		
		return result;
	}

}
