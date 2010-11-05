package org.nate.javatwitter.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.nate.javatwitter.model.Tweet;
import org.nate.javatwitter.repository.TweetStore;
import org.nate.javatwitter.repository.UserStore;

@RunWith(MockitoJUnitRunner.class)
public class TweetServletTest {

	private TweetServlet servlet;
	
	@Mock
	private ServletConfig config;
	@Mock
	private ServletContext context;
	@Mock
	private RequestDispatcher dispatcher;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private HttpSession session;
	@Mock
	private TweetStore tweetStore;
	@Mock
	private UserStore userStore;
	
	@Before
	public void setUp() throws Exception {
		servlet = new TweetServlet();
		Mockito.when(config.getServletContext()).thenReturn(context);
		Mockito.when(context.getAttribute(TweetStore.TWEET_STORE_KEY)).thenReturn(tweetStore);
		Mockito.when(context.getAttribute(UserStore.USER_STORE_KEY)).thenReturn(userStore);
		Mockito.when(request.getSession()).thenReturn(session);
		Mockito.when(request.getSession(false)).thenReturn(null);
		Mockito.when(request.getSession(true)).thenReturn(session);
		servlet.init(config);
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() throws ServletException, IOException {
		ArgumentCaptor<TweetForm> tfCaptor = ArgumentCaptor.forClass(TweetForm.class);
		Mockito.when(context.getRequestDispatcher("/WEB-INF/views/servlet/tweet.jsp")).thenReturn(dispatcher);
		
		Tweet tweet = new Tweet();
		tweet.setId(1L);
		tweet.setTweet("My test tweet");
		tweet.setUser("testuser");
		tweet.setTweetDate(new Date(System.currentTimeMillis()));
		Mockito.when(tweetStore.getLast(10)).thenReturn(Arrays.asList(tweet));
		
		servlet.doGet(request, response);
		
		Mockito.verify(request).setAttribute(Mockito.eq("tweetForm"), tfCaptor.capture());
		TweetForm populatedForm = tfCaptor.getValue();
		Assert.assertEquals(tweet, populatedForm.getTweets().get(0));
		
		Mockito.verify(dispatcher).forward(request, response);
	}

	@Test
	public void testDoGetUserAlreadyInSession() throws ServletException, IOException {
		ArgumentCaptor<TweetForm> tfCaptor = ArgumentCaptor.forClass(TweetForm.class);
		Mockito.when(context.getRequestDispatcher("/WEB-INF/views/servlet/tweet.jsp")).thenReturn(dispatcher);
		Mockito.when(session.getAttribute("username")).thenReturn("nbuwalda");
		Mockito.when(userStore.getFriends("nbuwalda")).thenReturn(Arrays.asList("nbuwalda"));
		
		Tweet tweet = new Tweet();
		tweet.setId(1L);
		tweet.setTweet("My test tweet");
		tweet.setUser("testuser");
		tweet.setTweetDate(new Date(System.currentTimeMillis()));
		Mockito.when(tweetStore.getLastForUsers(10, Arrays.asList("nbuwalda"))).thenReturn(Arrays.asList(tweet));
		
		servlet.doGet(request, response);
		
		Mockito.verify(request).setAttribute(Mockito.eq("tweetForm"), tfCaptor.capture());
		TweetForm populatedForm = tfCaptor.getValue();
		Assert.assertEquals(tweet, populatedForm.getTweets().get(0));
		
		Mockito.verify(dispatcher).forward(request, response);
	}
	
	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() throws ServletException, IOException {
		ArgumentCaptor<TweetForm> tfCaptor = ArgumentCaptor.forClass(TweetForm.class);
		Mockito.when(context.getRequestDispatcher("/WEB-INF/views/servlet/tweet.jsp")).thenReturn(dispatcher);
		Mockito.when(request.getParameter("yourtweet")).thenReturn("A properly sized tweet");
		Mockito.when(request.getParameter("username")).thenReturn("nbuwalda");
		Mockito.when(userStore.getFriends("nbuwalda")).thenReturn(Arrays.asList("nbuwalda"));
		
		Tweet tweet = new Tweet();
		tweet.setId(1L);
		tweet.setTweet("A properly sized tweet");
		tweet.setUser("nbuwalda");
		tweet.setTweetDate(new Date(System.currentTimeMillis()));
		Mockito.when(tweetStore.getLastForUsers(10, Arrays.asList("nbuwalda"))).thenReturn(Arrays.asList(tweet));
		
		servlet.doPost(request, response);
		
		Mockito.verify(request).setAttribute(Mockito.eq("tweetForm"), tfCaptor.capture());
		TweetForm populatedForm = tfCaptor.getValue();
		Assert.assertEquals("Your tweet was posted", populatedForm.getEntryMessage());
		Assert.assertEquals(0, populatedForm.getYourTweet().trim().length());
		Assert.assertEquals("A properly sized tweet", populatedForm.getTweets().get(0).getTweet());
		Assert.assertEquals("nbuwalda", populatedForm.getTweets().get(0).getUser());
		Assert.assertNotNull(populatedForm.getTweets().get(0).getTweetDate());
		
		Mockito.verify(tweetStore).put((Tweet) Mockito.any());
		Mockito.verify(userStore).put("nbuwalda");
		Mockito.verify(dispatcher).forward(request, response);
	}
	
	@Test
	public void testDoPostUserAlreadyInSession() throws ServletException, IOException {
		ArgumentCaptor<TweetForm> tfCaptor = ArgumentCaptor.forClass(TweetForm.class);
		Mockito.when(context.getRequestDispatcher("/WEB-INF/views/servlet/tweet.jsp")).thenReturn(dispatcher);
		Mockito.when(request.getParameter("yourtweet")).thenReturn("A properly sized tweet");
		Mockito.when(session.getAttribute("username")).thenReturn("nbuwalda");
		Mockito.when(userStore.getFriends("nbuwalda")).thenReturn(Arrays.asList("nbuwalda"));
		
		Tweet tweet = new Tweet();
		tweet.setId(1L);
		tweet.setTweet("A properly sized tweet");
		tweet.setUser("nbuwalda");
		tweet.setTweetDate(new Date(System.currentTimeMillis()));
		Mockito.when(tweetStore.getLastForUsers(10, Arrays.asList("nbuwalda"))).thenReturn(Arrays.asList(tweet));
		
		servlet.doPost(request, response);
		
		Mockito.verify(request).setAttribute(Mockito.eq("tweetForm"), tfCaptor.capture());
		TweetForm populatedForm = tfCaptor.getValue();
		Assert.assertEquals("Your tweet was posted", populatedForm.getEntryMessage());
		Assert.assertEquals(0, populatedForm.getYourTweet().trim().length());
		Assert.assertEquals("A properly sized tweet", populatedForm.getTweets().get(0).getTweet());
		Assert.assertEquals("nbuwalda", populatedForm.getTweets().get(0).getUser());
		Assert.assertNotNull(populatedForm.getTweets().get(0).getTweetDate());
		
		Mockito.verify(tweetStore).put((Tweet) Mockito.any());
		Mockito.verify(userStore).put("nbuwalda");
		Mockito.verify(dispatcher).forward(request, response);
	}
	
	@Test
	public void testTweetWasTooBig() throws ServletException, IOException {
		ArgumentCaptor<TweetForm> tfCaptor = ArgumentCaptor.forClass(TweetForm.class);
		Mockito.when(context.getRequestDispatcher("/WEB-INF/views/servlet/tweet.jsp")).thenReturn(dispatcher);
		String tooLongTweet = "An incredibly long tweet 11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
		Mockito.when(request.getParameter("yourtweet")).thenReturn(tooLongTweet);
		Mockito.when(request.getParameter("username")).thenReturn("nbuwalda");
		
		servlet.doPost(request, response);
		
		Mockito.verify(request).setAttribute(Mockito.eq("tweetForm"), tfCaptor.capture());
		TweetForm populatedForm = tfCaptor.getValue();
		Assert.assertEquals("Your tweet can only be 140 characters", populatedForm.getErrorMessages().get(0));
		Assert.assertEquals(tooLongTweet, populatedForm.getYourTweet());
		
		Mockito.verify(dispatcher).forward(request, response);
	}

	@Test
	public void testTweetWasEmpty() throws ServletException, IOException {
		ArgumentCaptor<TweetForm> tfCaptor = ArgumentCaptor.forClass(TweetForm.class);
		Mockito.when(context.getRequestDispatcher("/WEB-INF/views/servlet/tweet.jsp")).thenReturn(dispatcher);
		String emptyTweet = "";
		Mockito.when(request.getParameter("yourtweet")).thenReturn(emptyTweet);
		Mockito.when(request.getParameter("username")).thenReturn("nbuwalda");
		
		servlet.doPost(request, response);
		
		Mockito.verify(request).setAttribute(Mockito.eq("tweetForm"), tfCaptor.capture());
		TweetForm populatedForm = tfCaptor.getValue();
		Assert.assertEquals("You did not type anything to tweet about", populatedForm.getErrorMessages().get(0));
		Assert.assertEquals(emptyTweet, populatedForm.getYourTweet());
		
		Mockito.verify(dispatcher).forward(request, response);
	}
	
	@Test
	public void testNoUsername() throws ServletException, IOException {
		ArgumentCaptor<TweetForm> tfCaptor = ArgumentCaptor.forClass(TweetForm.class);
		Mockito.when(context.getRequestDispatcher("/WEB-INF/views/servlet/tweet.jsp")).thenReturn(dispatcher);
		Mockito.when(request.getParameter("yourtweet")).thenReturn("A properly sized tweet");
		
		servlet.doPost(request, response);
		
		Mockito.verify(request).setAttribute(Mockito.eq("tweetForm"), tfCaptor.capture());
		TweetForm populatedForm = tfCaptor.getValue();
		Assert.assertEquals("You must tell us who you are", populatedForm.getErrorMessages().get(0));
		
		Mockito.verify(dispatcher).forward(request, response);
	}
}
