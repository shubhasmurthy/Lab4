package edu.asupoly.ser422.lab4.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.asupoly.ser422.lab4.model.BizLogicHandler;
import edu.asupoly.ser422.lab4.model.NewsItemBean;
import edu.asupoly.ser422.lab4.model.UserBean;

@WebServlet("/HandleAll")
public class HandleAll extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public HandleAll() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleIt(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		handleIt(request, response);
	}

	private void handleIt(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String action = request.getParameter("action");
		PrintWriter out = response.getWriter();
		if (action == null) {
			populateNews(request, null);
			RequestDispatcher dispatcher = request.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("goToLogin")) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
		} else if (action.equals("viewNews") || action.equals("Cancel")) {
			HttpSession session = request.getSession(false);
			UserBean user = (UserBean) session.getAttribute("user");
			populateNews(request, user);
			RequestDispatcher dispatcher = request.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("Login")) {
			String userId = request.getParameter("userid");
			String passwd = request.getParameter("passwd");
			String role = request.getParameter("role");
			if ((role == null) || role.contains("null")) {
				if (userId == null || userId.length() == 0 || passwd == null || passwd.length() == 0) {
					response.sendRedirect("login.jsp");
				} else if (!userId.equals(passwd)) {
					response.sendRedirect("login.jsp");
				} else {
					UserBean user = BizLogicHandler.validateUser(userId);
					if (user != null) {
						HttpSession session = request.getSession(true);
						session.setAttribute("user", user);
						populateNews(request, user);
					} else {

					}
					RequestDispatcher dispatcher = request.getRequestDispatcher(BizLogicHandler.getUrl());
					dispatcher.forward(request, response);
				}
			} else if (role.contains("Subscriber")) {
				if (userId == null || userId.length() == 0 || passwd == null || passwd.length() == 0) {
					response.sendRedirect("login.jsp?role=Subscriber");
				} else if (!userId.equals(passwd)) {
					response.sendRedirect("login.jsp?role=Subscriber");
				} else {
					HttpSession session = request.getSession(true);
					UserBean user = BizLogicHandler.createNewSubscriber(userId, passwd);
					session.setAttribute("user", user);
					populateNews(request, user);
					RequestDispatcher dispatcher = request.getRequestDispatcher(BizLogicHandler.getUrl());
					dispatcher.forward(request, response);
				}
			} else {
				if (userId == null || userId.length() == 0 || passwd == null || passwd.length() == 0) {
					response.sendRedirect("login.jsp");
				} else if (!userId.equals(passwd)) {
					response.sendRedirect("login.jsp");
				} else {
					UserBean user = BizLogicHandler.validateUser(userId);
					if (user != null) {
						HttpSession session = request.getSession(true);
						session.setAttribute("user", user);
						populateNews(request, user);
					} else {

					}
					RequestDispatcher dispatcher = request.getRequestDispatcher(BizLogicHandler.getUrl());
					dispatcher.forward(request, response);
				}
			}
		} else if (action.equals("about")) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("about.jsp");
			dispatcher.forward(request, response);
		} else if (action.equals("ViewStory")) {
			Cookie newsIDCookie = processCookie(request, "newsid");
			String newsIDCookieValue = null;
			if (newsIDCookie != null) {
				newsIDCookieValue = newsIDCookie.getValue();
			}
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			NewsItemBean nib = BizLogicHandler.getStoryItem(user, Integer.parseInt(request.getParameter("newsid")),
					newsIDCookieValue);
			request.setAttribute("newsItem", nib);
			Boolean isFav = BizLogicHandler.isFav(user, newsIDCookieValue, nib.getItemId());
			request.setAttribute("isFav", isFav);
			RequestDispatcher dispatcher = request.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("A new subscriber")) {
			String userId = request.getParameter("userid");
			String passwd = request.getParameter("passwd");
			HttpSession session = request.getSession(true);
			UserBean user = BizLogicHandler.createNewSubscriber(userId, passwd);
			session.setAttribute("user", user);
			populateNews(request, user);
			RequestDispatcher dispatcher = request.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("A new reporter")) {
			String userId = request.getParameter("userid");
			String passwd = request.getParameter("passwd");
			HttpSession session = request.getSession(true);
			UserBean user = BizLogicHandler.createNewReporter(userId, passwd);
			session.setAttribute("user", user);
			populateNews(request, user);
			RequestDispatcher dispatcher = request.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("logout")) {
			HttpSession session = request.getSession(false);
			session.invalidate();
			response.sendRedirect(request.getContextPath());
		} else if (action.equals("Add favorite")) {
			String newsId = request.getParameter("newsid");
			HttpSession session = request.getSession(false);
			UserBean user = (UserBean) session.getAttribute("user");
			if (user != null) {
				BizLogicHandler.addUserFavorite(user, Integer.parseInt(newsId));
			} else {
				Cookie newsIDCookie = processCookie(request, "newsid");
				if (newsIDCookie == null)// no favorites yet
				{
					Cookie newCookie = new Cookie("newsid", newsId);
					newCookie.setMaxAge(86400);
					response.addCookie(newCookie);
				} else {
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(newsIDCookie.getValue());
					stringBuilder.append(" ");
					stringBuilder.append(newsId);
					newsIDCookie.setValue(stringBuilder.toString());
					out.println(newsIDCookie.getValue());
					response.addCookie(newsIDCookie);
				}
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher("controller?action=ViewStory&newsid=" + newsId);
			dispatcher.forward(request, response);
		} else if (action.equals("Remove favorite")) {
			String newsId = request.getParameter("newsid");
			HttpSession session = request.getSession(false);
			UserBean user = (UserBean) session.getAttribute("user");
			if (user != null) {
				BizLogicHandler.deleteUserFavorite(user, Integer.parseInt(newsId));
			} else {
				Cookie newsIDCookie = processCookie(request, "newsid");
				if (newsIDCookie != null)// no favorites yet
				{
					String newsIDCookieValue = newsIDCookie.getValue();
					newsIDCookieValue = newsIDCookieValue.replace(newsIDCookieValue, "");
					newsIDCookie.setValue(newsIDCookieValue);
					response.addCookie(newsIDCookie);
				}
				out.println("newsIDCookie " + newsIDCookie.getValue());
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher("controller?action=ViewStory&newsid=" + newsId);
			dispatcher.forward(request, response);
		} else if (action.equals("create")) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("CreateNewStory.jsp");
			dispatcher.forward(request, response);
		} else if (action.equals("Add")) {
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			String title = request.getParameter("title");
			String story = request.getParameter("story");
			boolean isPublic = request.getParameter("isPublic").equals("subscribersOnly") ? false : true;
			BizLogicHandler.addStory(title, story, user.getUserId(), isPublic);
			populateNews(request, user);
			RequestDispatcher dispatcher = request.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("Update")) {
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			String title = request.getParameter("title");
			String story = request.getParameter("story");
			int newsID = Integer.parseInt(request.getParameter("newsid"));
			boolean isPublic = request.getParameter("isPublic").equals("subscribersOnly") ? false : true;
			BizLogicHandler.updateStory(newsID, title, story, isPublic);
			populateNews(request, user);
			RequestDispatcher dispatcher = request.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("delete")) {
			int newsID = Integer.parseInt(request.getParameter("newsid"));
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			BizLogicHandler.removeStory(newsID);
			populateNews(request, user);
			RequestDispatcher dispatcher = request.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("edit")) {
			int newsID = Integer.parseInt(request.getParameter("newsid"));
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			Cookie newsIDCookie = processCookie(request, "newsid");
			String newsIDCookieValue = null;
			if (newsIDCookie != null) {
				newsIDCookieValue = newsIDCookie.getValue();
			}
			NewsItemBean nib = BizLogicHandler.getStoryItem(user, newsID, newsIDCookieValue);
			request.setAttribute("newsItem", nib);
			RequestDispatcher dispatcher = request.getRequestDispatcher("CreateNewStory.jsp");
			dispatcher.forward(request, response);
		} else if (action.equals("comment")) {
			int newsID = Integer.parseInt(request.getParameter("newsid"));
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			String comment = request.getParameter("Comment");
			BizLogicHandler.storeComment(newsID, user.getUserId(), comment);
			populateNews(request, user);
			RequestDispatcher dispatcher = request.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		}
	}

	private static Cookie processCookie(HttpServletRequest req, String cName) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(cName)) {
					return cookies[i];
				}
			}
		}
		return null;
	}

	private void populateNews(HttpServletRequest request, UserBean user) {
		Cookie newsIDCookie = processCookie(request, "newsid");
		NewsItemBean[] news;
		if (newsIDCookie != null) {
			news = BizLogicHandler.getNews(user, newsIDCookie.getValue());
		} else {
			news = BizLogicHandler.getNews(user, null);
		}
		request.setAttribute("newsList", news);
	}
}
