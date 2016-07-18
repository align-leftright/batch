package org.weaver.alr.batch;

import org.weaver.alr.batch.metadata.News;
import org.weaver.alr.batch.output.impl.FileOutput;

public class FileOutputTest {
	
	public static void main(String[] args) throws InterruptedException{
		
		Thread t1 = new Thread(new RunnableTest("t1"));
		Thread t2 = new Thread(new RunnableTest("t2"));
		Thread t3 = new Thread(new RunnableTest("t3"));
		
		t1.start();
		t2.start();
		t3.start();
		
	}
	
}

class RunnableTest implements Runnable{

	String name;
	
	RunnableTest(String name){
		this.name = name;
	}
	
	public void run() {
		
		FileOutput fout = new FileOutput();
		News news = new News();
		news.setChannel(name);
		
		for(int i=0 ; i<1 ; i++){
			fout.process(name, news);
		}
		
		
	}

}