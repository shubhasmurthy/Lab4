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
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		NewsItemBean newsItem = (NewsItemBean) request.getAttribute("newsItem");
		Boolean isFav = (Boolean) request.getAttribute("isFav");
	    if(user!=null){
	    	if(user.isSubscriber()){
		    	hideIt = "";
	    	}else if(user.isReporter() && user.getUserId().equals(newsItem.getReporterId())){
	    		hideIt = "";
	    	}	    	
	    }
	    String addFavBtn, remFavBtn;
	    if(isFav){
	    	addFavBtn = "hidden=true";
	    	remFavBtn = "";
	    }else{
	    	addFavBtn = "";
	    	remFavBtn = "hidden=true";
	    }
	    /* 
	    if(newsItem.isFavorite()){
	    	addFavBtn = "hidden=true";
	    	remFavBtn = "";
	    }else{
	    	addFavBtn = "";
	    	remFavBtn = "hidden=true";
	    } */
	    
%>
<br />
<br />
<input type="hidden" name="newsid" value="<%=newsItem.getItemId()%>">
<input type="submit" name="action" value="Add favorite" <%=addFavBtn %> align="right"/><input type="submit" name="action" value="Remove favorite" <%=remFavBtn %> align="right"/>
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
<br />
<br />
<br />
<a href="controller?action=viewNews">Go back to news</a>
</form>
</body>
</html>
