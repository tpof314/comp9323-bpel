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
 * Servlet implementation class removeProject
 */
@WebServlet("/removeProject")
public class removeProject extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserBean userBean = (UserBean)request.getSession().getAttribute("userBean");
		String id = new String();
		boolean successFlag = false;
		
		id = request.getParameter("id");
		successFlag = userBean.userController.removeProject(userBean.getUser(), userBean.getUser().getUserProjects().get(Integer.valueOf(id)));
		if(successFlag){
			userBean.getUser().getUserProjects().remove(Integer.valueOf(id));
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
        rd.forward(request, response);
	}

}
