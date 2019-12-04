<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.ivia.dao.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>User List</title>
<script>
	var xreq;
	if (window.XMLHttpRequest) {
		xreq = new XMLHttpRequest();
	} else {
		xreq = new ActiveXObject("Michosoft.XMLHTTP")
	}
	xreq.onreadystatechange = function() {
		document.getElementById("showtext").innerHTML = xreq.responseText;
	}
	xreq.open("get", "getUsers", "true");
	xreq.send();
</script>
</head>
<body>
	<div align="center">
		<h1>User List</h1>
		<a class="item" href="<%=request.getContextPath()%>/createUser.jsp">create User</a>
		<div id="showtext"></div>
	</div>
</body>
</html>