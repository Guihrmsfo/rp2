<%@ page import="website.WebSite"%>
<%@ page import="twitter4j.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trending Topics</title>
</head>
<body>
	<a href="${pageContext.request.contextPath}/trends?date=${date}">
		Return to trend selection
	</a>
	<h1>Trending Topics</h1>
	<h2>${trend}</h2>
</body>
</html>