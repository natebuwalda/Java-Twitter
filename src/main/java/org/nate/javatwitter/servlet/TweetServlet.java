package org.nate.javatwitter.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nate.javatwitter.model.Tweet;
import org.nate.javatwitter.repository.TweetStore;
import org.nate.javatwitter.repository.UserStore;


public class TweetServlet extends HttpServlet {

	private static final String YOURTWEET = "yourtweet";
	private static final String USERNAME = "username";
	private static final String TWEET_FORM = "tweetForm";
	private static final String NO_USERNAME = "You must tell us who you are";
	private static final String NO_TWEET = "You did not type anything to tweet about";
	private static final String TWEET_TOO_LONG = "Your tweet can only be 140 characters";
	private static final String SUCCESS = "Your tweet was posted";
	private static final int TWEETS_TO_SHOW = 10;
	private static final long serialVersionUID = 1L;

	private TweetStore tweetStore = null;
	private UserStore userStore = null;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.tweetStore = (TweetStore) getServletContext().getAttribute(TweetStore.TWEET_STORE_KEY);
		this.userStore = (UserStore) getServletContext().getAttribute(UserStore.USER_STORE_KEY);
		if (this.tweetStore == null) {
			this.tweetStore = new TweetStore();
			getServletContext().setAttribute(TweetStore.TWEET_STORE_KEY, this.tweetStore);
		}
		if (this.userStore == null) {
			this.userStore = new UserStore();	
			getServletContext().setAttribute(UserStore.USER_STORE_KEY, this.userStore);
		}
	}

    // url = /JavaTwitter/servlet/tweet RM.GET
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute(USERNAME);
		if (username == null) {
			username = request.getParameter(USERNAME);
		}
		
		TweetForm tweetForm = null;
		if (username == null) {
			tweetForm = fetchTweetsToShow();
		} else {
			List<String> friends = userStore.getFriends(username);
			tweetForm = fetchTweetsToShow(friends);
		}
		request.setAttribute(USERNAME, username);
		request.setAttribute(TWEET_FORM, tweetForm);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/servlet/tweet.jsp");
		dispatcher.forward(request, response);
	}
	
	// url = /JavaTwitter/servlet/tweet RM.POST
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute(USERNAME);	
		if (username == null) {
			username = request.getParameter(USERNAME);
		} 
		
		String entryMessage = null;
		List<String> errorMessages = new ArrayList<String>();
		String tweetMessage = request.getParameter(YOURTWEET);
		if (username == null || username.isEmpty()) {
			errorMessages.add(NO_USERNAME);
		} else if (tweetMessage == null || tweetMessage.isEmpty()) {
			errorMessages.add(NO_TWEET);
		} else if (tweetMessage.length() > 140) {
			errorMessages.add(TWEET_TOO_LONG);
		} else {
			Tweet newTweet = new Tweet();
			newTweet.setTweet(tweetMessage);
			newTweet.setUser(username);
			newTweet.setTweetDate(new Date(System.currentTimeMillis()));
			tweetStore.put(newTweet);
			userStore.put(username);
			
			entryMessage = SUCCESS;
			tweetMessage = "";
			session.setAttribute(USERNAME, username);
		}
		
		TweetForm tweetForm = fetchTweetsToShow(userStore.getFriends(username));
		tweetForm.setYourTweet(tweetMessage);
		tweetForm.getErrorMessages().addAll(errorMessages);
		tweetForm.setEntryMessage(entryMessage);
		request.setAttribute(USERNAME, username);
		request.setAttribute(TWEET_FORM, tweetForm);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/servlet/tweet.jsp");
		dispatcher.forward(request, response);
	}

	private TweetForm fetchTweetsToShow(List<String> friends) {
		List<Tweet> tweets = tweetStore.getLastForUsers(TWEETS_TO_SHOW, friends);
		TweetForm tweetForm = new TweetForm();
		tweetForm.getTweets().addAll(tweets);
		return tweetForm;
	}
	
	private TweetForm fetchTweetsToShow() {
		List<Tweet> tweets = tweetStore.getLast(TWEETS_TO_SHOW); 
		TweetForm tweetForm = new TweetForm();
		tweetForm.getTweets().addAll(tweets);
		return tweetForm;
	}
	
}
