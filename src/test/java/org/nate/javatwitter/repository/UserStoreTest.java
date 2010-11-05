package org.nate.javatwitter.repository;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserStoreTest {

	private UserStore store;
	
	@Before
	public void setUp() throws Exception {
		store = new UserStore();
	}

	
	@Test
	public void testPutNewUser() {
		store.put("nbuwalda");
		
		Assert.assertEquals(1, store.count().intValue());
		Assert.assertEquals(1, store.getFriends("nbuwalda").size());
		Assert.assertEquals(0, store.getFollowers("nbuwalda").size());
	}
	
	@Test
	public void testPutFriends() {
		store.put("nbuwalda");
		store.put("Lenny");
		store.put("Carl");
		
		store.addFriend("nbuwalda", "Lenny");
		store.addFriend("nbuwalda", "Carl");
		store.addFriend("Carl", "Lenny");
		
		Assert.assertEquals(3, store.count().intValue());
		Assert.assertEquals(3, store.getFriends("nbuwalda").size());
		Assert.assertEquals(2, store.getFollowers("Lenny").size());
		Assert.assertEquals(1, store.getFollowers("Carl").size());
	}
	
	@Test(expected=FriendNotFoundException.class)
	public void testPutFriendButHeDoesntExist() {
		store.put("nbuwalda");
		store.put("Lenny");
		
		store.addFriend("nbuwalda", "Lenny");
		store.addFriend("nbuwalda", "Carl");
		
		Assert.assertEquals(2, store.count().intValue());
		Assert.assertEquals(1, store.getFriends("nbuwalda").size());
		Assert.assertEquals(1, store.getFollowers("Lenny").size());
	}
	
	@Test
	public void testGetAllUsers() {
		store.put("nbuwalda");
		store.put("Lenny");
		store.put("Carl");
			
		Assert.assertEquals(3, store.count().intValue());
		Assert.assertEquals(3, store.getAllUsers().size());
	}
}
