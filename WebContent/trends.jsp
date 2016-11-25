<%@ page import="website.WebSite"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trending Topics</title>
</head>
<body>
	<a href="${pageContext.request.contextPath}/index.jsp">
		Return to date selection
	</a>
	<h1>Trending Topics</h1>
	<h2>Choose a Trend:</h2>
	<c:forEach items="${trends}" var="trend">
		<a href="${pageContext.request.contextPath}/trend?date=${date}&trend=${trend}">${trend}</a>
		<br>
	</c:forEach>
</body>
</html>