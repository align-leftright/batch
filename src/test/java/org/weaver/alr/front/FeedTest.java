package org.weaver.alr.front;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weaver.alr.front.RSSFeeder;
import org.weaver.alr.front.parse.HtmlParser;
import org.weaver.alr.front.parse.HtmlParserFactory;
import org.weaver.alr.front.parse.HtmlParserType;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={org.weaver.alr.front.config.ContextConfig.class})
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
