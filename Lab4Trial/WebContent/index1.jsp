<%@page import="edu.asupoly.ser422.lab4.model.*"%>
<%@page import="edu.asupoly.ser422.lab4.dao.*"%>
<h2>NEW News</h2>
<%
	NewsItemBean[] news = NewsDAOFactory.getTheDAO().getNews();
	for (int i = 0; i < news.length; i++) {
		out.println("<a href=\"ViewStory.jsp?storyid=" + news[i].getItemId() + "\">" + news[i].getItemTitle()
				+ "</a><br />");
	}
%>
<br />
<table border="0">
	<tr>
		<td><a href="./about.html">About</a></td>
		<td><a href="./login.html">Login</a></td>
	</tr>
</table>