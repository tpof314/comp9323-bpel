/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import controller.dataController.ProjectController;
import controller.dataController.UserController;
import controller.runtime.RuntimeController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import model.Assignment;

import model.Project;
import model.Submission;
import model.User;
import model.bean.UserBean;

/**
 *
 * @author Administrator
 */
public class test {

	public static void main(String args[]) throws GeneralSecurityException, IOException, URISyntaxException, ParserConfigurationException {
		UserBean userBean = new UserBean();
		boolean flag = false;
//		
//        try {
//            userController = new UserController();
//        } catch (GeneralSecurityException ex) {
//            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
//        }
//		
		User user = userBean.userController.getUserByName("TeacherHuang", "123456");
		//User user = userBean.userController.getUserByName("TeacherHuang", "123456");
		System.out.println(user);
//        
//        ProjectController projectController = null;
//        
//        try {
//             projectController = new ProjectController();
//        } catch (Exception ex) {
//            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        //Get the first project.
		//Project proj = userBean.projectController.loadProject(user, user.getUserProjects().get(0).getProjName(), user.getUserProjects().get(0).getProjId());
//        
//        UserBean userBean = new UserBean();
		userBean.setUser(user);
		//userBean.setCurrProj(proj);
		//create assignment 3.
		  /*
		 Assignment newAss = new Assignment();
		 newAss.setAssNo(3);
		 newAss.setAssName("How to be a caonima?");
		 newAss.setSpecification("http://www.caonima.com/contact-us.pdf");
		 newAss.setDeadline(new Date());
		  
		 flag = userBean.assignmentController.updateAssignment(user, newAss);
		 System.out.println(flag);
		 */

		List<Assignment> ass = userBean.assignmentController.getUserAssignments(user);
		userBean.setAssignments(ass);
		
		//Update ass3.
		/*
		for (int i = 0; i < ass.size(); ++i) {
			if (ass.get(i).getAssNo() == 3) {
				ass.get(i).setAssNo(4);
				ass.get(i).setAssName("How to be a caonima?");
				ass.get(i).setSpecification("http://www.caonima.com/contact-us.pdf");
				ass.get(i).setDeadline(new Date());
				flag = userBean.assignmentController.updateAssignment(user, ass.get(i));
				System.out.println("Update assignment 3 "+ flag);
			}
		}
		*/
		System.out.println(userBean.getAssignments());
	
		/*
		for (int i = 0; i < ass.size(); ++i) {
			if (ass.get(i).getAssNo() == 4) {
				Submission sub = null;
				if(ass.get(i).getSubmitRecord().isEmpty()){
					sub = new Submission();
					sub.setStuName(user.getUserName());
				}
				else{
					sub = ass.get(i).getSubmitRecord().get(0);
				}
				flag = userBean.assignmentController.submitAssignment(user, ass.get(i), sub, user.getUserProjects().get(0));
				System.out.println("Submit assignment " + flag);
			}
		}
		* */
		
		for (int i = 0; i < ass.size(); ++i) {
			if (ass.get(i).getAssNo() == 3) {
				flag = userBean.assignmentController.removeAssignment(user, ass.get(i));
				System.out.println("Remove assignment " + flag);
			}
		}
		
	
//        System.out.println(proj);
//		
//		
//		//System.out.println(userBean.fileController.remove(userBean.getCurrProj().getFiles().get(3)));
//        //userBean.getCurrProj().getFiles().remove(3);
//		
//		RuntimeController runtime = new RuntimeController();
//		String result = runtime.compile(user, proj);
//		System.out.println(result);


	}
}
