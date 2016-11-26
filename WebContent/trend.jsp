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
	<a href="${url}\ontology.owl" target="_blank">Ontology</a>
	<BR>
	<a href="${url}\tweets.json" target="_blank">Tweets</a>
	<BR>
	<a href="${url}\words.txt" target="_blank">Words</a>
</body>
</html>