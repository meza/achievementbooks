package com.stateshifterlabs.achievementbooks;

import java.io.UnsupportedEncodingException;

public class UTF8Utils {
	public static String utf8String(String... strs) {
		try {
			return new String(String.join("", strs).getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return String.join("", strs);
		}
	}
}
