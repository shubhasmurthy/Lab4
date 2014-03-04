/*package edu.asupoly.ser422.lab4.dao;

import edu.asupoly.ser422.lab4.model.NewsItemBean;
import edu.asupoly.ser422.lab4.model.UserBean;

public class NewsDummyDAO implements INewsDAO {

	NewsDummyDAO() {}
	
	@Override
	public NewsItemBean[] getNews() {
		NewsItemBean[] news = new NewsItemBean[10];
		for (int i = 0; i < news.length; i++) {
			news[i] = new NewsItemBean(i, "title"+i, "story"+i, "reporter"+i%3);
		}
		return news;
	}

	@Override
	public boolean createUser(UserBean user) {
		return Math.random() > 0.1;  // returns false 10% of the time
	}

	@Override
	public UserBean getUser(String userid) {
		if (Math.random() < 0.1) return null;
		
		UserBean.Role role = null;
		int choice = (int)Math.floor(Math.random()*3);
		if (choice == 0) role = UserBean.Role.GUEST;
		else if (choice == 1) role = UserBean.Role.SUBSCRIBER;
		else if (choice == 2) role = UserBean.Role.REPORTER;
		return new UserBean(userid, userid, role);
	}

	@Override
	public boolean storeComment(int newsItemId, String userid, String comment) {
		return Math.random() > 0.1;
	}

	@Override
	public boolean createNewsItem(NewsItemBean nib) {
		return Math.random() > 0.1;
	}

	@Override
	public NewsItemBean getNewsItem(int i) {
		return new NewsItemBean(i, "title"+i, "story"+i, "reporter"+i%3);
	}

	@Override
	public boolean updateNewsItem(int newsItemId, String newTitle, String newStory) {
		return Math.random() > 0.1;
	}

	@Override
	public boolean deleteNewsItem(int newsItemId) {
		return Math.random() > 0.1;
	}

}
*/