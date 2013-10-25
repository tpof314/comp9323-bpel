package servlets;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import model.User;
import model.bean.UserBean;

/**
 * Servlet implementation class register
 */
@WebServlet("/register")
public class register extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		UserBean userBean = null;
		try {
			userBean = new UserBean();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		userBean.userController.createUser(username, password);
		User user = userBean.userController.getUserByName(username, password);
		
		userBean.setUser(user);
        userBean.setAssignments(userBean.assignmentController.getUserAssignments(user));
		request.getSession().setAttribute("userBean", userBean);
		RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
        rd.forward(request, response);

	}

}
