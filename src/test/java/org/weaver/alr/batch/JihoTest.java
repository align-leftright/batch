package org.weaver.alr.batch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.weaver.alr.batch.config.FeedInfo;
import org.weaver.alr.batch.config.IntConfig;

import com.rometools.rome.feed.synd.SyndEntry;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes={org.weaver.alr.batch.config.ContextConfig.class})
public class JihoTest {

	public void test(){
		createFeed("javascript", "http://feeds.feedburner.com/javarticles/ieht");
	}
	
	public static void main(String[] args){
		System.setProperty("http.proxyHost", "168.219.61.252");
		System.setProperty("http.proxyPort", "8080");
		System.out.println("main--------------------------------------------");
		
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				createFeed("javascript", "http://feeds.feedburner.com/javarticles/ieht");
			}
		});
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				createFeed("hani", "http://www.hani.co.kr/rss/politics/");
			}
		});
		System.out.println("---------------------------------");

		try {
			t1.start();
			t2.start();
			Thread.sleep(1000*10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	private static void createFeed(String name, String url){
		System.out.println("createFeed");
		System.out.println(name);
		System.out.println(url);
		
		ApplicationContext conext = initApplicationContext(name, url);
		PollableChannel feedChannel = conext.getBean(PollableChannel.class);
		PropertiesPersistingMetadataStore ms = conext.getBean(PropertiesPersistingMetadataStore.class);
		
		try {
			Message<SyndEntry> message;
			
			int count=0;
			while((message = (Message<SyndEntry>) feedChannel.receive()) != null){
				SyndEntry entry = message.getPayload();
				System.out.println(name+" : "+entry.getPublishedDate() + " - " + entry.getTitle());
				ms.flush();
//				if(count++ == 100){
//					break;
//				}
				
			}
		} finally {
			System.out.println("finish");
			ms.flush();
		}
	}



	private static synchronized ApplicationContext initApplicationContext(String name, String url){
		IntConfig.queue.add(new FeedInfo(name, url));
		ApplicationContext conext = new AnnotationConfigApplicationContext(IntConfig.class);
		IntConfig.queue.remove();
		return conext;
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
