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
		String userName = "TeacherHuang";
		String userType = "teacher";
		List<Project> userProjects = null;

		User user = new User(userId, userName, userType, userProjects);
		
		MongoDBConnector mongo = new MongoDBConnector();
		//mongo.insertUser(user, "123456");
		
		// 1. Create Assignment Test
		/*
		Assignment ass1 = new Assignment();
		ass1.setAssNo(1);
		ass1.setAssName("Simple Bpel introduction");
		ass1.setDeadline(new Date());
		ass1.setSpecification("http://www.baidu.com");
		
		Assignment ass2 = new Assignment();
		ass2.setAssNo(2);
		ass2.setAssName("Another Example");
		ass2.setDeadline(new Date());
		ass2.setSpecification("http://www.google.com");
		
		mongo.addAssToDatabaseByTeacher(userName, ass1);
		mongo.addAssToDatabaseByTeacher(userName, ass2);
		*/
		
		// 2. Submission Test
		String ass1_id = "52651bf2f0d895fe540f7896";
		String ass2_id = "52651bf2f0d895fe540f7897";
		/*
		Submission submit1 = new Submission();
		submit1.setProjID("123123123121");
		submit1.setStuName("Peter");
		submit1.setSubmitTime(new Date());
		
		Submission submit2 = new Submission();
		submit2.setProjID("123123123121666");
		submit2.setStuName("Duancy");
		submit2.setSubmitTime(new Date());
		
		Submission submit3 = new Submission();
		submit3.setProjID("123e23123a1666");
		submit3.setStuName("Lisa");
		submit3.setSubmitTime(new Date());
		
		System.out.println(mongo.addSubmissionByStudent(ass1_id, submit1));
		System.out.println(mongo.addSubmissionByStudent(ass1_id, submit2));
		System.out.println(mongo.addSubmissionByStudent(ass2_id, submit3));
		*/
		
		// 3. Update Submission
		/*
		Submission submit = new Submission();
		submit.setProjID("1181189876543210");
		submit.setStuName("Lisa");
		submit.setSubmitTime(new Date());
		
		mongo.addSubmissionByStudent(ass1_id, submit);
		*/
		
		// 4. Load Assignment By Teacher
		/*
		Assignment ass = mongo.loadAssByTeacher("TeacherHuang", ass1_id);
		*/
		
		// 5. Load Assignment By Student
		/*
		Assignment ass = mongo.loadAssByStudent("Lisa", ass1_id);
		*/
		
		// 6. Fetch All Assignment
		/*
		ArrayList<Assignment> assList = mongo.loadAllAssByStudent("Peter");
		*/
		
		// 7. Update Assignment By Teacher
		/*
		Assignment ass1 = new Assignment();
		ass1.setAssNo(1);
		ass1.setAssName("Simple Bpel introduction v2.0");
		ass1.setDeadline(new Date());
		ass1.setSpecification("http://ChangeALinkHere.pdf");
		mongo.updateAssByTeacher("TeacherHuang", ass1_id, ass1);
		*/
		
		// 8. Mark a student's submission
		/*
		System.out.println(mongo.markSubmissionByTeacher(ass1_id, "Peter", 99.5));
		System.out.println(mongo.markSubmissionByTeacher(ass1_id, "Lisa", 23.2));
		System.out.println(mongo.markSubmissionByTeacher(ass2_id, "Duancy", 23.2));
		*/
		
		// 9. Test Replication
		Assignment ass2 = new Assignment();
		ass2.setAssNo(2);
		ass2.setAssName("Another Example");
		ass2.setDeadline(new Date());
		ass2.setSpecification("http://www.google.com");
		
		System.out.println(mongo.addAssToDatabaseByTeacher(userName, ass2));
	}
}
