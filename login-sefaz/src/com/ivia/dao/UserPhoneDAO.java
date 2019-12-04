package com.ivia.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ivia.model.UserPhone;

public class UserPhoneDAO extends BaseDAO {

	private static UserDAO instance;
	
	public UserPhoneDAO() {
		this.tableName = new String(" user_phone ");
	}
	
	public static UserDAO getInstance() {
		if(instance == null)
			instance = new UserDAO();
		return instance;
	}

	public UserPhone getUserPhone(String email) {
		try {
			return (UserPhone) getItem("SELECT * FROM " + this.tableName + " WHERE email = ?",
					new Object[] { email });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<UserPhone> getUserPhoneByWhere(String where) {
		try {
			return (List<UserPhone>) getList("SELECT * FROM " + this.tableName + " WHERE 1=1 " + where);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public UserPhone insertUserPhone(UserPhone userPhone) throws Exception {
 
		insertItemMySql("INSERT INTO " + this.tableName + 
								 "(phone_number, user_email, ddd, tp_phone) " + 
								 " VALUES (?, ?, ?, ?)",
				new Object[] { userPhone.getPhoneNumber(),
						userPhone.getUserEmail(),
						userPhone.getDdd(),
						userPhone.getTpPhone() });


		return userPhone;
	}
	
	public UserPhone updateUserPhone(UserPhone userPhone) throws Exception {
		
        int rows = updateItem("UPDATE " + this.tableName +
                        " SET user_email = ?, "
                        + " ddd = ? "
                        + " tp_phone = ? " +
	                        	"WHERE phone_number = ?",
                new Object[]{userPhone.getUserEmail(),
	                        userPhone.getDdd(),
	                        userPhone.getTpPhone(),
	                        userPhone.getPhoneNumber() });
        
        if(rows == 0){
            throw new Exception("Item not found");
        }
        
        return getUserPhone(userPhone.getPhoneNumber());
    }
	
	public int deleteUserPhone(UserPhone userPhone) throws Exception{
    	
        int rows = deleteItem("DELETE FROM " + this.tableName + " WHERE phone_number = ?",
                new Object[]{userPhone.getPhoneNumber()});

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
    protected UserPhone processRow(ResultSet rs) throws SQLException
    {
    	UserPhone userPhone = new UserPhone();

    	userPhone.setPhoneNumber(rs.getString("phone_number"));
    	userPhone.setUserEmail(rs.getString("user_email"));
    	userPhone.setDdd(rs.getInt("ddd"));
    	userPhone.setTpPhone(rs.getString("tp_phone"));

        return userPhone;
    }

}
