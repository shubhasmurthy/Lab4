package edu.asupoly.ser422.lab4.model;

import java.util.ArrayList;
import java.util.StringTokenizer;

import edu.asupoly.ser422.lab4.dao.NewsDAOFactory;

public class BizLogicHandler {
	private static String url = "";

	public static final NewsItemBean[] getNews(UserBean user, String cookieValue) {
		url = "ViewNews.jsp";
		ArrayList<NewsItemBean> news = new ArrayList<NewsItemBean>();
		NewsItemBean[] newsArray = NewsDAOFactory.getTheDAO().getNews();
		String newsID;
		if (user == null || user.getRole().equals(UserBean.Role.GUEST)) {
			if (cookieValue != null) {
				StringTokenizer st = new StringTokenizer(cookieValue, " ");
				while (st.hasMoreElements()) {
					newsID = st.nextToken();
					NewsItemBean nib = NewsDAOFactory.getTheDAO().getNewsItem(Integer.parseInt(newsID));
					if (nib.isPublicStory()) {
						news.add(nib);
					}
				}
			}
			for (NewsItemBean nib : newsArray) {
				if (!hasAlready(news, nib.getItemId())) {
					if (nib.isPublicStory()) {
						news.add(nib);
					}
				}
			}
		} else if (user.getRole().equals(UserBean.Role.SUBSCRIBER)) {
			ArrayList<Integer> favList = user.getFavorites();
			if (favList.size() != 0) {
				for (Integer i : favList) {
					news.add(NewsDAOFactory.getTheDAO().getNewsItem(i));
				}
			}
			for (NewsItemBean nib : newsArray) {
				if (!hasAlready(news, nib.getItemId())) {
					news.add(nib);
				}
			}
		} else {
			ArrayList<Integer> favList = user.getFavorites();
			if (favList.size() != 0) {
				for (Integer i : favList) {
					news.add(NewsDAOFactory.getTheDAO().getNewsItem(i));
				}
			}
			for (NewsItemBean nib : newsArray) {
				if (!hasAlready(news, nib.getItemId())) {
					if (nib.isPublicStory() || nib.getReporterId().equals(user.getUserId())) {
						news.add(nib);
					}
				}
			}
		}
		return news.toArray(new NewsItemBean[0]);
	}

	private static final boolean hasAlready(ArrayList<NewsItemBean> news, int newsID) {
		for (NewsItemBean n : news) {
			if (n.getItemId() == newsID)
				return true;
		}
		return false;
	}

	public static final UserBean createNewSubscriber(String userId, String passwd) {
		url = "ViewNews.jsp";
		UserBean user = new UserBean(userId, passwd, UserBean.Role.SUBSCRIBER);
		NewsDAOFactory.getTheDAO().createUser(user);
		return user;
	}

	public static final UserBean createNewReporter(String userId, String passwd) {
		url = "ViewNews.jsp";
		UserBean user = new UserBean(userId, passwd, UserBean.Role.REPORTER);
		NewsDAOFactory.getTheDAO().createUser(user);
		return user;
	}

	public static final NewsItemBean getStoryItem(UserBean user, int newsID, String cookieValue) {
		url = "ViewStory.jsp";
		NewsItemBean newsItem = NewsDAOFactory.getTheDAO().getNewsItem(newsID);
		if (user != null) {
			/*
			 * if (user.isFavorite(newsID)) { newsItem.markAsFavorite(); } else
			 * { newsItem.unMarkAsFavorite(); }
			 */
		} else {
			String newsId;
			int newsidAsInt;
			if (cookieValue != null) {
				StringTokenizer st = new StringTokenizer(cookieValue, " ");
				while (st.hasMoreElements()) {
					newsId = st.nextToken();
					newsidAsInt = Integer.parseInt(newsId);
					/*
					 * if (newsidAsInt == newsID) { newsItem.markAsFavorite();
					 * break; }
					 */
				}
			}
		}
		return newsItem;
	}

	public static final void storeComment(int newsItemId, String userid, String comment) {
		url = "ViewNews.jsp";
		NewsDAOFactory.getTheDAO().storeComment(newsItemId, userid, comment);
	}

	public static final void addStory(String title, String story, String userid, boolean isPublic) {
		url = "ViewNews.jsp";
		NewsDAOFactory.getTheDAO().createNewsItem(new NewsItemBean(title, story, userid, isPublic));
	}

	public static final void removeStory(int newsID) {
		url = "ViewNews.jsp";
		NewsDAOFactory.getTheDAO().deleteNewsItem(newsID);
	}

	public static final void updateStory(int newsID, String title, String story, boolean isPublic) {
		url = "ViewNews.jsp";
		NewsDAOFactory.getTheDAO().updateNewsItem(newsID, title, story, isPublic);
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

	public static final void addUserFavorite(UserBean user, int newsID) {
		NewsDAOFactory.getTheDAO().storeFavorite(user, newsID);
	}

	public static final void deleteUserFavorite(UserBean user, int newsID) {
		NewsDAOFactory.getTheDAO().removeFavorite(user, newsID);
	}

	public static boolean isFav(UserBean user, String cookieValue, int newsID) {
		if (user != null) {
			if (user.isFavorite(newsID)) {
				return true;
			} else {
				return false;
			}
		} else {
			String newsId;
			int newsidAsInt;
			if (cookieValue != null) {
				StringTokenizer st = new StringTokenizer(cookieValue, " ");
				while (st.hasMoreElements()) {
					newsId = st.nextToken();
					newsidAsInt = Integer.parseInt(newsId);
					if (newsidAsInt == newsID) {
						return true;
					}
				}
			}
			return false;
		}
	}
}
