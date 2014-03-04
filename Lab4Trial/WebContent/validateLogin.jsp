<%@page import="org.omg.CORBA.UserException"%>
<%@page import="edu.asupoly.ser422.lab4.model.UserBean"%>
<%@page import="edu.asupoly.ser422.lab4.dao.*"%>
<html>
<head>
</head>
<body>
<%
String userId = null;
String passwd = null;
String msg = "";
boolean userExists = true;
userId = request.getParameter("userid");
passwd = request.getParameter("passwd");

if (userId == null || userId.length() == 0 || passwd == null || passwd.length() == 0)
	{	msg = "The userID or password cannot be empty";   }
else if (!userId.equals(passwd))
	{	msg = "The userID or password is not valid";   }
// HERE YOU HAVE TO CHECK IF THE USER EXISTS OR NOT!
else {
	UserBean user = NewsDAOFactory.getTheDAO().getUser(userId);
	
	if (user == null) {
		msg = "No such user " + userId;
		userExists = false;
	} else {
		msg = "Successful login, you are User " + user.toString();
	}
}
out.println("<p>Login result " + msg + "</p>");

%>
<br/>
<pre>

At this point you would know whether you had a successful login or not, and
if so have a UserBean representing the existing user. If the login works (same
userid and password), but the user does not exist, then you should ask if a
new user should be created and under which role (subscriber or reporter).
NOTE: You do not have to keep this logic in a JSP. In fact you shouldn't!
</pre>
</body>
</html>