package controller.dataController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.api.services.drive.Drive;

import controller.tools.GoogleConnector;
import controller.tools.GoogleDriveBuilder;
import controller.tools.JavaZip;
import controller.tools.MongoDBConnector;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Assignment;
import model.Project;
import model.Submission;
import model.User;

/**
 * Assignment Controller.
 * This module provides methods for operating assignments. Each method in this 
 * class contains a logic sequence for assignment operations. Each operation 
 * usually combines operations provided by both GoogleConnector and 
 * MongoDBConnector.
 * @author Peizhi Shao
 */
public class AssignmentController {
	private MongoDBConnector mongo;
	private GoogleConnector google;
	private JavaZip zipManager;
	
	/**
	 * Create a new AssignmentController Object. Using this object, the front end 
	 * can operate an assignment model using the method provided here.
	 * 
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public AssignmentController() throws GeneralSecurityException, IOException, URISyntaxException {
		this.mongo = new MongoDBConnector();
		
		Drive drive = null;
        drive = GoogleDriveBuilder.createDriveService();
        this.google = new GoogleConnector(drive);
        
        this.zipManager = new JavaZip();
	}
	
	/**
	 * Fetch a list of assignments related to a user.
	 * @param user the user.
	 * @return a list of assignments.
	 */
	public List<Assignment> getUserAssignments(User user){
		if(user.getUserType().equals("teacher"))
			return mongo.loadAllAssByTeacher(user.getUserName());
		else if(user.getUserType().equals("student"))
			return mongo.loadAllAssByStudent(user.getUserName());
		else
			return null;
	}
	
	/**
	 * Create a new assignment.
	 * @param user the user who wants to create a new assignment.
	 * @param assignment the assignment parameters, including the assignment name, 
	 *        the deadline and a link to specifications.
	 * @return an assignment id created by MongoDB. If the assignment cannot be 
	 *         created, return null.
	 */
	public String createAssignment(User user, Assignment assignment){
		if(!user.getUserType().equals("teacher"))
			return null;
		else{
			String assID = null;
			assID = mongo.addAssToDatabaseByTeacher(user.getUserName(), assignment);
			if(assID == null)
				return null;
			else
				return assID;
		}
	}
	
	/**
	 * Remove an assignment from both the database. Only a teacher is allowed to 
	 * do this operation.
	 * @param user the user who wants to remove an assignment.
	 * @param assignment the assignemnt to be removed.
	 * @return true if the assignment is removed successfully, false otherwise.
	 */
	public boolean removeAssignment(User user, Assignment assignment){
		if(!user.getUserType().equals("teacher"))
			return false;
		else{
			boolean flag = false;

			/*
			for(int i = 0; i < assignment.getSubmitRecord().size(); ++i){
				flag = google.removeFile(assignment.getSubmitRecord().get(i).getProjID());
				if(!flag)
					return false;
			}
			*/
			
			flag = mongo.removeAssByTeacher(user.getUserName(), assignment.getAssID());
			if(!flag)
				return false;
			
			return true;
		}
	}
	
	/**
	 * Update an assignment in the database. Only a teacher is allowed to perform
	 * this operation. The field to be updated are assignment names and deadlines.
	 * @param user the teacher who wants to update the assignment.
	 * @param newAssignment the new assignment parameters.
	 * @return true if the assignment is updated successfully, false otherwise.
	 */
	public boolean updateAssignment(User user, Assignment newAssignment){
		if(!user.getUserType().equals("teacher"))
			return false;
		else{
			return mongo.updateAssByTeacher(user.getUserName(), newAssignment.getAssID(), newAssignment);
		}
	}
	
	/**
	 * Submit an assignment. This operation can only be performed by a student.
	 * @param user the student who wants to submit an assignment.
	 * @param assignment the assignment to be submitted.
	 * @param submission a submission model containing the date of submission,  
	 *                   homework ID and the student's name.
	 * @param project the project that the student wants to submit.
	 * @return true if the assignment is submitted successfully, false otherwise.
	 */
	public boolean submitAssignment(User user, Assignment assignment, Submission submission, Project project){
		if(!user.getUserType().equals("student"))
			return false;
		
		
		/*
		boolean flag = false;
		
		String zipPath = user.getUserDir() + project.getProjName() + ".zip";
		
		google.downloadFile(project.getProjId(), zipPath);

        String projId = google.uploadFile(assignment.getAssName(), user.getUserId(), zipPath);
        java.io.File zipfd = new java.io.File(zipPath);
        zipfd.delete();
		
        if(projId == null){
        	System.out.println("upload to google fail.");
            return false;
        }
		else{
			System.out.println("The id of the submitted file is " + projId);
		}
		
		if(submission.getProjID() != null){
			flag = google.removeFile(projId);
			if(!flag)
				return false;
		}
		
		* */
		submission.setSubmitName(project.getProjName());
        submission.setProjID(project.getProjId());
        submission.setSubmitTime(new Date());
		
		mongo.addSubmissionByStudent(assignment.getAssID(), submission);
        
		return true;
	}
	
	/**
	 * Mark an assignment. Only teachers are allowed to mark an assignment.
	 * @param user the teacher who wants to mark a student's assignment.
	 * @param assID the assignment ID.
	 * @param submission the student's submission to be marked.
	 * @return true if the marking is done successfully, false otherwise. 
	 */
	public boolean markSubmission(User user, String assID, Submission submission){
		if(!user.getUserType().equals("teacher"))
			return false;
		else
			return mongo.markSubmissionByTeacher(assID, submission.getStuName(), submission.getMark());
	}
}
