package servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import model.Assignment;
import model.bean.UserBean;
import sun.misc.IOUtils;

/**
 * Servlet implementation class createAssignment
 */
@WebServlet("/createAssignment")
public class createAssignment extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private ServletFileUpload uploader = null;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserBean userBean = null;
		String assignment_name = null;
		String date_str = null;
		Date deadline = null;
		
		String dateFormat = "dd/MM/yyyy HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat("dateFormat");
		
		Assignment newAssignment = new Assignment();
		userBean  = (UserBean) request.getSession().getAttribute("userBean");
		
		assignment_name  = request.getParameter("new_assignment_name");
		date_str  = request.getParameter("new_assignment_deadline");
		try {
			deadline  = sdf.parse(date_str + " 23:59:59");
		} catch (ParseException ex) {
			RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
	        rd.forward(request, response);
		}
		
		DiskFileItemFactory fileFactory = new DiskFileItemFactory();
		File filesDir = (File) getServletContext().getAttribute("FILES_DIR_FILE");
		fileFactory.setRepository (filesDir);
		this.uploader  = new ServletFileUpload(fileFactory);
		
		String fileName = "";
		InputStream fileContent = null;
		
		int newAssNo = userBean.getAssignments().size() + 1;
		
		String specPath1 = "";
		String specPath2 = "";
		
		try {
	        	List<FileItem> multiparts = uploader.parseRequest(request);
	        	Iterator<FileItem> fileItemsIterator = multiparts.iterator();
	        	while(fileItemsIterator.hasNext()){
	        		
	        		FileItem fileItem = fileItemsIterator.next();
	        		fileName = fileItem.getName();
					
					String splittedFileName[] = fileName.split(".");
					
					specPath1 = "src" + File.separator + "main" + File.separator + "webapp" + File.separator;
					specPath2 = "assignments" + File.separator + "assignment" + newAssNo + "." + splittedFileName[splittedFileName.length-1];
					
	        		fileContent = fileItem.getInputStream();
					
					File spec = new File(specPath1 + specPath2);
					FileOutputStream fws = new FileOutputStream(spec);
					fws.write(IOUtils.readFully(fileContent, -1, false));
					
	        	}

	        } catch (Exception ex) {
	        	
	        	RequestDispatcher rd = request.getRequestDispatcher("online-BPEL-IDE.jsp");
	    	    rd.forward(request, response);
	        }
		
		newAssignment.setAssNo(newAssNo);
		newAssignment.setAssName(assignment_name);
		newAssignment.setDeadline(deadline);
		newAssignment.setSpecification("localhost:8080/comp9323-bpel/" + specPath2);
		
		RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
	    rd.forward(request, response);
	}

}
