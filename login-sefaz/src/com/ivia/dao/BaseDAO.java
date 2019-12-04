package com.ivia.dao;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ivia.exception.DAOException;

public abstract class BaseDAO<T> {

	protected String tableName = new String();

	protected String sequenceName = new String();

	protected  List<Object> getList(String sql) throws DAOException
	{
		return getList(sql, new Object[] {});
	}

	protected List<Object> getList(String sql, int startRow, int pageSize) throws DAOException
	{
		return getList(sql, new Object[] {}, startRow, pageSize);
	}
	
	protected List<Object> getList(String sql, Object param) throws DAOException
	{
		return getList(sql, new Object[] {param});
	}

	protected List<Object> getListNoCast(String sql, Object param) throws DAOException
	{
		return getListNoCast(sql, new Object[] {param});
	}
	
	protected List<Object> getList(String sql, Object[] params) throws DAOException
	{
		return getList(sql, params, 1, 0);
	}

	protected List<Object> getListNoCast(String sql, Object[] params) throws DAOException
	{
		return getListNoCast(sql, params, 1, 0);
	}
	
	protected List<Object> getList(String sql, Object[] params, int startRow, int pageSize, Connection c) {
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			ps = c.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i + 1, params[i]);
				}
			}
			rs = ps.executeQuery();

			for (int i = 1; i < startRow; i++) {
				rs.next();
			}

			int count = 0;
			while (rs.next() && (pageSize == 0 || count < pageSize)) {
				Object obj = processRow(rs);
				list.add(obj);
				count++;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println(sql);
			System.out.println(params);
			
			e.printStackTrace();
			throw new DAOException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());

			}
		}
		return list;
	}

	protected List<Object> getList(String sql, Object[] params, int startRow, int pageSize)
	{
		List<Object> list = new ArrayList<Object>();
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try
		{
			c = SQLConnectionManager.getInstance().getConnection();
			ps = c.prepareStatement(sql);
			if (params != null)
			{
				for (int i=0; i<params.length; i++)
				{
					ps.setObject(i+1, params[i]);
				}
			}
			rs = ps.executeQuery();
			
			for (int i=1; i<startRow; i++)
			{
				rs.next();
			}
			
			int count = 0;
			while (rs.next() && (pageSize==0 || count<pageSize))
			{
				Object obj = processRow(rs);
				list.add(obj);
				count++;
			}

		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			System.out.println(sql);
			System.out.println(params);
			
			e.printStackTrace();
			throw new DAOException(e);
		}
		finally
		{
			 try {
			        if (rs != null) {
			                rs.close();
			        }
			        if (ps != null) {
			                ps.close();
			        }
			        if (c != null) {
			                c.close();
			        }
			    } catch (SQLException e) {
			    	System.out.println(e.getMessage());

			    }
		}
		return list;
	}

	protected Object executeQuery(String sql) throws DAOException, Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.newDocument();
	    Element results = doc.createElement("itens");
	    doc.appendChild(results);

		Connection c = null;
		ResultSet rs = null;
		try
		{
			c = SQLConnectionManager.getInstance().getConnection();
		    rs = c.createStatement().executeQuery(sql);

		    ResultSetMetaData rsmd = rs.getMetaData();

		    int colCount = rsmd.getColumnCount();
		    while (rs.next()) {
		      Element row = doc.createElement("item");
		      results.appendChild(row);
		      for (int i = 1; i <= colCount; i++) {
		        String columnName = rsmd.getColumnName(i);
		        Object value = rs.getObject(i);
		        
		        try {
		            row.setAttribute(columnName, value.toString());
		        } catch (Exception e) {
		            row.setAttribute(columnName, "");
		        }
		        
		      }
		    }
		    DOMSource domSource = new DOMSource(doc);
		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		    StringWriter sw = new StringWriter();
		    StreamResult sr = new StreamResult(sw);
		    transformer.transform(domSource, sr);

			return sw.toString();

		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			System.out.println(sql);
			
			e.printStackTrace();
			throw new DAOException(e);
		}
		finally
		{
			 try {
			        if (rs != null) {
			                rs.close();
			        }
			        if (c != null) {
			                c.close();
			        }
			    } catch (SQLException e) {
			    	System.out.println(e.getMessage());
			    }
		}
	}

	protected void executeDDL(String sql) throws DAOException, Exception
	{

		Connection c = null;
		ResultSet rs = null;
		try
		{
			c = SQLConnectionManager.getInstance().getConnection();
		    rs = c.createStatement().executeQuery(sql);

		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			System.out.println(sql);
			
			e.printStackTrace();
			throw new DAOException(e);
		}
		finally
		{
			 try {
			        if (rs != null) {
			                rs.close();
			        }
			        if (c != null) {
			                c.close();
			        }
			    } catch (SQLException e) {
			    	System.out.println(e.getMessage());
			    }
		}
	}
	
	protected Object executeQuery(String sql, String name, String title) throws DAOException, Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.newDocument();
	    
	    Element results = doc.createElement("itens");
	    doc.appendChild(results);

	    Element resume = doc.createElement("resume");
	    results.appendChild(resume);
	    
	    resume.setAttribute("NAME", name);
	    resume.setAttribute("TITLE", title);
	    resume.setAttribute("QUERY", sql);

		Connection c = null;
		ResultSet rs = null;
		try
		{
			c = SQLConnectionManager.getInstance().getConnection();
		    rs = c.createStatement().executeQuery(sql);

		    ResultSetMetaData rsmd = rs.getMetaData();

		    int colCount = rsmd.getColumnCount();
		    while (rs.next()) {
		      Element row = doc.createElement("item");
		      results.appendChild(row);
		      for (int i = 1; i <= colCount; i++) {
		        String columnName = rsmd.getColumnName(i);
		        Object value = rs.getObject(i);
		        
		        try {
		            row.setAttribute(columnName, value.toString());
		        } catch (Exception e) {
		            row.setAttribute(columnName, "");
		        } 
		      }
		    }
		    DOMSource domSource = new DOMSource(doc);
		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		    StringWriter sw = new StringWriter();
		    StreamResult sr = new StreamResult(sw);
		    transformer.transform(domSource, sr);

			return sw.toString();

		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			System.out.println(sql);
			
			e.printStackTrace();
			throw new DAOException(e);
		}
		finally
		{
			 try {
			        if (rs != null) {
			                rs.close();
			        }
			        if (c != null) {
			                c.close();
			        }
			    } catch (SQLException e) {
			    	System.out.println(e.getMessage());
			    }
		}
	}

	protected List<Object> getListNoCast(String sql, Object[] params, int startRow, int pageSize) throws DAOException
	{
		List<Object> list = new ArrayList<Object>();
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try
		{
			c = SQLConnectionManager.getInstance().getConnection();
			ps = c.prepareStatement(sql);
			if (params != null)
			{
				for (int i=0; i<params.length; i++)
				{
					ps.setObject(i+1, params[i]);
				}
			}
			rs = ps.executeQuery();
			
			for (int i=1; i<startRow; i++)
			{
				rs.next();
			}
			
			int count = 0;
			while (rs.next() && (pageSize==0 || count<pageSize))
			{
				Object obj = processRowNoCast(rs);
				list.add(obj);
				count++;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			System.out.println(sql);
			System.out.println(params);
			
			e.printStackTrace();
			throw new DAOException(e);
		}
		finally
		{
			 try {
			        if (rs != null) {
			                rs.close();
			        }
			        if (ps != null) {
			                ps.close();
			        }
			        if (c != null) {
			                c.close();
			        }
			    } catch (SQLException e) {
			    	System.out.println(e.getMessage());
			    }
		}
		return list;
	}
	
	protected Object getItem(String sql, Object pk) throws DAOException
	{
		Object[] params = { pk };
		List<Object> list = getList(sql, params);
		if (list != null && list.size()>0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}
	
	protected Object getItem(String sql, Object pk, Connection c) throws DAOException {
		Object[] params = { pk };
		List<Object> list = getList(sql, params, 1, 0, c);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	protected Object getItem(String sql, Object[] params) throws DAOException
	{
		List<Object> list = getList(sql, params);
		if (list != null && list.size()>0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}
	
	protected Object getItemNoCast(String sql, Object pk) throws DAOException
	{
		Object[] params = { pk };
		List<Object> list = getListNoCast(sql, params);
		if (list != null && list.size()>0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}
	
	protected Object getItemNoCast(String sql, Object[] params) throws DAOException
	{
		List<Object> list = getListNoCast(sql, params);
		if (list != null && list.size()>0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}
		
	protected Object processRow(ResultSet rs) throws SQLException
	{
		throw new SQLException("Row processor not implemented");
	}
	
	public Object parseObject(String json) throws Exception
	{
		throw new Exception("Parse Object not implemented");
	}
	
	protected Object processRowNoCast(ResultSet rs) throws SQLException
	{
		throw new SQLException("Row processor not implemented");
	}
	
	protected int insertItem(String sql, Object[] params, String sequenceName, Connection c)
			throws DAOException {
		
		try {
			updateItem(sql, params, c);

			if (sequenceName.length() > 0) {
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery("SELECT " + sequenceName + ".CURRVAL VALUE FROM DUAL");
				rs.next();
				int pk = rs.getInt("VALUE");
				rs.close();
				return pk;
			} else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		} 
	}
	
	protected int insertItem(String sql, Object[] params, String sequenceName) throws DAOException
	{
		Connection c = null;
		try
		{
			c = SQLConnectionManager.getInstance().getConnection();
			updateItem(sql, params, c);
	
			
			if (sequenceName.length() > 0) {
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery("SELECT " + sequenceName + ".CURRVAL VALUE FROM DUAL");
				rs.next();
				int pk = rs.getInt("VALUE");
				rs.close();
				return pk;
			} else {
				return 0;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			System.out.println(sql);
			System.out.println(params);
			
			e.printStackTrace();
			throw new DAOException(e);
		}
		finally
		{
			 try {
			        if (c != null) {
			                c.close();
			        }
			    } catch (SQLException e) {
			    	System.out.println(e.getMessage());
			    }
		}
	}

	protected int insertItemMySql(String sql, Object[] params) throws DAOException
	{
		Connection c = null;
		try
		{
			c = SQLConnectionManager.getInstance().getConnection();
			
			updateItem(sql, params, c);
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID()");
			rs.next();
			int pk = rs.getInt(1);
			rs.close();
			return pk;
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			System.out.println(sql);
			System.out.println(params);
			
			e.printStackTrace();
			throw new DAOException(e);
		}
		finally
		{
			 try {
			        if (c != null) {
			                c.close();
			        }
			    } catch (SQLException e) {
			    	System.out.println(e.getMessage());

			    }
		}
	}

	public int updateItem(String sql, Object[] params) throws DAOException
	{
		Connection c = null;
		try
		{
			c = SQLConnectionManager.getInstance().getConnection();					
			return updateItem(sql, params, c);
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			System.out.println(sql);
			System.out.println(params);
			
			e.printStackTrace();
			throw new DAOException(e);
		}
		finally
		{
			 try {
			        if (c != null) {
			                c.close();
			        }
			    } catch (SQLException e) {
			    	System.out.println(e.getMessage());

			    }
		}
	}

	public int deleteItem(String sql, Object[] params) throws DAOException
	{
		Connection c = null;
		try
		{
			c = SQLConnectionManager.getInstance().getConnection();
			return updateItem(sql, params, c,true);
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			System.out.println(sql);
			System.out.println(params);
			
			e.printStackTrace();
			throw new DAOException(e);
		}
		finally
		{
			 try {
			        if (c != null) {
			                c.close();
			        }
			    } catch (SQLException e) {
			    	System.out.println(e.getMessage());

			    }
		}
	}
	
	public int deleteItem(String sql, Object[] params, Connection c) throws DAOException {
		try {
			return updateItem(sql, params, c, true);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int updateItem(String sql, Object[] params, Connection connection) throws DAOException
	{
		return updateItem(sql, params, connection, false);
	}
	
	public int updateItem(String sql, Object[] params, Connection connection, Boolean isDelete) throws DAOException {
		int rows = -1;
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i + 1, params[i]);
				}
			}
			if (isDelete == true) {
				ps.execute();
				rows = 0;
			} else {
				rows = ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return rows;
	}

	
}
