/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import controller.tools.MongoDBConnector;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Assignment;
import model.Project;
import model.Submission;
import model.User;

/**
 *
 * @author z3409413
 */
public class MongoTest {
	
	public static void main(String args[]) {
		String userId = "teacher_id_for_test";
		String userName = "Denis";
		String userType = "teacher";
		List<Project> userProjects = null;

		User user = new User(userId, userName, userType, userProjects);
		
		MongoDBConnector mongo = new MongoDBConnector();
		
		// 9. Add assignment
		Assignment ass2 = new Assignment();
		ass2.setAssNo(4);
		ass2.setAssName("Another Example");
		ass2.setDeadline(new Date());
		ass2.setSpecification("http://www.google.com");
		
		System.out.println(mongo.addAssToDatabaseByTeacher(userName, ass2));
	}
}
