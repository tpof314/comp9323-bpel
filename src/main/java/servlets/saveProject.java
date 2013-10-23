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
 * Servlet implementation class saveProject
 */
@WebServlet("/saveProject")
public class saveProject extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserBean userBean = null;
		userBean = (UserBean)request.getSession().getAttribute("userBean");

		userBean.projectController.saveToServer(userBean.getCurrProj());
		userBean.projectController.removeProject(userBean.getCurrProj());

	    RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
	    rd.forward(request, response);
	}

}