package com.ivia.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ivia.model.User;

public class UserDAO extends BaseDAO {

	private static UserDAO instance;
	
	public UserDAO() {
		this.tableName = new String(" user ");
	}
	
	public static UserDAO getInstance() {
		if(instance == null)
			instance = new UserDAO();
		return instance;
	}

	public boolean validate(User user) throws ClassNotFoundException {
//		boolean status = false;
		try {
			User usr = (User) getItem("select * from user where email = ? and password = ? ", 
				new Object[] { user.getEmail(), user.getPassword() });
			return usr != null && !usr.getEmail().equals("");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
//		Class.forName("com.mysql.jdbc.Driver");
//
//		try (Connection connection = DriverManager
//				.getConnection("jdbc:mysql://localhost:3306/mysql_database?useSSL=false", "root", "");
//
//				// Step 2:Create a statement using connection object
//				PreparedStatement preparedStatement = connection
//						.prepareStatement("select * from user where email = ? and password = ? ")) {
//			preparedStatement.setString(1, user.getEmail());
//			preparedStatement.setString(2, user.getPassword());
//
//			System.out.println(preparedStatement);
//			ResultSet rs = preparedStatement.executeQuery();
//			status = rs.next();
//
//		} catch (SQLException e) {
//			// process sql exception
//			printSQLException(e);
//		}
//		return status;
	}

	public User getUser(String email) {
		try {
			return (User) getItem("SELECT * FROM " + this.tableName + " WHERE email = ?",
					new Object[] { email });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getUserByWhere(String where) {
		try {
			return (List<User>) getList("SELECT * FROM " + this.tableName + " WHERE 1=1 " + where);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public User insertUser(User user) throws Exception {
 
		insertItemMySql("INSERT INTO " + this.tableName + 
								 "(email,"
								 + " password,"
								 + " user_name) " + 
								 " VALUES (?, ?, ?)",
				new Object[] { user.getEmail(), user.getPassword(), user.getUserName() });


		return user;
	}
	
	public User updateUser(User user) throws Exception {
		
        int rows = updateItem("UPDATE " + this.tableName +
                        " SET password = ?, "
                        + " user_name = ? " +
	                        	"WHERE email = ?",
                new Object[]{user.getPassword(),
	                        user.getUserName(),
	                        user.getEmail()});
        
        if(rows == 0){
            throw new Exception("Item not found");
        }
        
        return getUser(user.getEmail());
    }
	
	public int deleteUser(User user) throws Exception{
    	
        int rows = deleteItem("DELETE FROM " + this.tableName + " WHERE email = ?",
                new Object[]{user.getEmail()});

        if(rows == -1){
            throw new Exception("Item not found");
        }

        return rows;
    }
	
	@Override
    protected Object processRowNoCast(ResultSet rs) throws SQLException {
        Map<String, String> map = new HashMap<String, String>();
        ResultSetMetaData rsmd = rs.getMetaData();

        int columnCount = rsmd.getColumnCount();

        for(int i = 1; i < columnCount + 1; i++){
            map.put(rsmd.getColumnLabel(i).toUpperCase(), rs.getString(i));
        }

        return map;
    }

    @Override
    protected User processRow(ResultSet rs) throws SQLException
    {
    	User user = new User();

    	user.setEmail(rs.getString("email"));
    	user.setPassword(rs.getString("password"));
    	user.setUserName(rs.getString("user_name"));

        return user;
    }

//	private void printSQLException(SQLException ex) {
//		for (Throwable e : ex) {
//			if (e instanceof SQLException) {
//				e.printStackTrace(System.err);
//				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
//				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
//				System.err.println("Message: " + e.getMessage());
//				Throwable t = ex.getCause();
//				while (t != null) {
//					System.out.println("Cause: " + t);
//					t = t.getCause();
//				}
//			}
//		}
//	}
}
