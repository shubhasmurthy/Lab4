<%@page import="edu.asupoly.ser422.lab4.model.*"%>
<%@page import="edu.asupoly.ser422.lab4.dao.*"%>
<html>
    <head>
        <title>NEW News - NEW News about stuff that matters.</title>
    </head>
    <body>
        <h1>NEW News</h1> 
        <jsp:include page="Header.jsp" />  
        <%
        String title = "";
        String story = "";
        String isPublicChecked = "checked";
        String isSubscriberChecked = "checked";
        int newsID = 0;
        NewsItemBean nib = (NewsItemBean) request.getAttribute("newsItem");
        String newsid = request.getParameter("newsid");
        if(nib != null){
				title = nib.getItemTitle();
				story = nib.getItemStory();
				if(!nib.isPublicStory()){
					isPublicChecked = "";
				}
				else{
					isSubscriberChecked = "";
				}
			}
        %>    
		<p>Fill in all fields to add your news to NEW news.</p>
		<form action="controller" method="post">
    		Title: <input type="text" size="50" name="title" value="<%= title%>" /><br/>
    		Story: <textarea cols="50" rows="15" name="story" /><%= story%></textarea>
			<br/>
			Make this:
			<br />
			<input type="radio" name="isPublic" value="isPublic" <%= isPublicChecked%>>Public story 
			<input type="radio" name="isPublic" value="subscribersOnly" <%= isSubscriberChecked%>>Subscribers only
			<br/>
			<%
			String action = request.getParameter("action");
			if(action.equals("create")){
				out.println("<input type=\"submit\" name=\"action\" value=\"Add\" />");
			}else if(action.equals("edit")){
				out.println("<input type=\"hidden\" name=\"newsid\" value=\""+nib.getItemId()+"\">");
				out.println("<input type=\"submit\" name=\"action\" value=\"Update\" />");
			}
			out.println("<input type=\"submit\" name=\"action\" value=\"Cancel\" />");
			%>	
		</form>
	</body>
</html>
