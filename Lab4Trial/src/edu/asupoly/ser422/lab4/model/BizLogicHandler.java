package edu.asupoly.ser422.lab4.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import edu.asupoly.ser422.lab4.dao.NewsDAOFactory;

public class BizLogicHandler {
	private static String url = "";

	/*
	 * public static final NewsItemBean[] getNews() { url = "ViewNews.jsp";
	 * return NewsDAOFactory.getTheDAO().getNews(); }
	 */

	public static final NewsItemBean[] getNews(UserBean user) {
		url = "ViewNews.jsp";
		ArrayList<NewsItemBean> news = new ArrayList<NewsItemBean>();
		NewsItemBean[] newsArray = NewsDAOFactory.getTheDAO().getNews();
		// news.addAll(NewsDAOFactory.getTheDAO().getNews());
		if (user == null || user.getRole().equals(UserBean.Role.GUEST)) {
			for (NewsItemBean nib : newsArray) {
				if (nib.isPublicStory()) {
					news.add(nib);
				}
			}
		} else if (user.getRole().equals(UserBean.Role.SUBSCRIBER)) {
			// return __news.values().toArray(new NewsItemBean[0]);
			news.addAll(Arrays.asList(newsArray));
		} else {
			for (NewsItemBean nib : newsArray) {
				if (nib.isPublicStory() || nib.getReporterId().equals(user.getUserId())) {
					news.add(nib);
				}
			}
		}
		Collections.sort(news, new Comparator<NewsItemBean>() {
			public int compare(NewsItemBean n1, NewsItemBean n2) {
				return n2.getFavorite().compareTo(n1.getFavorite());
			}
		});
		return news.toArray(new NewsItemBean[0]);
	}

	public static final NewsItemBean getStoryItem(int newsID) {
		url = "ViewStory.jsp";
		return NewsDAOFactory.getTheDAO().getNewsItem(newsID);
	}

	public static final void storeComment(int newsItemId, String userid, String comment) {
		url = "ViewNews.jsp";
		NewsDAOFactory.getTheDAO().storeComment(newsItemId, userid, comment);
	}

	public static final UserBean validateUser(String uID) {
		url = "ViewNews.jsp";
		UserBean user = NewsDAOFactory.getTheDAO().getUser(uID);
		if (user == null) {
			url = "CreateNewUser.jsp";
			return null;
		} else {
			url = "ViewNews.jsp";
		}
		return user;
	}

	public static final String getUrl() {
		return url;
	}
}
