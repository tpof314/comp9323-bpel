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
 * Servlet implementation class removeAssignment
 */
@WebServlet("/removeAssignment")
public class removeAssignment extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserBean userBean = null;
		userBean = (UserBean)request.getSession().getAttribute("userBean");
		int assignment_id = Integer.valueOf(request.getParameter("assignment_id"));
		boolean flag = false;
		
		flag = userBean.assignmentController.removeAssignment(userBean.getUser(), userBean.getAssignments().get(assignment_id));
		
		if(flag)
			userBean.getAssignments().remove(assignment_id);
		
		RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
        rd.forward(request, response);
	}

}
