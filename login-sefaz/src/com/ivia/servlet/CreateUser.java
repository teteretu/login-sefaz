package com.ivia.servlet;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ivia.dao.UserDAO;
import com.ivia.model.User;

/**
 * @email Ramesh Fadatare
 */

@WebServlet("/createUser")
public class CreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String userName = request.getParameter("user_name");
		
		User user = new User();
		UserDAO userDAO = UserDAO.getInstance();
		
		try {
			user = userDAO.getUser(email);
			
			if (user != null) {

				user.setPassword(password);
				user.setUserName(userName);
				
				System.out.println("User updated");

				HttpSession session = request.getSession();
				session.setAttribute("user", userDAO.updateUser(user));
				
				response.sendRedirect("listUsers.jsp");
				
			} else {
				user = new User();
				
				user.setEmail(email);
				user.setUserName(userName);
				user.setPassword(password);

				System.out.println("User inserted");
				HttpSession session = request.getSession();
				session.setAttribute("user", userDAO.insertUser(user));
				
				response.sendRedirect("listUsers.jsp");
				
			}
			
//			HttpSession session = request.getSession();
//			session.setAttribute("email", email);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
