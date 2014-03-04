<%@page import="edu.asupoly.ser422.lab4.model.*"%>
<%@page import="edu.asupoly.ser422.lab4.dao.*"%>
<%NewsItemBean newsItem = NewsDAOFactory.getTheDAO().getNewsItem(Integer.parseInt(request.getParameter("storyid")));
%>
<h2><%= newsItem.getItemTitle() %></h2>
<br />
<%= newsItem.getItemStory() %>
<br />
<br />
<br />
<a href="index.jsp">Back</a>