package com.ivia.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ivia.model.UserPhone;

public class UserPhoneDao {

	public boolean insertUserPhone(UserPhone userPhone) throws ClassNotFoundException {
		boolean status = false;

		Class.forName("com.mysql.jdbc.Driver");

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql_database?useSSL=false", "root", "");

			// Step 2:Create a statement using connection object
			PreparedStatement preparedStatement = connection.prepareStatement(
					"INSERT INTO user_phone "
					+ " (phone_number, user_email, ddd, tp_phone) values (?, ?, ?, ?) ")) {
			preparedStatement.setString(1, userPhone.getPhoneNumber());
			preparedStatement.setString(2, userPhone.getUserEmail());
			preparedStatement.setInt(3, userPhone.getDdd());
			preparedStatement.setString(4, userPhone.getTpPhone());

			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			status = rs.next();

		} catch (SQLException e) {
			// process sql exception
			printSQLException(e);
		}
		return status;
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}
