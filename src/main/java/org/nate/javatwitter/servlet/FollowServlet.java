package org.nate.javatwitter.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nate.javatwitter.repository.UserStore;


public class FollowServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private UserStore userStore = null;
    
	public FollowServlet() {
        super();
    }
    
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.userStore = (UserStore) getServletContext().getAttribute(UserStore.USER_STORE_KEY);
		if (this.userStore == null) {
			this.userStore = new UserStore();	
			getServletContext().setAttribute(UserStore.USER_STORE_KEY, this.userStore);
		}
	}

    // url = /JavaTwitter/servlet/follow RM.GET
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String target = request.getParameter("target");
		
		List<String> potentialVictims = new ArrayList<String>();
		potentialVictims.addAll(userStore.getAllUsers());
		List<String> friends = new ArrayList<String>();
		friends.addAll(userStore.getFriends(username));
		potentialVictims.removeAll(friends);
		
		if (target != null) {
			potentialVictims.remove(target);
			userStore.addFriend(username, target);
		}
		
		request.setAttribute("username", username);
		request.setAttribute("newFriend", target);
		request.setAttribute("otherUsers", potentialVictims);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/servlet/follow.jsp");
		dispatcher.forward(request, response);
	}
	
	// url = /JavaTwitter/servlet/follow RM.POST
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/servlet/follow.jsp");
		dispatcher.forward(request, response);
	}

}
