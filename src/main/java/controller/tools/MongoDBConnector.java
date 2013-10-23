package controller.tools;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Assignment;
import model.Project;
import model.Submission;
import model.User;

public class MongoDBConnector {

    private Mongo mongo;
    private DB db;
    private DBCollection userTable;
	private DBCollection assignmentTable;
	
    private static final String DATABASE_URI = "ds039768.mongolab.com";
    private static final int DATABASE_PORT = 39768;
    private static final String DATABASE_NAME = "comp9323";
    private static final String ADMIN_NAME = "hhua175";
    private static final String ADMIN_PASSWORD = "bpel_project";
    private static final String USER_TABLE = "Users";
    private static final String ASSIGNMENT_TABLE = "Assignments";
	private static final String USERNAME = "username";
    private static final String USER_ID = "userID";
    private static final String USER_TYPE = "userType";
    private static final String PASSWORD = "password";
    public static final String STUDENT = "student";
    public static final String TEACHER = "teacher";
    private static final String PROJECTS = "projects";
    private static final String PROJECT_NAME = "projectname";
    private static final String PROJECT_ID = "projectID";

	private static final String ASSIGNMENT_NO = "ass_no";
	private static final String ASSIGNMENT_NAME = "ass_name";
	private static final String ASSIGNMENT_DEADLINE = "deadline";
	private static final String ASSIGNMENT_SPEC = "specification";
	private static final String ASSIGNMENT_SUBMISSIONS = "submissions";
	private static final String STUDENT_NAME = "student_name";
	private static final String SUBMIT_TIME = "submit_time";
	private static final String MARK = "mark";
	
