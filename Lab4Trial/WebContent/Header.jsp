<%@page import="edu.asupoly.ser422.lab4.model.*"%>
<%@page import="edu.asupoly.ser422.lab4.dao.*"%>
		
		<% 
	    String pageName = "controller?action=goToLogin";
	    String linkName = "Login";
		String uname = (String) request.getSession().getAttribute("userName");
		UserBean user = NewsDAOFactory.getTheDAO().getUser(uname);
	    if(user!=null){
	    	pageName = "controller?action=logout";
        	linkName = "Logout";
	    }
	    
		%>
<table border="0" align="center">
	<tr>
		<td><a href="./about.html">About</a></td>
		<td><a href="controller?action=viewNews">View News</a></td>
		<%
		if(user!=null){
			if(user.isReporter()){
				out.println("<td><a href=\"controller?action=create\">Create New</a></td>");
			}
		}
		%>
		<td><a href="<%=pageName %>"><%=linkName %></a></td>
	</tr>
</table>