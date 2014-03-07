package edu.asupoly.ser422.lab4.model;

import java.util.ArrayList;

public class UserBean implements java.io.Serializable {
	private static final long serialVersionUID = -1408433159918540612L;

	public enum Role {
		GUEST, SUBSCRIBER, REPORTER;

		public String toString() {
			switch (this) {
			case GUEST:
				return "Guest";
			case SUBSCRIBER:
				return "Subscriber";
			case REPORTER:
				return "Reporter";
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	private String userId;
	private String passwd;
	private Role role;
	private ArrayList<Integer> favorites;

	public UserBean(String id, String passwd, Role r) {
		this.userId = id;
		this.passwd = passwd;
		this.role = r;
		favorites = new ArrayList<Integer>();
	}

	public UserBean(UserBean user, Role newRole) {
		this.userId = user.userId;
		this.passwd = user.passwd;
		this.role = newRole;
	}

	public ArrayList<Integer> getFavorites() {
		return favorites;
	}

	public void addToFavorites(int newsID) {
		favorites.add(newsID);
	}

	public void removeFromFavorites(int newsID) {
		favorites.remove((Integer) newsID);
	}

	public boolean isFavorite(int newsID) {
		if (favorites.contains((Integer) newsID))
			return true;
		return false;
	}

	public String getUserId() {
		return userId;
	}

	public String getPasswd() {
		return passwd;
	}

	public Role getRole() {
		return role;
	}

	public String toString() {
		return "User " + userId + " of Role " + role.toString();
	}

	@Override
	public Object clone() {
		return new UserBean(this, this.role);
	}

	public boolean isSubscriber() {
		if (role.equals(Role.SUBSCRIBER)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isReporter() {
		if (role.equals(Role.REPORTER)) {
			return true;
		} else {
			return false;
		}
	}

}
