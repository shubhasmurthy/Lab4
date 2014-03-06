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

import edu.asupoly.ser422.lab4.dao.NewsDAOFactory;
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

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		handleIt(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		handleIt(request, response);
	}

	private void handleIt(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		PrintWriter out = response.getWriter();
		if (action == null) {
			populateNews(request, null);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("goToLogin")) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
		} else if (action.equals("viewNews") || action.equals("Cancel")) {
			HttpSession session = request.getSession(false);
			UserBean user = (UserBean) session.getAttribute("user");
			populateNews(request, user);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("Login")) {
			String userId = request.getParameter("userid");
			String passwd = request.getParameter("passwd");
			String role = request.getParameter("role");
			if (role != null) {
				if (userId == null || userId.length() == 0 || passwd == null
						|| passwd.length() == 0) {
					response.sendRedirect("login.jsp?role=Subscriber");
				} else if (!userId.equals(passwd)) {
					response.sendRedirect("login.jsp?role=Subscriber");
				} else {
					HttpSession session = request.getSession(true);
					UserBean user = BizLogicHandler.createNewSubscriber(userId,
							passwd);
					session.setAttribute("user", user);
					populateNews(request, user);
					RequestDispatcher dispatcher = request
							.getRequestDispatcher(BizLogicHandler.getUrl());
					dispatcher.forward(request, response);
				}
			} else {
				if (userId == null || userId.length() == 0 || passwd == null
						|| passwd.length() == 0) {
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
					RequestDispatcher dispatcher = request
							.getRequestDispatcher(BizLogicHandler.getUrl());
					dispatcher.forward(request, response);
				}
			}
		} else if (action.equals("about")) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("about.jsp");
			dispatcher.forward(request, response);
		} else if (action.equals("ViewStory")) {
			NewsItemBean nib = BizLogicHandler.getStoryItem(Integer
					.parseInt(request.getParameter("newsid")));
			request.setAttribute("newsItem", nib);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("A new subscriber")) {
			String userId = request.getParameter("userid");
			String passwd = request.getParameter("passwd");
			HttpSession session = request.getSession(true);
			UserBean user = BizLogicHandler.createNewSubscriber(userId, passwd);
			session.setAttribute("user", user);
			populateNews(request, user);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("A new reporter")) {
			String userId = request.getParameter("userid");
			String passwd = request.getParameter("passwd");
			HttpSession session = request.getSession(true);
			UserBean user = BizLogicHandler.createNewReporter(userId, passwd);
			session.setAttribute("user", user);
			populateNews(request, user);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("logout")) {
			HttpSession session = request.getSession(false);
			session.invalidate();
			response.sendRedirect(request.getContextPath());
		} else if (action.equals("Add as favorite")) {
			// int newsID = Integer.parseInt(request.getParameter("newsid"));
			boolean setCookie = false;
			NewsItemBean nib = BizLogicHandler.getStoryItem(Integer
					.parseInt(request.getParameter("newsid")));
			request.setAttribute("newsItem", nib);
			String userId = null,role=null;
			HttpSession session = request.getSession(false);
			UserBean user = (UserBean) session.getAttribute("user");
			if(user!=null){
			 userId = user.getUserId();
			 role = user.getRole().toString();
			}
			if (userId == null || role.contains("Guest")) {
				String newsId = request.getParameter("newsid");
				Cookie[] cookie = request.getCookies();
				if (cookie == null)// no favorites yet
				{
					Cookie newCookie = new Cookie("newsid", newsId);
					newCookie.setMaxAge(86400);
					response.addCookie(newCookie);
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("ViewStory.jsp");
					dispatcher.forward(request, response);

				} else {
					int i = 0;
					for (i = 0; i < cookie.length; i++) {
						if (cookie[i].getName().contains("newsid")) {
							String oldCookie = cookie[i].getValue();
							newsId = oldCookie + "\t" + newsId;
							cookie[i].setValue(newsId);
							cookie[i].setMaxAge(86400);
							setCookie = true;
							response.addCookie(cookie[i]);
							RequestDispatcher dispatcher = request
									.getRequestDispatcher("ViewStory.jsp");
							dispatcher.forward(request, response);
						}
					}

					if ((i == cookie.length) && (setCookie == false)) {
						Cookie newCookie = new Cookie("newsid", newsId);
						newCookie.setMaxAge(86400);
						response.addCookie(newCookie);
						RequestDispatcher dispatcher = request
								.getRequestDispatcher("ViewStory.jsp");
						dispatcher.forward(request, response);
					}
				}
			} else if (role.contains("SUBSCRIBER")) {
				String newsId = request.getParameter("newsid");
				String subCookie = "sub" + userId + "newsid";
				Cookie[] cookie = request.getCookies();
				if (cookie == null)// no favorites yet
				{
					Cookie newCookie = new Cookie(subCookie, newsId);
					response.addCookie(newCookie);
					response.sendRedirect(request.getContextPath());

				} else {
					int i = 0;
					for (i = 0; i < cookie.length; i++) {
						if (cookie[i].getName().contains(subCookie)) {
							String oldCookie = cookie[i].getValue();
							newsId = oldCookie + "\t" + newsId;
							cookie[i].setValue(newsId);
							cookie[i].setMaxAge(86400);
							setCookie = true;
							response.addCookie(cookie[i]);
							RequestDispatcher dispatcher = request
									.getRequestDispatcher("ViewStory.jsp");
							dispatcher.forward(request, response);
						}
					}

					if ((i == cookie.length) && (setCookie == false)) {
						Cookie newCookie = new Cookie(subCookie, newsId);
						newCookie.setMaxAge(86400);
						response.addCookie(newCookie);
						RequestDispatcher dispatcher = request
								.getRequestDispatcher("ViewStory.jsp");
						dispatcher.forward(request, response);
					}
				}
			} else if (role.contains("REPORTER")) {
				String newsId = request.getParameter("newsid");
				String repCookie = "rep" + userId + "newsid";
				Cookie[] cookie = request.getCookies();
				if (cookie == null)// no favorites yet
				{
					Cookie newCookie = new Cookie(repCookie, newsId);
					response.addCookie(newCookie);
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("ViewStory.jsp");
					dispatcher.forward(request, response);

				} else {
					int i = 0;
					for (i = 0; i < cookie.length; i++) {
						if (cookie[i].getName().contains(repCookie)) {
							String oldCookie = cookie[i].getValue();
							newsId = oldCookie + "\t" + newsId;
							cookie[i].setValue(newsId);
							cookie[i].setMaxAge(86400);
							setCookie = true;
							response.addCookie(cookie[i]);
							RequestDispatcher dispatcher = request
									.getRequestDispatcher("ViewStory.jsp");
							dispatcher.forward(request, response);
						}
					}

					if ((i == cookie.length) && (setCookie == false)) {
						Cookie newCookie = new Cookie(repCookie, newsId);
						newCookie.setMaxAge(86400);
						response.addCookie(newCookie);
						RequestDispatcher dispatcher = request
								.getRequestDispatcher("ViewStory.jsp");
						dispatcher.forward(request, response);
					}
				}
			}
		} else if (action.equals("Remove as favorite")) {
			NewsItemBean nib = BizLogicHandler.getStoryItem(Integer
					.parseInt(request.getParameter("newsid")));
			request.setAttribute("newsItem", nib);
			HttpSession session = request.getSession(false);
			UserBean user = (UserBean) session.getAttribute("user");
			String userId = user.getUserId();
			String role = user.getRole().toString();
			
			Cookie[] cookie  = request.getCookies();
			if (cookie==null)
				{RequestDispatcher dispatcher = request
				.getRequestDispatcher("ViewStory.jsp");
		dispatcher.forward(request, response);}
			if (role == null || role.contains("GUEST")) {
				String newsId = request.getParameter("newsid");
				if (cookie != null) {
					int i = 0;
					for (i = 0; i < cookie.length; i++) {
						if (cookie[i].getName().contains("newsid")) {
							String[] newsArray = cookie[i].getValue().split(
									"\t");
							String newCookieValue = "";
							for (int j = 0; j < newsArray.length; j++) {
								if (!newsArray[j].contentEquals(newsId))
									newCookieValue = newCookieValue + "\t"
											+ newsArray[j];
							}
							cookie[i].setValue(newCookieValue);
							cookie[i].setMaxAge(86400);
							response.addCookie(cookie[i]);
							
						}
					}
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("ViewStory.jsp");
					dispatcher.forward(request, response);
				}
			} else if (role.contains("SUBSCRIBER")) {
				String newsId = request.getParameter("newsid");
				String subCookie = "sub" + userId + "newsid";
				if (cookie != null) {
					int i = 0;
					for (i = 0; i < cookie.length; i++) {
						if (cookie[i].getName().contains(subCookie)) {
							String[] newsArray = cookie[i].getValue().split(
									"\t");
							String newCookieValue = "";
							for (int j = 0; j < newsArray.length; j++) {
								if (!newsArray[j].contentEquals(newsId))
									newCookieValue = newCookieValue + "\t"
											+ newsArray[j];
							}
							cookie[i].setValue(newCookieValue);
							cookie[i].setMaxAge(86400);
							response.addCookie(cookie[i]);
							
						}
					}
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("ViewStory.jsp");
					dispatcher.forward(request, response);
				}
				
			} else if (role.contains("REPORTER")) {
				String newsId = request.getParameter("newsid");
				String repCookie = "rep" + userId + "newsid";
				if (cookie != null) {
					int i = 0;
					for (i = 0; i < cookie.length; i++) {
						if (cookie[i].getName().contains(repCookie)) {
							String[] newsArray = cookie[i].getValue().split(
									"\t");
							String newCookieValue = "";
							for (int j = 0; j < newsArray.length; j++) {
								if (!newsArray[j].contentEquals(newsId))
									newCookieValue = newCookieValue + "\t"
											+ newsArray[j];
							}
							cookie[i].setValue(newCookieValue);
							cookie[i].setMaxAge(86400);
							response.addCookie(cookie[i]);
						}
					}
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("ViewStory.jsp");
					dispatcher.forward(request, response);
				}
			}

		} else if (action.equals("create")) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("CreateNewStory.jsp");
			dispatcher.forward(request, response);
		} else if (action.equals("Add")) {
			UserBean user = (UserBean) request.getSession()
					.getAttribute("user");
			String title = request.getParameter("title");
			String story = request.getParameter("story");
			boolean isPublic = request.getParameter("isPublic").equals(
					"subscribersOnly") ? false : true;
			BizLogicHandler.addStory(title, story, user.getUserId(), isPublic);
			populateNews(request, user);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("Update")) {
			out.println("Here");
			UserBean user = (UserBean) request.getSession()
					.getAttribute("user");
			String title = request.getParameter("title");
			String story = request.getParameter("story");
			int newsID = Integer.parseInt(request.getParameter("newsid"));
			boolean isPublic = request.getParameter("isPublic").equals(
					"subscribersOnly") ? false : true;
			BizLogicHandler.updateStory(newsID, title, story, isPublic);
			populateNews(request, user);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("delete")) {
			int newsID = Integer.parseInt(request.getParameter("newsid"));
			UserBean user = (UserBean) request.getSession()
					.getAttribute("user");
			BizLogicHandler.removeStory(newsID);
			populateNews(request, user);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		} else if (action.equals("edit")) {
			int newsID = Integer.parseInt(request.getParameter("newsid"));
			UserBean userid = (UserBean) request.getSession().getAttribute(
					"user");
			NewsItemBean nib = BizLogicHandler.getStoryItem(newsID);
			request.setAttribute("newsItem", nib);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("CreateNewStory.jsp");
			dispatcher.forward(request, response);
		} else if (action.equals("comment")) {
			int newsID = Integer.parseInt(request.getParameter("newsid"));
			UserBean user = (UserBean) request.getSession()
					.getAttribute("user");
			String comment = request.getParameter("Comment");
			BizLogicHandler.storeComment(newsID, user.getUserId(), comment);
			populateNews(request, user);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(BizLogicHandler.getUrl());
			dispatcher.forward(request, response);
		}

	}

	private void populateNews(HttpServletRequest request, UserBean user) {
		NewsItemBean[] news = BizLogicHandler.getNews(user);
		request.setAttribute("newsList", news);
	}
}
