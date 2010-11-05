<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="false"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>JavaTwitter</title>
	</head>
	<body>
		<div class="page">
			<div id="header">
				<div id="title">
					<h1>Java Twitter</h1>
				</div>
			</div>
			<div id="main">
				<c:if test="${(!empty tweetForm.errorMessages)}">
					<div class="error">
						<p>Please correct the following errors:</p>
						<ol>
							<c:forEach items="${tweetForm.errorMessages}" var="error">
								<li><c:out value="${error}"/></li>
							</c:forEach>
						</ol>
					</div>
				</c:if>
				
				<c:if test="${tweetForm.entryMessage != null}">
					<p>Previous action: <c:out value="${tweetForm.entryMessage}" /></p>
				</c:if>
			
				<fieldset><legend>Go ahead and tweet!</legend> 
					<form action="tweet" method="POST">
						<table>
							<tr>
								<td>You:</td>
								<c:if test="${empty username}">
									<td><input type="text" id="username" name="username" size="25"/></td>
								</c:if>
								<c:if test="${!empty username}">
									<td><c:out value="${username}" /></td>
								</c:if>
							</tr>
							<tr>
								<td>Your tweet:</td>
								<td><input type="text" id="yourtweet" name="yourtweet" size="140"/></td>
							</tr>
						</table>
						<input type="submit" value="Tweet!" />
					</form>
				</fieldset>
				<c:url value="/servlet/tweet" var="tweetUrl"/>
				<p><a href="<c:out value='${tweetUrl}'/>">Refresh your tweets</a></p>		
				
				<c:if test="${!empty username}">
					<c:url value="/servlet/follow" var="followUrl">
						<c:param name="username" value="${username}"/>
					</c:url>
					<p><a href="<c:out value='${followUrl}'/>">Follow someone</a></p>		
				</c:if>
				
				<c:if test="${!empty tweetForm.tweets}">
					<fieldset><legend>Timeline</legend> 
						<c:forEach items="${tweetForm.tweets}" var="tweet">
							<div id="tweet" style="border: 1px; border-style: solid;">
								User: <c:out value="${tweet.user}"/>
									<c:if test="${(!empty username) && (username != tweet.user)}">
										<c:url value="/servlet/follow" var="followUrl">
											<c:param name="username" value="${username}"/>
											<c:param name="target" value="${tweet.user}"/>
										</c:url>
										<a href="<c:out value='${followUrl}'/>">Follow me!</a>	
									</c:if>
								</br>
								Tweet: <c:out value="${tweet.tweet}"/></br>
								Tweeted on: <c:out value="${tweet.tweetDate}"/>
							</div>
							</br>
						</c:forEach>
					</fieldset>
				</c:if>
			</div>
		</div>
	</body>
</html>