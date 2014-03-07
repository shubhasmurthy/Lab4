package edu.asupoly.ser422.lab4.dao;

import java.util.Hashtable;

import edu.asupoly.ser422.lab4.model.CommentBean;
import edu.asupoly.ser422.lab4.model.NewsItemBean;
import edu.asupoly.ser422.lab4.model.UserBean;

public class NewsDefaultDAO implements INewsDAO {
	protected Hashtable<Integer, NewsItemBean> __news;
	protected Hashtable<String, UserBean> __users;
	protected Hashtable<String, CommentBean> __comments; // key is the news item
															// id

	NewsDefaultDAO() {
		__news = new Hashtable<Integer, NewsItemBean>();
		__users = new Hashtable<String, UserBean>();
		__comments = new Hashtable<String, CommentBean>();
	}

	@Override
	public NewsItemBean[] getNews() {
		synchronized (__news) {
			return __news.values().toArray(new NewsItemBean[0]);
		}
	}

	@Override
	public boolean createUser(UserBean user) {
		boolean rval = true;
		if (user != null) {
			try {
				__users.put(user.getUserId(), user);
			} catch (Throwable t) {
				rval = false; // Most likely scenario is
								// ConcurrentModificationException
			}
		} else {
			rval = false;
		}
		return rval;
	}

	@Override
	public UserBean getUser(String userid) {
		try {
			return __users.get(userid);
		} catch (Throwable t) {
			return null; // Most likely scenario is
							// ConcurrentModificationException
		}
	}

	@Override
	public boolean storeComment(int newsItemId, String userid, String comment) {
		NewsItemBean nib = getNewsItem(newsItemId);
		if (nib != null) {
			synchronized (nib) {
				nib.addComment(new CommentBean(nib, userid, comment));
			}
			return true;
		}
		return false;
	}

	public boolean storeFavorite(UserBean user, int newsItemId) {
		NewsItemBean nib = getNewsItem(newsItemId);
		if (nib != null) {
			synchronized (user) {
				user.addToFavorites(newsItemId);
			}
			return true;
		}
		return false;
	}

	public boolean removeFavorite(UserBean user, int newsItemId) {
		NewsItemBean nib = getNewsItem(newsItemId);
		if (nib != null) {
			synchronized (nib) {
				user.removeFromFavorites(newsItemId);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean createNewsItem(NewsItemBean nib) {
		boolean rval = true;
		if (nib != null) {
			try {
				__news.put(nib.getItemId(), nib);
			} catch (Throwable t) {
				rval = false; // Most likely scenario is
								// ConcurrentModificationException
			}
		} else {
			rval = false;
		}
		return rval;
	}

	@Override
	public NewsItemBean getNewsItem(int i) {
		try {
			return __news.get(i);
		} catch (Throwable t) {
			return null; // Most likely scenario is
							// ConcurrentModificationException
		}
	}

	@Override
	public boolean updateNewsItem(int newsItemId, String newTitle, String newStory, boolean isPublic) {
		NewsItemBean nib = getNewsItem(newsItemId);
		boolean rval = false;
		if (nib != null) {
			synchronized (nib) {
				nib.setItemTitle(newTitle, nib.getReporterId());
				nib.setItemStory(newStory, nib.getReporterId());
				if (isPublic) {
					nib.setAsPublicStory();
				}
			}
			rval = true;
		}
		return rval;
	}

	@Override
	public boolean deleteNewsItem(int newsItemId) {
		NewsItemBean deletedNews = __news.remove(newsItemId);
		return deletedNews != null;
	}
}