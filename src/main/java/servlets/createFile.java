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
 * Servlet implementation class createFile
 */
@WebServlet("/createFile")
public class createFile extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserBean userBean = null;
		String file_name = null;
		userBean = (UserBean)request.getSession().getAttribute("userBean");
		file_name = request.getParameter("file_name");
        
        if (file_name.endsWith(".bpel")) {
        	userBean.directoryController.createFile(userBean.getCurrProj().getDirs().get(0), file_name);
        }
        else if (file_name.endsWith(".wsdl")) {
        	userBean.directoryController.createFile(userBean.getCurrProj().getDirs().get(1), file_name);
        }
        else if (file_name.endsWith(".soap")) {
        	userBean.directoryController.createFile(userBean.getCurrProj().getDirs().get(2), file_name);
        }
        else {
        	userBean.projectController.createFile(userBean.getCurrProj(), file_name);
        }
        
        RequestDispatcher rd = request.getRequestDispatcher("online-BPEL-IDE.jsp");
        rd.forward(request, response);
        
	}
	
}
