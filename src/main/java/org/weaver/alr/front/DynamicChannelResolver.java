package org.weaver.alr.front;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;


@Component
public class DynamicChannelResolver 
{
	
	public static final int MAX_CACHE_SIZE = 999999;

	private static String CONFIG_PATH = "pollableChannelFeedContext.xml";

	private final Map<ChannelManagement, ConfigurableApplicationContext> contexts = new HashMap<ChannelManagement, ConfigurableApplicationContext>();

	private final LinkedHashMap<String, ChannelManagement> channelManagements = new LinkedHashMap<String, ChannelManagement>() {

		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Entry<String, ChannelManagement> eldest) {

			//This returning true means the least recently used
			//channel and its application context will be closed and removed
			boolean remove = size() > MAX_CACHE_SIZE;
			if(remove) {
				ChannelManagement channel = eldest.getValue();
				ConfigurableApplicationContext ctx = contexts.get(channel);
				if(ctx != null) { //shouldn't be null ideally
					ctx.close();
					contexts.remove(channel);
				}
			}
			return remove;
		}
	};

	public ChannelManagement resolve(String url) {
		ChannelManagement channelManagement = this.channelManagements.get(url);
		if (channelManagement == null) {
			channelManagement = createChannelManagement(url);
		}
		return channelManagement;
	}

	private synchronized ChannelManagement createChannelManagement(String url) {
		ChannelManagement myChannels = this.channelManagements.get(url);
		if (myChannels == null) {
			this.setEnvironmentForUrl(url);
			ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(CONFIG_PATH);
			ctx.refresh();

			MessageChannel channel = ctx.getBean("myChannel", PollableChannel.class);
			MessageChannel controlChannel = ctx.getBean("controlChannel", MessageChannel.class);
			
			myChannels = new ChannelManagement(controlChannel, channel, url, null, null);
			
			this.channelManagements.put(url, myChannels);
			//Will works as the same reference is presented always
			this.contexts.put(myChannels, ctx);

		}
		return myChannels;
	}

	private void setEnvironmentForUrl(String url) {
		System.setProperty("url", url);
	}


	public class ChannelManagement{

		private MessageChannel controlChannel;
		public MessageChannel channel;

		String url;
		String textSearchKey;
		String imageSearchKey;
		
		ChannelManagement(MessageChannel controlChannel, MessageChannel channel, String url, String textSearchKey, String imageSearchKey){
			this.controlChannel=controlChannel;
			this.channel=channel;
			this.url=url;
			this.textSearchKey=textSearchKey;
			this.imageSearchKey=imageSearchKey;
		}

		public void start(){
			this.controlChannel.send(new GenericMessage<String>("@jihoFeedAdapter.start()"));
		}

		public void stop(){
			this.controlChannel.send(new GenericMessage<String>("@jihoFeedAdapter.stop()"));
		}

		public String getUrl() {
			return url;
		}

		public String getTextSearchKey() {
			return textSearchKey;
		}

		public String getImageSearchKey() {
			return imageSearchKey;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public void setTextSearchKey(String textSearchKey) {
			this.textSearchKey = textSearchKey;
		}

		public void setImageSearchKey(String imageSearchKey) {
			this.imageSearchKey = imageSearchKey;
		}

	}

}
