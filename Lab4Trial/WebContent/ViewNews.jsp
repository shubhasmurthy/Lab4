<%@page import="edu.asupoly.ser422.lab4.model.*"%>
<%@page import="edu.asupoly.ser422.lab4.dao.*"%>
<html>
<head>
<title>NEW News Inc</title>
<body>
	
<h2>NEW News</h2>
<jsp:include page="Header.jsp" />
<%
	UserBean user = (UserBean) request.getSession().getAttribute("user");
%>
Welcome
<%
	if (user != null)
		out.println(user.getUserId());
%>!
<br />
<br />
<br />
<table border="0" align="left">
<%
	NewsItemBean[] news = (NewsItemBean[]) request.getAttribute("newsList");
	for (int i = 0; i < news.length; i++) {
		out.println("<tr>");
		out.println("<td><a href=\"controller?action=ViewStory&newsid=" + news[i].getItemId() + "\">"+news[i].getItemTitle()+"</a></td>");
		if(user!=null){
			if(news[i].getReporterId().equals(user.getUserId())){
			out.println("<td colspan=\"10\"><a href=\"controller?action=edit&newsid=" + news[i].getItemId() + "\">Edit</a></td>");
			out.println("<td colspan=\"10\"><a href=\"controller?action=delete&newsid=" + news[i].getItemId() + "\">Delete</a></td>");
			}
		}
		out.println("</tr>");
		out.println("<tr><td colspan=\"50\"><hr></td></tr>");
	}
%>
</table>
<br />
</body>
</html>