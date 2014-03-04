package edu.asupoly.ser422.lab4.dao;

import java.io.InputStream;
import java.util.Properties;

// Classic Singleton pattern for DAO
public final class NewsDAOFactory {
	private static INewsDAO __theDAO = null;

	private NewsDAOFactory() {
	}

	public static final INewsDAO getTheDAO() {
		if (__theDAO == null) {

			InputStream is = null;
			try {
				is = NewsDAOFactory.class.getClassLoader().getResourceAsStream(
						"dao.properties");
				if (is != null) {
					Properties p = new Properties();
					p.load(is);
					String daoClassName = p.getProperty("dao.class");
					if (daoClassName
							.equals("edu.asupoly.ser422.lab4.dao.NewsPeriodicDAO")) {
						String mvp = p.getProperty("dao.settings");
						if (mvp != null && mvp.length() > 0) {
							String[] params = mvp.split(",");
							__theDAO = new NewsPeriodicDAO(params[0], params[1]);
						} else {
							__theDAO = new NewsPeriodicDAO();
						}
					} else {
						// Implementing class must have a default constructor
						Class<?> daoClass = Class.forName(daoClassName);
						__theDAO = (INewsDAO) daoClass.newInstance();
					}
					// really need a logger
					System.out.println("Created DAO: " + daoClassName);
				} else {
					__theDAO = new NewsDefaultDAO();
					System.out.println("Created DAO: NewsDefaultDAO");
				}
			} catch (Throwable t) { // in case we barf on anything
				t.printStackTrace();
				__theDAO = new NewsDefaultDAO();
				System.out
						.println("Problem creating DAO, instead reated DAO: NewsDefaultDAO");
			}
		}
		return __theDAO;
	}
}