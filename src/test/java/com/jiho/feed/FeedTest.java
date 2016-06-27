package com.jiho.feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jiho.feed.html.parse.HtmlParser;
import com.jiho.feed.html.parse.HtmlParserFactory;
import com.jiho.feed.html.parse.HtmlParserType;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={com.jiho.feed.config.ContextConfig.class})
public class FeedTest {
	
	
	@Autowired
	private RSSFeeder rSSFeeder;
	
	@Autowired
	private HtmlParserFactory HtmlParserFactory;
	
	
	
	@Test
	public void test() throws InterruptedException {
		System.out.println("main");
		HtmlParser html = HtmlParserFactory.build(HtmlParserType.ID);
		rSSFeeder.run("http://rss.cnn.com/rss/cnn_topstories.rss", html);
	}


}
