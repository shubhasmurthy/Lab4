<%@page import="edu.asupoly.ser422.lab4.model.*"%>
<%@page import="edu.asupoly.ser422.lab4.dao.*"%>
<html>
<head>
<title>NEW News Inc</title>
<body>
	<form method="post" action="controller">
	<jsp:include page="Header.jsp" />
<% 
	    String hideIt = "style=\"display: none;\"";
		String uname = (String) request.getSession().getAttribute("userName");
		UserBean user = NewsDAOFactory.getTheDAO().getUser(uname);
	    if(user!=null){
	    	if(user.isSubscriber()){
		    	hideIt = "";
	    	}
	    }
%>
<br />
<br />
<%NewsItemBean newsItem = (NewsItemBean) request.getAttribute("newsItem");
%>
<input type="hidden" name="newsid" value="<%=newsItem.getItemId()%>">
<input type="submit" name="controller?action=addFav" value="Add as favorite"  align="right"/><input type="submit" name="action" value="Remove as favorite"  align="right"/>
<h2><%= newsItem.getItemTitle() %></h2> 
<br />
<%= newsItem.getItemStory() %>
<br />
<br />
<br />
Comments:
<br />
<br />
<table border="0" align="left">
	<%
	if(newsItem.getComments()!=null){
		CommentBean[] comments = newsItem.getComments();
		for(CommentBean c: comments){
		out.println("<tr>");
		out.println("<td>"+c.getUserId()+" : "+"</td>");
		out.println("<td>"+c.getComment()+"</td>");
		out.println("</tr>");
		}
	}
	%>
</table>
<br />
<br />
<br />
<div <%= hideIt%>>
<textarea rows="4" cols="50" name="Comment"></textarea>
<input type="submit" name="action" value="comment" />
</div>

	</form>
</body>
</html>
