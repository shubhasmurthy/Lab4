<html>
<head>
<title>Login to the Web News Application</title>
<body>
<h2>NEW News</h2>
	<form method="post" action="controller">
		User ID: <input type="text" name="userid" size="25" /> 
		Password: <input
			type="password" name="passwd" size="25" /><br />
		<p></p>
		<input type="submit" name="action" value="Login" /> 
		<input type="reset" value="Reset" />
		<input type="hidden" name="role" value="<%= request.getParameter("role") %>">
			<br /> <br /><br />
		<a href="controller">Go back to news</a>
	</form>
</body>
</html>
