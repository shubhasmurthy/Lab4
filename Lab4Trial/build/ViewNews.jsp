<%@page import="edu.asupoly.ser422.lab4.model.*"%>
<%@page import="edu.asupoly.ser422.lab4.dao.*"%>
<h2>NEW News</h2>
<%
	String uname = (String) request.getSession().getAttribute("userName");
%>
Welcome 
<% if(uname!=null)
	out.println(uname);
%>!
<br /><br /><br />
<%
	UserBean user = NewsDAOFactory.getTheDAO().getUser(uname);
	if (user != null) {
		NewsItemBean[] news = NewsDAOFactory.getTheDAO().getNews(user);
		for (int i = 0; i < news.length; i++) {
			out.println("<a href=\"ViewStory.jsp?storyid=" + news[i].getItemId() + "\">"
					+ news[i].getItemTitle() + "</a><br />");
		}
	}
%>
<br />
<table border="0">
	<tr>
		<td><a href="./about.html">About</a></td>
		<td><a href="./login.html">Login</a></td>
	</tr>
</table>