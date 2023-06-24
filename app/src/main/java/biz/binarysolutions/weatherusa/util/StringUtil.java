package biz.binarysolutions.weatherusa.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 
 *
 */
public class StringUtil {

	/**
	 *
	 * @param s
	 * @return
	 */
	private static String capitalizeWord(String s) {
		
		if (s.length() == 0) {
			return s;
		} else {
			return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
		}
	}

	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String getString(InputStream is) throws IOException {
		
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder  sb     = new StringBuilder();
	    
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	    	sb.append(line + "\n");
	    }
	
	    return sb.toString();
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static InputStream getInputStream(String s) throws UnsupportedEncodingException {
		
		return new ByteArrayInputStream(s.getBytes("UTF-8"));
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String capitalize(String string) {
		
		StringBuffer sb = new StringBuffer();
		
		String[] words = string.split(" ");
		for (int i = 0; i < words.length; i++) {
			sb.append(capitalizeWord(words[i]));
			sb.append(" ");
		}
		
		return sb.toString().trim();
	}	
}
