package org.nate.javatwitter.servlet;

import java.util.ArrayList;
import java.util.List;

import org.nate.javatwitter.model.Tweet;

public class TweetForm {

	private String yourTweet;
	private String entryMessage;
	private List<String> errorMessages = new ArrayList<String>();
	private List<Tweet> tweets = new ArrayList<Tweet>();
	
	public String getYourTweet() {
		return yourTweet;
	}

	public void setYourTweet(String yourTweet) {
		this.yourTweet = yourTweet;
	}

	public String getEntryMessage() {
		return this.entryMessage;
	}

	public void setEntryMessage(String entryMessage) {
		this.entryMessage = entryMessage;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public List<Tweet> getTweets() {
		return this.tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}

}
