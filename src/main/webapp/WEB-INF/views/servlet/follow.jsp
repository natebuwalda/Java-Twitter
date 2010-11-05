<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="false"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>JavaTwitter - Friends</title>
	</head>
	<body>
		<div class="page">
			<div id="header">
				<div id="title">
					<h1>Java Twitter (Friend Management)</h1>
				</div>
			</div>
			<div id="main">
				<c:if test="${!empty newFriend}">
					<p>Added <c:out value="${newFriend}" /> to your friends list, <c:out value="${username}"/>.</p>
				</c:if>
				<c:url value="/servlet/tweet" var="tweetUrl"/>
				<p><a href="<c:out value='${tweetUrl}'/>">Go back to tweeting</a></p>		
		
				<fieldset><legend>Would you like to follow other people?</legend> 
					<c:forEach items="${otherUsers}" var="otherUser">
						<c:out value="${otherUser}"/>
						<c:url value="/servlet/follow" var="followUrl">
							<c:param name="username" value="${username}"/>
							<c:param name="target" value="${otherUser}"/>
						</c:url>
						<a href="<c:out value='${followUrl}'/>">Follow me!</a>							
						</br>
					</c:forEach>
				</fieldset>
				
			</div>
		</div>
	</body>
</html>