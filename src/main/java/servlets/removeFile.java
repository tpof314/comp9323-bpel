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
 * Servlet implementation class removeFile
 */
@WebServlet("/removeFile")
public class removeFile extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserBean userBean = null;
		boolean is_in_project = true;
		int directory_number = 0;
		int file_number = 0;
		boolean flag = true;

		userBean = (UserBean)request.getSession().getAttribute("userBean");
		is_in_project = (request.getParameter("is_in_project").equals("true")) ? true : false;
		if (!request.getParameter("directory_number").equals("null"))
			directory_number = Integer.valueOf(request.getParameter("directory_number"));
		file_number = Integer.valueOf(request.getParameter("file_number"));

		if(is_in_project) {
			flag = userBean.fileController.remove(userBean.getCurrProj().getFiles().get(file_number));
			if (flag)
				userBean.getCurrProj().getFiles().remove(file_number);
		}
		else {
			flag = userBean.fileController.remove(userBean.getCurrProj().getDirs().get(directory_number).getFiles().get(file_number));
			if (flag)
				userBean.getCurrProj().getDirs().get(directory_number).getFiles().remove(file_number);
		}
        RequestDispatcher rd = request.getRequestDispatcher("online-BPEL-IDE.jsp");
        rd.forward(request, response);
        
	}

}
