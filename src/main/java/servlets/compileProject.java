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
 * Servlet implementation class compileProject
 */
@WebServlet("/compileProject")
public class compileProject extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        String param = new String();
        boolean isProjectModified = false;      

        param = request.getParameter("is_project_modified");
        isProjectModified = Boolean.valueOf(param);

        UserBean userBean = null;
        userBean = (UserBean) request.getSession().getAttribute("userBean");
        
        if (isProjectModified) {
            boolean compileResult = false;
            compileResult = userBean.runtimeController.compile(userBean.getUser(), userBean.getCurrProj(), isProjectModified);
        }
        
        userBean.getCurrProj().setExecuteResult("");
        
        RequestDispatcher rd = request.getRequestDispatcher("online-BPEL-IDE.jsp");
        rd.forward(request, response);
    }
}