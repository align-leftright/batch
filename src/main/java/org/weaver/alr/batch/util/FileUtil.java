package org.weaver.alr.batch.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {


	public static String readFile(String classpath){

		BufferedReader br = null;
		StringBuilder sb  = new StringBuilder();

		try {
			
			File file = new File(ClassLoader.getSystemClassLoader().getResource(classpath).getFile());
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

}
