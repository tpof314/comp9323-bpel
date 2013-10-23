package servlets;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.bean.UserBean;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class uploadFile
 */
@WebServlet("/uploadFile")
public class uploadFile extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private ServletFileUpload uploader = null;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserBean userBean = null;
    	String fileName = new String();
        String fileContent = new String();
        
        userBean = (UserBean)request.getSession().getAttribute("userBean");
        
		DiskFileItemFactory fileFactory = new DiskFileItemFactory();
    	File filesDir = (File) getServletContext().getAttribute("FILES_DIR_FILE");
    	fileFactory.setRepository(filesDir);
    	this.uploader = new ServletFileUpload(fileFactory);

        try {

        	List<FileItem> multiparts = uploader.parseRequest(request);
        	Iterator<FileItem> fileItemsIterator = multiparts.iterator();
        	while(fileItemsIterator.hasNext()){
        		FileItem fileItem = fileItemsIterator.next();
        		fileName = fileItem.getName();
        		fileContent = fileItem.getString();
        		if (fileName.endsWith(".bpel")) {
                	userBean.directoryController.uploadFile(userBean.getCurrProj().getDirs().get(0), fileName, fileContent);
                }
                else if (fileName.endsWith(".wsdl")) {
                	userBean.directoryController.uploadFile(userBean.getCurrProj().getDirs().get(1), fileName, fileContent);
                }
                else if (fileName.endsWith(".soap")) {
                	userBean.directoryController.uploadFile(userBean.getCurrProj().getDirs().get(2), fileName, fileContent);
                }
                else {
                	userBean.projectController.uploadFile(userBean.getCurrProj(), fileName, fileContent);
                }
        	}

        } catch (Exception ex) {
        	
        	RequestDispatcher rd = request.getRequestDispatcher("online-BPEL-IDE.jsp");
    	    rd.forward(request, response);
        }
        
        RequestDispatcher rd = request.getRequestDispatcher("online-BPEL-IDE.jsp");
	    rd.forward(request, response);
        
	}

}