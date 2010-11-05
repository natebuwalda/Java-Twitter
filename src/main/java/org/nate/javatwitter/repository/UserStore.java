package org.nate.javatwitter.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserStore {

public static final String USER_STORE_KEY = "UserStoreKey";
	
	private Map<String, List<String>> userStore = new HashMap<String, List<String>>();
	private Map<String, List<String>> followerStore = new HashMap<String, List<String>>();

	public void put(String username) {
		if (userStore.get(username) == null) {
			userStore.put(username, new ArrayList<String>());
			followerStore.put(username, new ArrayList<String>());
		}
	}

	public Integer count() {
		return userStore.size();
	}

	public List<String> getFriends(String username) {
		List<String> friends = userStore.get(username);
		if (friends == null) {
			friends = new ArrayList<String>();
		}
		friends.add(username);
		return friends;
	}

	public List<String> getFollowers(String username) {
		return followerStore.get(username);
	}

	public void addFriend(String username, String friend) {
		if (userStore.get(username) != null
				&& followerStore.get(friend) != null) {
			userStore.get(username).add(friend);
			followerStore.get(friend).add(username);
		} else {
			throw new FriendNotFoundException("You picked a bad friend");
		}
	}

	public List<String> getAllUsers() {
		Set<String> keySet = userStore.keySet();
		List<String> userList = new ArrayList<String>();
		userList.addAll(keySet);
		return userList;
	}

}
