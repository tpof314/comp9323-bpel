package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.bean.UserBean;

/**
 * Servlet implementation class createProject
 */
@WebServlet("/createProject")
public class createProject extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserBean userBean = (UserBean)request.getSession().getAttribute("userBean");
		String projName = new String();
		boolean successFlag = false;
		
		projName = request.getParameter("project_name");
		successFlag = userBean.userController.createProject(userBean.getUser(), projName);
		
		RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
        rd.forward(request, response);
	}

}
