package com.stateshifterlabs.achievementbooks.core.data;

public class DemoAlreadyExistsException extends RuntimeException {


	public DemoAlreadyExistsException() {
		super("A demo.json already exists in config/achievementbooks");
	}
}
