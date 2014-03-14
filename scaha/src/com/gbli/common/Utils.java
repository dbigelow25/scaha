/**
 * 
 */
package com.gbli.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;

import com.scaha.objects.UsaHockeyRegistration;


/**
 * @author dbigelow
 *
 */
public class Utils {
	
	public static void main (String[] args) throws Exception  {
		
		System.out.println(getRandomString());
		
	}
	
	public static String properCase(String s) {
		Pattern p = Pattern.compile("(^|\\W)([a-z])");
		Matcher m = p.matcher(s.toLowerCase());
		StringBuffer sb = new StringBuffer(s.length());
		while(m.find()) {
			m.appendReplacement(sb, m.group(1) + m.group(2).toUpperCase() );
		}
		m.appendTail(sb);
		return sb.toString();
	}

	
	
	public static String getRandomString() {
		return RandomStringUtils.random(4, true, true).toUpperCase();
	}
	
}
