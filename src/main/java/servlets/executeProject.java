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
 * Servlet implementation class executeProject
 */
@WebServlet("/executeProject")
public class executeProject extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserBean userBean = null;
		boolean isProjectModified = false;
		String function = new String();
		String inputParam = new String();
		boolean executeResult = false;

        userBean = (UserBean)request.getSession().getAttribute("userBean");
        isProjectModified = Boolean.valueOf(request.getParameter("is_project_modified"));
        function = request.getParameter("service");
        inputParam = request.getParameter("file_name");

        executeResult = userBean.runtimeController.execute(userBean.getUser(), userBean.getCurrProj(), function, inputParam, isProjectModified);
        
        RequestDispatcher rd = request.getRequestDispatcher("online-BPEL-IDE.jsp");
        rd.forward(request, response);
	}

}
