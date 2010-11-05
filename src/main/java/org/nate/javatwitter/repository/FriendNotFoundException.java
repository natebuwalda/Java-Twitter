package org.nate.javatwitter.repository;

public class FriendNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FriendNotFoundException() {
		super();
	}

	public FriendNotFoundException(String message) {
		super(message);
	}

}
