package edu.asupoly.ser422.lab4.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.asupoly.ser422.lab4.dao.NewsDAOFactory;
import edu.asupoly.ser422.lab4.model.UserBean;

/**
 * Servlet implementation class LoginValidate
 */
@WebServlet("/LoginValidate")
public class LoginValidate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public LoginValidate() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String action = request.getParameter("action");
		HttpSession session = request.getSession(true);
		if (action == null) {
			String userId = request.getParameter("userid");
			String passwd = request.getParameter("passwd");
			boolean userExists = true;
			if (userId == null || userId.length() == 0 || passwd == null || passwd.length() == 0) {
				/*
				 * RequestDispatcher dispatcher =
				 * request.getRequestDispatcher("login.html");
				 * dispatcher.forward(request, response);
				 */
				response.sendRedirect("login.html");
				// msg = "The userID or password cannot be empty";
			} else {
				UserBean user = NewsDAOFactory.getTheDAO().getUser(userId);
				// if (session.getAttribute("userName") == null) {
				session.setAttribute("userName", userId);
				// }
				// if (session.getAttribute("password") == null) {
				session.setAttribute("password", passwd);
				// }
				if (user == null) {
					RequestDispatcher dispatcher = request.getRequestDispatcher("CreateNewUser.html");
					dispatcher.forward(request, response);
				} else {
					RequestDispatcher dispatcher = request.getRequestDispatcher("ViewNews.jsp");
					dispatcher.forward(request, response);
					// msg = "Successful login, you are User " +
					// user.toString();
				}
			}
		} else {
			PrintWriter out = response.getWriter();
			String uname = (String) session.getAttribute("userName");
			String pwd = (String) session.getAttribute("password");
			if (uname != null && pwd != null) {
				if (action.equals("A new subscriber")) {
					UserBean user = new UserBean(uname, pwd, UserBean.Role.SUBSCRIBER);
					NewsDAOFactory.getTheDAO().createUser(user);
					RequestDispatcher dispatcher = request.getRequestDispatcher("ViewNews.jsp");
					dispatcher.forward(request, response);
				}
				if (action.equals("A new reporter")) {
					UserBean user = new UserBean(uname, pwd, UserBean.Role.REPORTER);
					NewsDAOFactory.getTheDAO().createUser(user);
					RequestDispatcher dispatcher = request.getRequestDispatcher("ViewNews.jsp");
					dispatcher.forward(request, response);
				}
			}

		}

	}
}
