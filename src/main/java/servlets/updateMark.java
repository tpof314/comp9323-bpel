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
 * Servlet implementation class updateMark
 */
@WebServlet("/updateMark")
public class updateMark extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserBean userBean = null;
		userBean = (UserBean)request.getSession().getAttribute("userBean");
		int assignment_id = Integer.valueOf(request.getParameter("assignment_id"));
		int student_id = Integer.valueOf(request.getParameter("student_id"));
		double mark = Double.valueOf(request.getParameter("mark"));
		
		userBean.getAssignments().get(assignment_id).getSubmitRecord().get(student_id).setMark(mark);
		userBean.assignmentController.markSubmission(userBean.getUser(), userBean.getAssignments().get(assignment_id).getAssID(), userBean.getAssignments().get(assignment_id).getSubmitRecord().get(student_id));
		
		RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
        rd.forward(request, response);
	}

}
