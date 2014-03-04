package edu.asupoly.ser422.lab4.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

		__news.put(1, new NewsItemBean(1, "Telangana1", "The worst stuff happened", "rep1"));
		__news.put(2, new NewsItemBean(2, "Telangana2", "The worst  happened", "rep2"));
		__news.put(3, new NewsItemBean(3, "Telangana3", "The  stuff happened", "rep3"));
		__news.put(4, new NewsItemBean(4, "Telangana4", "The worst stuff", "rep1"));
		__news.put(5, new NewsItemBean(5, "Telangana5", "The worst stuff", "rep1", false));
		__news.put(6, new NewsItemBean(6, "Rep2 private story", "This is Rep2's private story", "rep2", false));
		__users.put("sub1", new UserBean("sub1", "sub1", UserBean.Role.SUBSCRIBER));
		__users.put("rep1", new UserBean("rep1", "rep1", UserBean.Role.REPORTER));
	}

	@Override
	public NewsItemBean[] getNews() {
		synchronized (__news) {
			return __news.values().toArray(new NewsItemBean[0]);
		}
	}

	public NewsItemBean[] getNews(UserBean user) {
		synchronized (__news) {
			ArrayList<NewsItemBean> news = new ArrayList<NewsItemBean>();
			if (user == null || user.getRole().equals(UserBean.Role.GUEST)) {
				for (NewsItemBean nib : __news.values()) {
					if (nib.isPublicStory()) {
						news.add(nib);
					}
				}
			} else if (user.getRole().equals(UserBean.Role.SUBSCRIBER)) {
				// return __news.values().toArray(new NewsItemBean[0]);
				news.addAll(__news.values());
			} else {
				for (NewsItemBean nib : __news.values()) {
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

	public boolean storeFavorite(int newsItemId) {
		NewsItemBean nib = getNewsItem(newsItemId);
		if (nib != null) {
			synchronized (nib) {
				nib.addFavorite();
			}
			return true;
		}
		return false;
	}

	public boolean removeFavorite(int newsItemId) {
		NewsItemBean nib = getNewsItem(newsItemId);
		if (nib != null) {
			synchronized (nib) {
				nib.removeFavorite();
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