package org.weaver.alr.batch;


import org.springframework.context.ApplicationContext;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.weaver.alr.batch.config.ApplicationContextBuilder;

import com.rometools.rome.feed.synd.SyndEntry;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes={org.weaver.alr.batch.config.ContextConfig.class})
public class JihoTest {

//	public void test(){
//		createFeed("javascript", "http://feeds.feedburner.com/javarticles/ieht");
//	}
	
	public static void main(String[] args){
		System.setProperty("http.proxyHost", "168.219.61.252");
		System.setProperty("http.proxyPort", "8080");
		System.setProperty("https.proxyHost", "168.219.61.252");
		System.setProperty("https.proxyPort", "8080");
		System.out.println("main--------------------------------------------");
		
		createFeed("youtube", "https://www.youtube.com/feeds/videos.xml?channel_id=UC78PMQprrZTbU0IlMDsYZPw");
	}
	
	private static void createFeed(String name, String url){
		System.out.println("createFeed");
		System.out.println(name);
		System.out.println(url);
		
		ApplicationContext conext = ApplicationContextBuilder.build(name, url);
		testContext(conext);
		
		PollableChannel feedChannel = conext.getBean(PollableChannel.class);
		PropertiesPersistingMetadataStore ms = conext.getBean(PropertiesPersistingMetadataStore.class);
		
		try {
			Message<SyndEntry> message;
			
			int count=0;
			while((message = (Message<SyndEntry>) feedChannel.receive()) != null){
				SyndEntry entry = message.getPayload();
				System.out.println(name+" : "+entry.getPublishedDate() + " - " + entry.getTitle());
				System.out.println(entry);
				System.out.println("--------------------------");
				ms.flush();
				if(count++ == 10){
					break;
				}
			}
		} finally {
			System.out.println("finish");
			ms.flush();
		}
	}



	private static void testContext(ApplicationContext context){
		System.out.println("----------------------------------------------------------------");

		System.out.println("webApplicationContext.getBeanDefinitionNames()");
		int count=0;
		for(String name : context.getBeanDefinitionNames()){
			System.out.println(count + " "+name);
			count++;
		}

		System.out.println("----------------------------------------------------------------");

	}

}
