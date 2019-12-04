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
        
		for (User user : users) {
			printWriter.print("<p> Email :: " + user.getEmail() + "</p>");
	        printWriter.print("<p> Password :: " + user.getPassword() + "</p>");
	        printWriter.print("<p> UserName :: " + user.getUserName() + "</p>");
	        
	        usersPhone = userPhoneDAO.getUserPhoneByWhere(" AND user_email = '" + user.getEmail() + "' ");
	        
	        if (usersPhone != null && usersPhone.size() > 0) {
	        	int i = 0;
	        	for (UserPhone userPhone : usersPhone) {
	        		i++;
	        		printWriter.print("<p> Telefone " + i + " :: (" + userPhone.getDdd() + ") " + userPhone.getPhoneNumber() + "</p>");
	        	}
	        }
		}
        
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
