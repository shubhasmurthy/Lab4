<html>
<head>
<title>Oops no user found</title>
</head>
<body>
	<form method="post" action="controller">
		Sorry no such user found!
		<p></p>
		<input type="hidden" name="userid" value="<%= request.getParameter("userid")%>">
		<input type="hidden" name="passwd" value="<%= request.getParameter("passwd")%>">
		Make me <input type="submit" name="action" value="A new subscriber" />
		<input type="submit" name="action" value="A new reporter" /> <br />
		<br /> <a href="login.html">Cancel</a>
		<a href="controller">Go back to news</a>
	</form>
</body>
</html>
