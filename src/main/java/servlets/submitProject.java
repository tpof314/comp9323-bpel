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
 * Servlet implementation class submitProject
 */
@WebServlet("/submitProject")
public class submitProject extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserBean userBean = null;
		userBean = (UserBean)request.getSession().getAttribute("userBean");
		int assignment_id = Integer.valueOf(request.getParameter("assignment_id"));
		int project_id = Integer.valueOf(request.getParameter("project_id"));
		
		userBean.assignmentController.submitAssignment(userBean.getUser(), userBean.getAssignments().get(assignment_id), userBean.getAssignments().get(assignment_id).getSubmitRecord().get(0), userBean.getUser().getUserProjects().get(project_id));
		
		RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
        rd.forward(request, response);
	}

}
