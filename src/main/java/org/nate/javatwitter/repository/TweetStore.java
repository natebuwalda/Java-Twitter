package org.nate.javatwitter.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.nate.javatwitter.model.Tweet;

public class TweetStore {
	public static final String TWEET_STORE_KEY = "TweetStoreKey";
	
	private Map<Long, Tweet> timestampStore = new HashMap<Long, Tweet>();

	public void put(Tweet tweet) {
		tweet.setId(new Long(count()));
		timestampStore.put(tweet.getTweetDate().getTime(), tweet);
	}

	public Integer count() {
		return timestampStore.size();
	}

	public List<Tweet> getLast(int requestedTweets) {
		SortedSet<Long> timestamps = new TreeSet<Long>(new Comparator<Long>() {
			public int compare(Long o1, Long o2) {
				return o2.intValue() - o1.intValue();
			}
		});
		timestamps.addAll(timestampStore.keySet());
		
		List<Tweet> requestedLast = new ArrayList<Tweet>();
		int index = 0;
		Iterator<Long> timestampIterator = timestamps.iterator();
		while (index < requestedTweets && timestampIterator.hasNext()) {
			requestedLast.add(timestampStore.get(timestampIterator.next()));
		}
		
		return requestedLast;
	}
	
	public List<Tweet> getLastForUsers(int requestedTweets, List<String> users) {
		SortedSet<Long> timestamps = sortedTweets();
		
		List<Tweet> requestedLast = new ArrayList<Tweet>();
		int index = 0;
		Iterator<Long> timestampIterator = timestamps.iterator();
		while (index < requestedTweets && timestampIterator.hasNext()) {
			Tweet tweet = timestampStore.get(timestampIterator.next());
			if (users.contains(tweet.getUser())) {
				requestedLast.add(tweet);
			}
		}
		
		return requestedLast;
	}

	private SortedSet<Long> sortedTweets() {
		SortedSet<Long> timestamps = new TreeSet<Long>(new Comparator<Long>() {
			public int compare(Long o1, Long o2) {
				return o2.intValue() - o1.intValue();
			}
		});
		timestamps.addAll(timestampStore.keySet());
		return timestamps;
	}
	
	
}
