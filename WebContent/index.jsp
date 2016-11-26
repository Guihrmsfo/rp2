<%@ page import="website.WebSite" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String[] availableDates = WebSite.getAvailableDates();
    pageContext.setAttribute("availableDates", availableDates);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trending Topics</title>
</head>
<body>
	<h1>Trending Topics</h1>
	
	<h2>Choose a Date:</h2>
	<form action="${pageContext.request.contextPath}/trends" method="get">
		<select name="date">
			<c:forEach items="${availableDates}" var="individual">
				<option value="${individual}">${individual}</option>
			</c:forEach>
		</select>
    	<input type="submit" name="submit" value="Choose" />
	</form>
</body>
</html>