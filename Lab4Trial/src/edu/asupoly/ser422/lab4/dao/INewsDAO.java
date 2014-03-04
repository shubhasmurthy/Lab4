package edu.asupoly.ser422.lab4.dao;

import edu.asupoly.ser422.lab4.model.NewsItemBean;
import edu.asupoly.ser422.lab4.model.UserBean;

public interface INewsDAO {
	public boolean createUser(UserBean user);

	public UserBean getUser(String userid);

	public boolean storeComment(int newsItemId, String userid, String comment);

	public boolean createNewsItem(NewsItemBean nib);

	public NewsItemBean[] getNews();

	public NewsItemBean[] getNews(UserBean user);

	public NewsItemBean getNewsItem(int newsItemId);

	public boolean updateNewsItem(int newsItemId, String newTitle, String newStory, boolean isPublic);

	public boolean deleteNewsItem(int newsItemId);

	public boolean storeFavorite(int newsItemId);

	public boolean removeFavorite(int newsItemId);
}