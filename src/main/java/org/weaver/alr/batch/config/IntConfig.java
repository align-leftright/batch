package org.weaver.alr.batch.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.messaging.PollableChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.ErrorHandler;


@Configuration
public class IntConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(IntConfig.class);
	
	public static URL feedUrl;
	public static String feedName;
	
	@Bean
	@InboundChannelAdapter(channel="myChannel", poller = @Poller(fixedDelay = "1000", maxMessagesPerPoll = "100"))
	public FeedEntryMessageSource feedMessageSource(){
		FeedEntryMessageSource feedEntryMessageSource = new FeedEntryMessageSource(feedUrl, feedName);
		feedEntryMessageSource.setMetadataStore(messageStore());
		feedEntryMessageSource.afterPropertiesSet();
		
		return feedEntryMessageSource;
	}
	
	@Bean(name="myChannel")
	public PollableChannel channel(){
		return new PriorityChannel();
	}
	
	@Bean
	public PropertiesPersistingMetadataStore messageStore(){
		PropertiesPersistingMetadataStore ms = new PropertiesPersistingMetadataStore();
		ms.setBaseDirectory("./meta/");
		ms.setFileName(feedName);
		return ms;
	}
	
	@Bean
	public SourcePollingChannelAdapter channelAdapter() throws MalformedURLException{
		SourcePollingChannelAdapter ca = new SourcePollingChannelAdapter();
		
		ca.setOutputChannel(channel());
		ca.setSource(feedMessageSource());
		ca.setErrorHandler(new ErrorHandler() {
			public void handleError(Throwable t) {
				logger.equals("error : "+t.getMessage());
			}
		});
		ca.setTaskScheduler(taskScheduler());
		
		return ca;
	}
	
	
	@Bean
	ThreadPoolTaskScheduler taskScheduler(){
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		return taskScheduler;
	}
	

}
