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

public class AssignmentController {
	private MongoDBConnector mongo;
	private GoogleConnector google;
	private JavaZip zipManager;
	
	public AssignmentController() throws GeneralSecurityException, IOException, URISyntaxException {
		this.mongo = new MongoDBConnector();
		
		Drive drive = null;
        drive = GoogleDriveBuilder.createDriveService();
        this.google = new GoogleConnector(drive);
        
        this.zipManager = new JavaZip();
	}
	
	public List<Assignment> getUserAssignments(User user){
		if(user.getUserType().equals("teacher"))
			return mongo.loadAllAssByTeacher(user.getUserName());
		else if(user.getUserType().equals("student"))
			return mongo.loadAllAssByStudent(user.getUserName());
		else
			return null;
	}
	
	public boolean createAssignment(User user, Assignment assignment){
		if(!user.getUserType().equals("teacher"))
			return false;
		else{
			return mongo.addAssToDatabaseByTeacher(user.getUserName(), assignment);
		}
	}
	
	public boolean removeAssignment(User user, Assignment assignment){
		if(!user.getUserType().equals("teacher"))
			return false;
		else{
			boolean flag = false;
			
			java.io.File specfd = new java.io.File(assignment.getSpecification());
			flag = specfd.delete();
			if(!flag)
				return false;

			for(int i = 0; i < assignment.getSubmitRecord().size(); ++i){
				flag = google.removeFile(assignment.getSubmitRecord().get(i).getProjID());
				if(!flag)
					return false;
			}
			flag = mongo.removeAssByTeacher(user.getUserName(), assignment.getAssID());
			if(!flag)
				return false;
			
			return true;
		}
	}
	
	public boolean updateAssignment(User user, Assignment newAssignment){
		if(!user.getUserType().equals("teacher"))
			return false;
		else{
			return mongo.updateAssByTeacher(user.getUserName(), newAssignment.getAssID(), newAssignment);
		}
	}
	
	public boolean submitAssignment(User user, Assignment assignment, Submission submission, Project project){
		if(!user.getUserType().equals("student"))
			return false;
		
		boolean flag = false;
		String zipPath = user.getUserDir() + "assignment" + assignment.getAssNo() + ".zip";
		
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
		
        submission.setProjID(projId);
        submission.setSubmitTime(new Date());
		
		mongo.addSubmissionByStudent(assignment.getAssID(), submission);
        
		return true;
	}
	
	public boolean markSubmission(User user, String assID, Submission submission){
		if(!user.getUserType().equals("teacher"))
			return false;
		else
			return mongo.markSubmissionByTeacher(assID, submission.getStuName(), submission.getMark());
	}
}
