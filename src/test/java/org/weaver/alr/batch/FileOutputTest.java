package org.weaver.alr.batch;

import org.junit.Test;
import org.weaver.alr.batch.metadata.News;
import org.weaver.alr.batch.output.impl.FileOutput;

public class FileOutputTest {
	@Test
	public void test(){
		
		FileOutput fout = new FileOutput();
		News news = new News();
		news.setChannel("jiho");
		fout.process("jiho", news);
		
	}

}
