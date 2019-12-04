package com.ivia.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ivia.dao.UserDAO;
import com.ivia.dao.UserPhoneDAO;
import com.ivia.model.User;
import com.ivia.model.UserPhone;

/**
 * @email Ramesh Fadatare
 */

@WebServlet("/getUsers")
public class GetUsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDao;

	public void init() {
		new UserDAO();
		userDao = UserDAO.getInstance();
	}
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		List<User> users = userDao.getUserByWhere("");

        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.print("<html>");
        printWriter.print("<body>");
        
        UserPhoneDAO userPhoneDAO = new UserPhoneDAO();
        List<UserPhone> usersPhone = new ArrayList<UserPhone>();
        
        printWriter.print("<table>");
        printWriter.print("<thead>");
        printWriter.print("<tr>");
        printWriter.print("<td>Email</td>");
        printWriter.print("<td>Password</td>");
        printWriter.print("<td>Nome</td>");
        printWriter.print("<td>Telefones</td>");
        printWriter.print("</tr>");
        printWriter.print("</thead>");
		for (User user : users) {
			printWriter.print("<tr id='mainTr' name='mainTr'>");
			printWriter.print("<td> " + user.getEmail() + " </td>");
			printWriter.print("<td> " + user.getPassword() + " </td>");
			printWriter.print("<td> " + user.getUserName() + " </td>");
		    
	        usersPhone = userPhoneDAO.getUserPhoneByWhere(" AND user_email = '" + user.getEmail() + "' ");
	        
	        if (usersPhone != null && usersPhone.size() > 0) {
	        	int i = 0;
	        	for (UserPhone userPhone : usersPhone) {
	        		i++;
	        		printWriter.print("<td> Telefone " + i + " :: (" + userPhone.getDdd() + ") " + userPhone.getPhoneNumber() + "</td>");
	        	}
	        }
	        printWriter.print("</tr>");
		}
		printWriter.print("</table>");
        
        printWriter.print("</body>");
        printWriter.print("</html>");
        printWriter.close();

    }
	
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		String email = request.getParameter("email");
//		String password = request.getParameter("password");
//		User user = new User();
//		
//		user.setEmail(email);
//		user.setPassword(password);
//
//		try {
//			if (loginDao.validate(user)) {
//				//HttpSession session = request.getSession();
//				// session.setAttribute("username",username);
//				response.sendRedirect("listUsers.jsp");
//			} else {
//				HttpSession session = request.getSession();
//				session.setAttribute("email", email);
//				System.out.println("not logged");
//				response.sendRedirect("login.jsp");
//			}
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
}
