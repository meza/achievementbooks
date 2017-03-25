package com.stateshifterlabs.achievementbooks.data;

import com.stateshifterlabs.achievementbooks.UTF8Utils;

import java.io.File;

public class JsonParseError extends java.lang.RuntimeException {

	private String message;
	private File file;

	public JsonParseError(String message, File file) {
		super(String.format(
				"\n\n" +
				"*****************************  THIS IS WHY YOUR GAME DOESN'T START  *******************************\n\n"+
				"\t%s\n" +
				"\tFile: %s\n"+
				"\n**************************************************************************************************\n",
				message, file.getAbsolutePath()));
		this.message = message;
		this.file = file;
	}

	public String simpleMessage() {
		return String.format(
				UTF8Utils.utf8String("§e%s in file: %s§r"),
				message, file.getAbsolutePath());
	}
}
