package edu.asupoly.ser422.lab4.model;

import java.util.ArrayList;
import java.util.Date;

public class NewsItemBean implements java.io.Serializable {
	private static final long serialVersionUID = 4760114651123862127L;

	private static int nextId = 100;

	private int itemId;
	private String itemTitle;
	private String itemStory;
	private Date itemDate;
	private String reporterId;
	private int favoriteCount;
	private boolean isPublic;
	private ArrayList<CommentBean> comments = new ArrayList<CommentBean>();

	// This constructor is used for a new news item
	public NewsItemBean(String title, String story, String rid) {
		this(nextId++, title, story, rid, true);
	}

	public NewsItemBean(String title, String story, String rid, boolean isPub) {
		this(nextId++, title, story, rid, isPub);
	}

	public NewsItemBean(int id, String title, String story, String rid) {
		itemTitle = title;
		itemStory = story;
		reporterId = rid;
		itemDate = new Date();
		itemId = id;
		isPublic = true;
		favoriteCount = 0;
	}

	// This constructor is used for an existing, i.e. coming from datastore
	public NewsItemBean(int id, String title, String story, String rid, boolean isPub) {
		itemTitle = title;
		itemStory = story;
		reporterId = rid;
		itemDate = new Date();
		itemId = id;
		isPublic = isPub;
		favoriteCount = 0;
	}

	public int getItemId() {
		return itemId;
	}

	public boolean isPublicStory() {
		return isPublic;
	}

	public void setAsPublicStory() {
		isPublic = true;
	}

	public String getReporterId() {
		return reporterId;
	}

	public void setItemTitle(String itemTitle, String rid) {
		if (rid.equals(reporterId)) {
			this.itemTitle = itemTitle;
		}
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemStory(String itemStory, String rid) {
		if (rid.equals(reporterId)) {
			this.itemStory = itemStory;
			setItemDate(new Date());
		}
	}

	public String getItemStory() {
		return itemStory;
	}

	private void setItemDate(Date itemDate) {
		this.itemDate = itemDate;
	}

	public Date getItemDate() {
		return itemDate;
	}

	public void addComment(CommentBean cb) {
		comments.add(cb);
	}

	public CommentBean[] getComments() {
		return comments.toArray(new CommentBean[0]);
	}

	public void addFavorite() {
		this.favoriteCount++;
	}

	public void removeFavorite() {
		this.favoriteCount--;
	}

	public Integer getFavorite() {
		return (Integer) this.favoriteCount;
	}
}
