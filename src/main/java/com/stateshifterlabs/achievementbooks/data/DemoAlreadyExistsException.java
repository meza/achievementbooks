package com.stateshifterlabs.achievementbooks.data;

public class DemoAlreadyExistsException extends RuntimeException {


	public DemoAlreadyExistsException() {
		super("A demo.json already exists in config/achievementbooks");
	}
}
