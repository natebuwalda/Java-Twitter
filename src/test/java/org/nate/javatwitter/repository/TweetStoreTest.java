package org.nate.javatwitter.repository;


import java.util.Arrays;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.nate.javatwitter.model.Tweet;

@RunWith(MockitoJUnitRunner.class)
public class TweetStoreTest {

	private TweetStore store;
	
	@Before
	public void setUp() throws Exception {
		store = new TweetStore();
	}

	@Test
	public void testPut() {
		Tweet tweet = new Tweet();
		tweet.setTweetDate(new Date(System.currentTimeMillis()));
		
		store.put(tweet);
		
		Assert.assertEquals(1, store.count().intValue());
	}
	
	@Test
	public void testGetLastX() {
		Tweet tweet = new Tweet();
		tweet.setTweetDate(new Date(System.currentTimeMillis()));
		tweet.setTweet("First tweet");
		store.put(tweet);
		
		Tweet otherTweet = new Tweet();
		otherTweet.setTweetDate(new Date(System.currentTimeMillis() + 1));
		otherTweet.setTweet("Second tweet");
		store.put(otherTweet);
		
		Tweet anotherTweet = new Tweet();
		anotherTweet.setTweetDate(new Date(System.currentTimeMillis() + 2));
		anotherTweet.setTweet("Third tweet");
		store.put(anotherTweet);
		
		Assert.assertEquals(3, store.count().intValue());
		Assert.assertEquals("Second tweet", store.getLast(2).get(1).getTweet());
	}
	
	@Test
	public void testGetLastXForUser() {
		Tweet tweet = new Tweet();
		tweet.setTweetDate(new Date(System.currentTimeMillis()));
		tweet.setTweet("First tweet");
		tweet.setUser("user1");
		store.put(tweet);
		
		Tweet otherTweet = new Tweet();
		otherTweet.setTweetDate(new Date(System.currentTimeMillis() + 1));
		otherTweet.setTweet("Second tweet");
		otherTweet.setUser("user2");
		store.put(otherTweet);
		
		Tweet anotherTweet = new Tweet();
		anotherTweet.setTweetDate(new Date(System.currentTimeMillis() + 2));
		anotherTweet.setTweet("Third tweet");
		anotherTweet.setUser("user3");
		store.put(anotherTweet);
		
		Assert.assertEquals(3, store.count().intValue());
		Assert.assertEquals(1, store.getLastForUsers(2, Arrays.asList("user2")).size());
		Assert.assertEquals("Second tweet", store.getLastForUsers(2, Arrays.asList("user2")).get(0).getTweet());
	}
}
