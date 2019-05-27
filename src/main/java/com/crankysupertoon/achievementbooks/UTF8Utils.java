package com.crankysupertoon.achievementbooks;

import java.nio.charset.Charset;

public class UTF8Utils {
	public static String utf8String(String... strs) {
		return new String(String.join("", strs).getBytes(), Charset.defaultCharset());
	}
}
