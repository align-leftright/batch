package org.weaver.alr.batch;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.weaver.alr.batch.common.util.ImageReaderUtil;
import org.weaver.alr.batch.common.util.FileUtil;

public class ImageTest {

	private File f;
	
	@Test
	public void test(){

		URL url;
		try {
			String src = "http://img.hani.co.kr/imgdb/resize/2016/0630/146716353398_20160630.JPG";
			url = new URL(src);
			f = FileUtil.creatTempFile(url);
			System.out.println(f.getAbsolutePath());
			FileUtils.copyURLToFile(url, f);
			System.out.println(url);
			
			BufferedImage image = ImageReaderUtil.readImage(f);
			int type = image.getType();
			System.out.println("type : "+type);

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			f.delete();
		}

	}











}
