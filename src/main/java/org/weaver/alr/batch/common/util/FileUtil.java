package org.weaver.alr.batch.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileUtil {
	
	
	public static String readInputStream(final InputStream is){
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb= new StringBuilder();
		String str;
	
		try {
			while( (str= br.readLine()) != null ){
				sb.append(str);
			}
		} catch (IOException e) {
			return null;
		} 
		
		return sb.toString();
	}

	public static String readFile(final File file){
		BufferedReader br = null;
		StringBuilder sb  = new StringBuilder();

		try {
			br = new BufferedReader(new FileReader(file));
			String str;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	
	public static String readFile(final String classpath){
		return readFile(getFile(classpath));
	}
	
	
	public static File creatTempFile(final URL url) throws IOException{
		File file = File.createTempFile("alr_", "tmp");
		FileUtils.copyURLToFile(url, file);	
		return file;
	}
	
	
	public static File getFile(final String classpath){
		return new File(ClassLoader.getSystemClassLoader().getResource(classpath).getFile());
	}
	
	
	public static List<File> listFilesForFolder(final File folder) { 
		if(folder == null){
			return null;
		}
		
		LinkedList<File> fileList = new LinkedList<File>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	        	fileList.add(fileEntry);
	        }
	    }
	    return fileList;
	}
	
	
	public URL getURL(String name){
		URL url = getClass().getClassLoader().getResource(name);
		return url;
	}
	
	public File getFile(final URL url){
		return new File(url.getFile());
	}
	
	
}
