package edu.asupoly.ser422.lab4.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import edu.asupoly.ser422.lab4.model.CommentBean;
import edu.asupoly.ser422.lab4.model.NewsItemBean;
import edu.asupoly.ser422.lab4.model.UserBean;

public class NewsPeriodicDAO extends NewsDefaultDAO {
	private static final String __STATE_FILENAME = "/tmp/news.ser";

	private Timer __timer;
	private String __store = null;

	NewsPeriodicDAO(long seconds, String fn) {
		super();
		// read in any objects we may have out on disk
		if (fn != null) {
			__store = fn;
		} else {
			__store = __STATE_FILENAME;
		}
		// __loadState();

		// Now create a timer
		__timer = new Timer();
		// write the OOS periodically every seconds
		__timer.schedule(new NewsTimerTask(), 0L, seconds * 1000L);
	}

	NewsPeriodicDAO() {
		this(180L, __STATE_FILENAME); // 3 minute default
	}

	NewsPeriodicDAO(String[] params) {
		this(params[0], params[1]);
	}

	NewsPeriodicDAO(String secs, String filename) {
		this(Integer.parseInt(secs), filename);
	}

	public void finalize() {
		if (__timer != null) {
			__timer.cancel();
			// if we don't do this the Thread may keep going
		}
	}

	@SuppressWarnings("unchecked")
	private void __loadState() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(__store));
			__comments = (Hashtable<String, CommentBean>) ois.readObject();
			if (ois.readObject() != null)
				__users = (Hashtable<String, UserBean>) ois.readObject();
			__news = (Hashtable<Integer, NewsItemBean>) ois.readObject();
		} catch (Throwable t) {
			System.out.println("Unable to load state file, proceeding empty!");
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (java.io.IOException ie) {
					System.out.println("Unable to close ois stream");
				}
			}
		}
	}

	private class NewsTimerTask extends TimerTask {
		@Override
		public void run() {
			// again need logging
			System.out.println("Writing " + __store);
			ObjectOutputStream oos = null;
			synchronized (this) {
				try {
					// write the 3 Hashtables out to a serialized object
					oos = new ObjectOutputStream(new FileOutputStream(__store));
					oos.writeObject(__comments);
					oos.writeObject(__users);
					oos.writeObject(__news);
					System.out.println("Wrote " + __store);
				} catch (Throwable t) {
					t.printStackTrace();
				} finally {
					if (oos != null) {
						try {
							oos.close();
						} catch (Throwable t) {
							t.printStackTrace();
						}
					}
				}
			}
		}
	}
}