    public MongoDBConnector() {
        try {
            this.mongo = new Mongo(DATABASE_URI, DATABASE_PORT);
            this.db = mongo.getDB(DATABASE_NAME);
            this.db.authenticate(ADMIN_NAME, ADMIN_PASSWORD.toCharArray());
            this.userTable = db.getCollection(USER_TABLE);
			this.assignmentTable = db.getCollection(ASSIGNMENT_TABLE);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MongoDBConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Check if a username exists in database.
     *
     * @param username the username.
     * @return true if user exists or connection error, otherwise false.
     */
    public boolean userExist(String username) {
        try {
            BasicDBObject query = new BasicDBObject(USERNAME, username);
            DBCursor cursor = userTable.find(query);
            if (cursor.hasNext()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Create a new user, and set his password in the database.
     *
     * @param user the user.
     * @param password the password.
     * @return true if the user has been successfully inserted into the
     * database; false otherwise.
     */
    public boolean insertUser(User user, String password) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.put(USERNAME, user.getUserName());
            document.put(USER_ID, user.getUserId());
            document.put(USER_TYPE, user.getUserType());
            document.put(PASSWORD, password);

            BasicDBList projects = new BasicDBList();
            document.put(PROJECTS, projects);
			
            userTable.insert(document);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get a user object from MongoDB. The username and the password will be
     * sent to MongoLab server to check if the password is correct. User
     * information will only be assigned if both username and password are
     * correct. Otherwise, this method will return null.
     *
     * @param username the username.
     * @param password the password.
     * @return a User instance without projects assigned if password is correct,
     * otherwise null.
     */
    public User getUserByNameWithoutProjects(String username, String password) {
        try {
            BasicDBObject query = new BasicDBObject(USERNAME, username);
            DBCursor cursor = userTable.find(query);
            if (cursor.hasNext()) {
                DBObject currentUser = cursor.next();
                String correctPassword = (String) currentUser.get(PASSWORD);
                if (password.equals(correctPassword)) {
                    String userID = (String) currentUser.get(USER_ID);
                    String userType = (String) currentUser.get(USER_TYPE);
                    User user = new User(username, userID, userType, null);
                    return user;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get a user object from MongoDB. The username and the password will be
     * sent to MongoLab server to check if the password is correct. User
     * information will only be assigned if both username and password are
     * correct. Otherwise, this method will return null.
     *
     * @param username the username.
     * @param password the password.
     * @return a User instance without projects assigned if password is correct,
     * otherwise null.
     */
    public User getUserByName(String username, String password) {
        BasicDBObject query = new BasicDBObject(USERNAME, username);
        DBCursor cursor = userTable.find(query);
        if (cursor.hasNext()) {
            DBObject currentUser = cursor.next();
            String correctPassword = (String) currentUser.get(PASSWORD);
            if (password.equals(correctPassword)) {
                String userID = (String) currentUser.get(USER_ID);
                String userType = (String) currentUser.get(USER_TYPE);

                BasicDBList list = (BasicDBList) currentUser.get(PROJECTS);
                ArrayList<Project> projects = extractProjects(list);
                User user = new User(userID, username, userType, projects);
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Insert a new project to a user's database.
     *
     * @param user the user.
     * @param project the new project.
     * @return true if the new project is inserted successfully; false
     * otherwise.
     */
    public boolean insertProject(User user, Project project) {
        BasicDBObject query = new BasicDBObject(USERNAME, user.getUserName());
        DBCursor cursor = userTable.find(query);
        if (cursor.hasNext()) {
            BasicDBObject dbProject = new BasicDBObject();
            dbProject.put(PROJECT_NAME, project.getProjName());
            dbProject.put(PROJECT_ID, project.getProjId());

            DBObject listItem = new BasicDBObject(PROJECTS, dbProject);
            BasicDBObject updateQuery = new BasicDBObject("$push", listItem);
            userTable.update(query, updateQuery);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Remove a project from a user's database.
     *
     * @param user the user.
     * @param project the project to be removed.
     * @return true if the project has been removed successfully; false
     * otherwise.
     */
    public boolean removeProject(User user, Project project) {
        BasicDBObject query = new BasicDBObject(USERNAME, user.getUserName());
        DBCursor cursor = userTable.find(query);
        if (cursor.hasNext()) {
            BasicDBObject dbProject = new BasicDBObject();
            dbProject.put(PROJECT_NAME, project.getProjName());

            BasicDBObject toDelete = new BasicDBObject("$pull", new BasicDBObject(PROJECTS, dbProject));
            userTable.update(query, toDelete);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Rename a project.
     *
     * @param user the user.
     * @param project the project to be renamed.
     * @param newProjectName the new name of the project.
     * @return true if the project has been renamed successfully; false
     * otherwise.
     */
    public boolean renameProject(User user, Project project, String newProjectName) {
        BasicDBObject query = new BasicDBObject(USERNAME, user.getUserName());
        DBCursor cursor = userTable.find(query);
        if (cursor.hasNext()) {
            query.append(PROJECTS, new BasicDBObject("$elemMatch", new BasicDBObject(PROJECT_NAME, project.getProjName())));
            BasicDBObject update = new BasicDBObject("$set", new BasicDBObject(PROJECTS + ".$." + PROJECT_NAME, newProjectName));
            userTable.update(query, update);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Change the ID of a project.
     *
     * @param user the user.
     * @param project the project to be renamed.
     * @param newProjectID the new id of the project.
     * @return true if the project id has been reassigned successfully; false
     * otherwise.
     */
    public boolean reassignProject(User user, Project project, String newProjectID) {
        BasicDBObject query = new BasicDBObject(USERNAME, user.getUserName());
        DBCursor cursor = userTable.find(query);
        if (cursor.hasNext()) {
            query.append(PROJECTS, new BasicDBObject("$elemMatch", new BasicDBObject(PROJECT_NAME, project.getProjName())));
            BasicDBObject update = new BasicDBObject("$set", new BasicDBObject(PROJECTS + ".$." + PROJECT_ID, newProjectID));
            userTable.update(query, update);
            return true;
        } else {
            return false;
        }
    }

    
    /* ============== Added Methods For Assignment Submission ============== */
    /**
     * Load a single assignment with all students' submission. If a student has submitted his 
     * work, it will be added to the submission list automatically.
     * @param username teacher's username.
     * @param assID the assignment ID to be loaded.
     * @return an assignment structure.
     */
    public Assignment loadAssByTeacher(String username, String assID) {
        DBObject assDBObj = getAssDBObject(assID);
		if (assDBObj == null) {
			return null;
		}
		
		int assNo = (Integer) assDBObj.get(ASSIGNMENT_NO);
		String assName = (String) assDBObj.get(ASSIGNMENT_NAME);
		Date deadline = (Date) assDBObj.get(ASSIGNMENT_DEADLINE);
		String spec = (String) assDBObj.get(ASSIGNMENT_SPEC);
		
		ArrayList<Submission> submitRecords = fetchSubmissions((BasicDBList) assDBObj.get(ASSIGNMENT_SUBMISSIONS));
		Assignment ass = new Assignment(assID, assNo, assName, deadline, spec, submitRecords);
		
		return ass;
    }
    
    /**
     * Load a single assignment with a student's submission. This method is called by a student
     * who wants to check his mark. A whole assignment structure will be fetched from the 
     * database, only a single submission record will be attached to the submission list
     * field. If the student has not submitted his assignment, the submission list will be an 
     * empty list. (Note: The submission list is NOT null!)
     * @param username student's username.
     * @param assID the assignment ID to be loaded.
     * @return an assignment structure, or null if the assignment does not exist.
     */
    public Assignment loadAssByStudent(String username, String assID) {
        DBObject assDBObj = getAssDBObject(assID);
		if (assDBObj == null) {
			return null;
		}
		
		int assNo = (Integer) assDBObj.get(ASSIGNMENT_NO);
		String assName = (String) assDBObj.get(ASSIGNMENT_NAME);
		Date deadline = (Date) assDBObj.get(ASSIGNMENT_DEADLINE);
		String spec = (String) assDBObj.get(ASSIGNMENT_SPEC);
		
		ArrayList<Submission> submitRecords = getSubmissionByStudentName(username, (BasicDBList) assDBObj.get(ASSIGNMENT_SUBMISSIONS));
		Assignment ass = new Assignment(assID, assNo, assName, deadline, spec, submitRecords);
		
		return ass;
    }
    
    /**
     * Load a whole list of assignment released by the teacher.
     * All the submission lists will be filled as well.
     * @param username teacher's username.
     */
    public ArrayList<Assignment> loadAllAssByTeacher(String username) {
		DBObject query = new BasicDBObject();
		DBCursor cursor = assignmentTable.find(query);
		
		ArrayList<Assignment> assList = new ArrayList<Assignment>();
		while (cursor.hasNext()) {
			DBObject assDBObj = cursor.next();
			String assID = ((ObjectId) assDBObj.get("_id")).toString();
			int assNo = (Integer) assDBObj.get(ASSIGNMENT_NO);
			String assName = (String) assDBObj.get(ASSIGNMENT_NAME);
			Date deadline = (Date) assDBObj.get(ASSIGNMENT_DEADLINE);
			String spec = (String) assDBObj.get(ASSIGNMENT_SPEC);
			
			ArrayList<Submission> submitRecords = fetchSubmissions((BasicDBList) assDBObj.get(ASSIGNMENT_SUBMISSIONS));
			Assignment ass = new Assignment(assID, assNo, assName, deadline, spec, submitRecords);
			assList.add(ass);
		}
        return assList;
    }
    
    /**
     * Load a whole list of assignment released by the teacher.
     * The only difference to loadAllAssByTeacher() is that, a student can 
     * only see the submission record belongs to him.
     * @param username student's username.
     */
    public ArrayList<Assignment> loadAllAssByStudent(String username) {
    	DBObject query = new BasicDBObject();
		DBCursor cursor = assignmentTable.find(query);
		
		ArrayList<Assignment> assList = new ArrayList<Assignment>();
		while (cursor.hasNext()) {
			DBObject assDBObj = cursor.next();
			String assID = ((ObjectId) assDBObj.get("_id")).toString();
			int assNo = (Integer) assDBObj.get(ASSIGNMENT_NO);
			String assName = (String) assDBObj.get(ASSIGNMENT_NAME);
			Date deadline = (Date) assDBObj.get(ASSIGNMENT_DEADLINE);
			String spec = (String) assDBObj.get(ASSIGNMENT_SPEC);
			
			ArrayList<Submission> submitRecords = getSubmissionByStudentName(username, (BasicDBList) assDBObj.get(ASSIGNMENT_SUBMISSIONS));
			Assignment ass = new Assignment(assID, assNo, assName, deadline, spec, submitRecords);
			assList.add(ass);
		}
        return assList;
    }
    
    /**
     * Add a new assignment to the database. 
     * @param username teacher's username.
     * @param assignment an assignment.
	 * @return true if add assignment successfully, false otherwise.
	 * (NOTE: The database doesn't allow replicated assignment numbers.)
     */
    public boolean addAssToDatabaseByTeacher(String username, Assignment assignment) {
        try {
			DBObject dbAssignment = new BasicDBObject(ASSIGNMENT_NO, assignment.getAssNo());
			DBCursor cursor = assignmentTable.find(dbAssignment);
			if (cursor.hasNext()) {
				return false;
			}
			BasicDBObject document = new BasicDBObject();
            document.put(ASSIGNMENT_NO, assignment.getAssNo());
            document.put(ASSIGNMENT_NAME, assignment.getAssName());
            document.put(ASSIGNMENT_DEADLINE, assignment.getDeadline());
            document.put(ASSIGNMENT_SPEC, assignment.getSpecification());

            BasicDBList submissions = new BasicDBList();
            document.put(ASSIGNMENT_SUBMISSIONS, submissions);
            assignmentTable.insert(document);
			
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Remove an assignment. When a teacher removes an assignment, all the submissions
     * to this assignment will also be removed from the database.
     * @param username teacher's username.
     * @param assID an ID indicating which assignment to be removed.
	 * @return true if remove successfully, false otherwise.
     */
    public boolean removeAssByTeacher(String username, String assID) {
		DBObject dbAssignment = new BasicDBObject("_id", new ObjectId(assID));
		DBCursor cursor = assignmentTable.find(dbAssignment);
		
		if (cursor.hasNext()) {
			assignmentTable.remove(dbAssignment);
			return true;
		}
		
		return false;
    }
    
    /**
     * Add a new submission record to the database.
     * This record is added by a student. If the submission is already existed
     * in the database, then it will be updated.
     * @param assID the assignment to be submitted.
     * @param submission a record of submission with mark = -1.
	 * @return true if add submission successfully, false otherwise.
     */
    public boolean addSubmissionByStudent(String assID, Submission submission) {
		DBObject assDBObject = getAssDBObject(assID);
		if (assDBObject == null) {
			return false;
		}

		// If a student's submission is already in the database, then it is updated.
		// Else a new record will be added to the database.
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(assID));
		query.append(ASSIGNMENT_SUBMISSIONS + "." + STUDENT_NAME, submission.getStuName());
        DBCursor cursor = assignmentTable.find(query);
		
		if (cursor.hasNext()) {
			BasicDBObject updateObj = new BasicDBObject();
			updateObj.append(ASSIGNMENT_SUBMISSIONS + ".$." + PROJECT_ID, submission.getProjID());
			updateObj.append(ASSIGNMENT_SUBMISSIONS + ".$." + SUBMIT_TIME, submission.getSubmitTime());
			BasicDBObject update = new BasicDBObject("$set", updateObj);
			assignmentTable.update(query, update);
		}
		else {
			BasicDBObject dbSubmission = new BasicDBObject();
			dbSubmission.put(STUDENT_NAME, submission.getStuName());
			dbSubmission.put(PROJECT_ID, submission.getProjID());
			dbSubmission.put(SUBMIT_TIME, submission.getSubmitTime());
			dbSubmission.put(MARK, -1.0);
			
			DBObject listItem = new BasicDBObject(ASSIGNMENT_SUBMISSIONS, dbSubmission);
			BasicDBObject updateQuery = new BasicDBObject("$push", listItem);
			assignmentTable.update(assDBObject, updateQuery);
		}

        return true;
    }
    
    /**
     * Update an assignment. Either the names, deadline or specifications can be
     * updated by a teacher.
     * @param username the teacher's name.
     * @param assID the assignment ID.
     * @param newAssignment an assignment structure with updated name, deadline or 
     *        specification. 
     */

    public boolean updateAssByTeacher(String username, String assID, Assignment newAssignment) {
    	BasicDBObject query = new BasicDBObject("_id", new ObjectId(assID));
		DBCursor cursor = assignmentTable.find(query);
		if (!cursor.hasNext()) {
			return false;
		}
		
		BasicDBObject updateDBObj = new BasicDBObject();
		updateDBObj.append(ASSIGNMENT_NO, newAssignment.getAssNo());
		updateDBObj.append(ASSIGNMENT_NAME, newAssignment.getAssName());
		updateDBObj.append(ASSIGNMENT_DEADLINE, newAssignment.getDeadline());
		updateDBObj.append(ASSIGNMENT_SPEC, newAssignment.getSpecification());
		
		BasicDBObject update = new BasicDBObject("$set", updateDBObj);
		assignmentTable.update(query, update);
		return true;
	}
    /**
     * Mark a submission. This method could only be called by a teacher.
     * The assignment id, student's name and a marked must be specified.
     * @param assID assignment id.
     * @param stuName student's name.
     * @param mark the mark that the teacher gives.
	 * @return true if the student's submission has been marked; false if the 
	 *          student's submission cannot be found in the database, or the assignment
	 *          doesn't exist.
     */
    public boolean markSubmissionByTeacher(String assID, String stuName, double mark) {
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(assID));
		query.append(ASSIGNMENT_SUBMISSIONS + "." + STUDENT_NAME, stuName);
        DBCursor cursor = assignmentTable.find(query);
		if (!cursor.hasNext()) {
			return false;
		}
		BasicDBObject updateObj = new BasicDBObject();
		updateObj.append(ASSIGNMENT_SUBMISSIONS + ".$." + MARK, mark);
		BasicDBObject update = new BasicDBObject("$set", updateObj);
		assignmentTable.update(query, update);
		return true;
    }
	
    /* ========================== Helper Methods ============================ */
    /**
     * Extract all project names and ids from a database list.
     *
     * @param list the database list.
     * @return a list of project, or return an empty list if list is empty.
     */
    private ArrayList<Project> extractProjects(BasicDBList list) {
        if (list == null) {
            return new ArrayList<Project>();
        }
        if (list.size() == 0) {
            return new ArrayList<Project>();
        }

        ArrayList<Project> projects = new ArrayList<Project>();
        for (int i = 0; i < list.size(); i++) {
            BasicDBObject proj = (BasicDBObject) list.get(i);
            String proj_name = (String) proj.get(PROJECT_NAME);
            String proj_id = (String) proj.get(PROJECT_ID);

            Project proj_i = new Project(proj_id, proj_name, null, null, null);
            projects.add(proj_i);
        }
        return projects;
    }
	
	/**
	 * Fetch a single Assignment Object from the database.
	 * @param assID the assignment ID.
	 * @return the assignment ID.
	 */
	private DBObject getAssDBObject(String assID) {
		DBObject query = new BasicDBObject("_id", new ObjectId(assID));
        DBCursor cursor = assignmentTable.find(query);
        if (cursor.hasNext()) {
            DBObject assignment = cursor.next();
			return assignment;
		}
		return null;
	}
	
	/**
	 * Fetch all submissions in an assignment.
	 * @param list the submission list in the assignment.
	 * @return a submission list in array list format.
	 */
	private ArrayList<Submission> fetchSubmissions(BasicDBList list) {
		if (list == null) {
            return new ArrayList<Submission>();
        }
        if (list.size() == 0) {
            return new ArrayList<Submission>();
        }
		
		ArrayList<Submission> records = new ArrayList<Submission>();
        for (int i = 0; i < list.size(); i++) {
            BasicDBObject submissionDBObj = (BasicDBObject) list.get(i);
            String stuName = (String) submissionDBObj.get(STUDENT_NAME);
            String projID = (String) submissionDBObj.get(PROJECT_ID);
			Date submitTime = (Date) submissionDBObj.get(SUBMIT_TIME);;
			double mark = (Double) submissionDBObj.get(MARK);
			
            Submission submission = new Submission(stuName, projID, submitTime, mark);
            records.add(submission);
        }
		return records;
	}
	
	/**
	 * Fetch a single submission record in an assignment by student name.
	 * @param stuName student's name.
	 * @param list the db list.
	 * @return a single submission in an array list.
	 */
	private ArrayList<Submission> getSubmissionByStudentName(String stuName, BasicDBList list) {
		if (list == null) {
            return new ArrayList<Submission>();
        }
        if (list.size() == 0) {
            return new ArrayList<Submission>();
        }
		
		ArrayList<Submission> records = new ArrayList<Submission>();
        for (int i = 0; i < list.size(); i++) {
            BasicDBObject submissionDBObj = (BasicDBObject) list.get(i);
            String student = (String) submissionDBObj.get(STUDENT_NAME);
			if (student.equals(stuName)) {
				String projID = (String) submissionDBObj.get(PROJECT_ID);
				Date submitTime = (Date) submissionDBObj.get(SUBMIT_TIME);;
				double mark = (Double) submissionDBObj.get(MARK);
				
				Submission submission = new Submission(stuName, projID, submitTime, mark);
				records.add(submission);
				return records;
			}
        }
		return records;
	}
}
