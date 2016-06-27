package com.jiho.feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.stereotype.Component;

import com.jiho.feed.DynamicChannelResolver.ChannelManagement;
import com.jiho.feed.html.parse.HtmlParser;
import com.jiho.feed.html.vo.Article;
import com.rometools.rome.feed.synd.SyndEntry;

@Component
public class RSSFeeder {

	@Autowired
	private DynamicChannelResolver dynamicChannelResolver;

	
	@SuppressWarnings("unchecked")
	public void run(String url, HtmlParser htmlParser){
		
		PollableChannel feedChannel    = null;
		try {
			ChannelManagement channelManagement = dynamicChannelResolver.resolve(url);
			feedChannel    = (PollableChannel) channelManagement.channel;
			channelManagement.start();

			System.out.println("----------------------------------------------------------");
			System.out.println(url);
			System.out.println("----------------------------------------------------------");

			Message<SyndEntry> message;
			while( (message = (Message<SyndEntry>)feedChannel.receive()) != null){
				
				System.out.println("----------------------------------------------------------");
				System.out.println(url);
				
				SyndEntry entry = (SyndEntry)message.getPayload();
				printSyndEntry(entry);
				Article article  = htmlParser.search(entry.getUri(), "content");
				System.out.println(article);
				System.out.println("----------------------------------------------------------");
			}

		} catch(Exception e ){
			System.out.println(e);
		} 
		finally {
			System.out.println("finally");
		}
	}


	private void printSyndEntry(SyndEntry entry){
		StringBuilder sb = new StringBuilder();
		sb.append("Author        : "+entry.getAuthor()+"\n");
		sb.append("PublishedDate : "+entry.getPublishedDate()+"\n");
		sb.append("Title         : "+entry.getTitle()+"\n");
		sb.append("Uri           : "+entry.getUri());
		System.out.println(sb.toString());
	}

//	private void printSyndEntryDetail(SyndEntry entry){
//		StringBuilder sb = new StringBuilder();
//		sb.append("Author        : "+entry.getAuthor()+"\n");
//		sb.append("PublishedDate : "+entry.getPublishedDate()+"\n");
//		sb.append("Title         : "+entry.getTitle()+"\n");
//		sb.append("Link          : "+entry.getLink()+"\n");
//		sb.append("Uri           : "+entry.getUri()+"\n");
//		sb.append("Contents      : "+entry.getContents()+"\n");
//		sb.append("Comments      : "+entry.getComments()+"\n");
//
//		List<SyndCategory> category = entry.getCategories();
//		for(SyndCategory syndCategory : category){
//			sb.append("categories      : "+syndCategory.getName()+"\n");
//		}
//		List<SyndPerson> contributors = entry.getContributors();
//		for(SyndPerson syndPerson : contributors){	
//			sb.append("SyndPersonName  : "+syndPerson.getName()+"\n");
//			sb.append("SyndPersonEmail : "+syndPerson.getEmail()+"\n");
//		}
//		sb.append("Description"+entry.getDescription());
//		System.out.println(sb.toString());
//	}





}
