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
		Return to trend selection </a>
	<h1>Trending Topics</h1>
	<h2>${trend}</h2>
	<a href="${url}\ontology.owl" target="_blank">Ontology</a>
	<BR>
	<a href="${url}\tweets.json" target="_blank">Tweets</a>
	<BR>
	<a href="${url}\words.txt" target="_blank">Words</a>
	<BR>
	<div class="tagcloud02">
		<h2>Tag Cloud</h2>
		<ul>
			<c:forEach items="${tags}" var="tag">
				<li><a style="position: relative; font-size: ${tag.weight}px;">
						${tag.name} </a></li>
			</c:forEach>
		</ul>
	</div>
</body>

<style>
/*	
	Reference: https://codepen.io/nxworld/pen/ByGGPj 
*/
div {
	width: 480px;
	margin: 5em auto 2.5em;
}

div:first-child {
	margin-top: 3em;
}

.tagcloud02 ul {
	margin: 0;
	padding: 0;
	list-style: none;
	overflow: hidden;
}

.tagcloud02 ul li {
	float: left;
	width: 150px;
	margin: 0 10px 10px 0;
	padding: 0;
}

.tagcloud02 ul li a {
	display: block;
	width: 100%;
	height: 32px;
	line-height: 32px;
	padding: 0 1em;
	background-color: #fff;
	border: 1px solid #aaa;
	border-radius: 3px;
	white-space: nowrap;
	text-overflow: ellipsis;
	overflow: hidden;
	color: #333;
	font-size: 13px;
	text-decoration: none;
	-webkit-transition: .2s;
	transition: .2s;
	-webkit-box-sizing: border-box;
	box-sizing: border-box;
}

.tagcloud02 ul li a:hover {
	background-color: #3498db;
	border: 1px solid #3498db;
	color: #fff;
}

ne-block






;
margin






:



 



0
.3em



 



.3em



 



0;
padding






:



 



0;
}
.tagcloud01 ul li div {
	display: inline-block;
	max-width: 100px;
	height: 28px;
	line-height: 28px;
	padding: 0 1em;
	background-color: #fff;
	border: 1px solid #aaa;
	border-radius: 3px;
	white-space: nowrap;
	text-overflow: ellipsis;
	overflow: hidden;
	color: #333;
	font-size: 13px;
	text-decoration: none;
	-webkit-transition: .2s;
	transition: .2s;
}

.tagcloud01 ul li div:hover {
	background-color: #3498db;
	border: 1px solid #3498db;
	color: #fff;
}
</style>

</html>

