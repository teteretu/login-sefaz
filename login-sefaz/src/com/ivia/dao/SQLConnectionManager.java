package com.ivia.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class SQLConnectionManager {

	private static SQLConnectionManager instance;
	private ComboPooledDataSource cpds;

	private String url;
	private String user;
	private String password;
	private String driver;
	private String minPoolSize;
	private String maxPoolSize;
	private String incrementPoolSize;

	private SQLConnectionManager() {

		cpds = new ComboPooledDataSource();

		try {
			ResourceBundle bundle = ResourceBundle.getBundle("portal");

			driver = bundle.getString("jdbc.mysql.driver");

			Class.forName(driver);

			url = bundle.getString("jdbc.mysql.url");

			user = bundle.getString("jdbc.mysql.user");

			password = bundle.getString("jdbc.mysql.password");

			minPoolSize = bundle.getString("c3po.mysql.MinPoolSize");
			maxPoolSize = bundle.getString("c3po.mysql.MaxPoolSize");

			incrementPoolSize = bundle.getString("c3po.mysql.AcquireIncrement");

			cpds.setDriverClass(driver);

			cpds.setJdbcUrl( url );
			cpds.setUser(user);
			cpds.setPassword(password);

			// config tamanho do pool
			cpds.setMinPoolSize(Integer.parseInt(minPoolSize));
			cpds.setAcquireIncrement(Integer.parseInt(incrementPoolSize));
			cpds.setMaxPoolSize(Integer.parseInt(maxPoolSize));

		} catch(PropertyVetoException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: Error loading JDBC driver. Class " + driver + " not found.");
		} 


	}

	public static SQLConnectionManager getInstance()  {
		if(instance == null) {
			instance = new SQLConnectionManager();
		}
		return instance;
	}

	public Connection getConnection() throws SQLException {
		return cpds.getConnection();
	}


}