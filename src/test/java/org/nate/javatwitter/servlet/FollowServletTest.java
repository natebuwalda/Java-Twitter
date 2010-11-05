package org.nate.javatwitter.servlet;


import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.nate.javatwitter.repository.UserStore;

@RunWith(MockitoJUnitRunner.class)
public class FollowServletTest {

	private FollowServlet servlet;
	
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
	private UserStore userStore;
	
	@Before
	public void setUp() throws Exception {
		servlet = new FollowServlet();
		Mockito.when(config.getServletContext()).thenReturn(context);
		Mockito.when(context.getAttribute(UserStore.USER_STORE_KEY)).thenReturn(userStore);
		servlet.init(config);
	}
	
	@Test
	public void testGet() throws Exception {
		Mockito.when(context.getRequestDispatcher("/WEB-INF/views/servlet/follow.jsp")).thenReturn(dispatcher);
		Mockito.when(userStore.getAllUsers()).thenReturn(Arrays.asList("Joe", "Tom", "Dick", "Harry"));
		Mockito.when(userStore.getFriends("nbuwalda")).thenReturn(Arrays.asList("Tom"));
		Mockito.when(request.getParameter("username")).thenReturn("nbuwalda");
		Mockito.when(request.getParameter("target")).thenReturn("Joe");
		
		servlet.doGet(request, response);
		
		Mockito.verify(userStore).addFriend("nbuwalda", "Joe");
		Mockito.verify(request).setAttribute("username", "nbuwalda");
		Mockito.verify(request).setAttribute("newFriend", "Joe");
		Mockito.verify(request).setAttribute("otherUsers", Arrays.asList("Dick", "Harry"));
		Mockito.verify(dispatcher).forward(request, response);
	}
	
	@Test
	public void testPost() throws Exception {
		Mockito.when(context.getRequestDispatcher("/WEB-INF/views/servlet/follow.jsp")).thenReturn(dispatcher);
		
		servlet.doPost(request, response);
		
		Mockito.verify(dispatcher).forward(request, response);
	}
}
