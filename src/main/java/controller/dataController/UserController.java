
package controller.dataController;

import com.google.api.services.drive.Drive;
import controller.tools.GoogleConnector;
import controller.tools.GoogleDriveBuilder;
import controller.tools.MongoDBConnector;
import controller.tools.JavaZip;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;
import model.Project;
import model.User;

/**
 * User controller.
 * A controller for user operations in the system. 
 * @author Peizhi Shao
 */
public class UserController {
    MongoDBConnector mongo;
    GoogleConnector google;
    JavaZip zipManager;

	/**
	 * Create a new user controller object.
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
    public UserController() throws GeneralSecurityException, IOException, URISyntaxException {
        Drive drive = null;
        drive = GoogleDriveBuilder.createDriveService();

        this.google = new GoogleConnector(drive);
        this.mongo = new MongoDBConnector();
        this.zipManager = new JavaZip();
    }
    
	/**
	 * Check if a username matches a password. If the username doesn't match the 
	 * password, or if the username itself doesn't exist at all, return null. 
	 * @param name the user name.
	 * @param password the password.
	 * @return a user structure if the user name matches the password, null otherwise.
	 */
    public User getUserByName(String name, String password){
        
        return mongo.getUserByName(name, password);
    }
    
	/**
	 * Create a project. This method will create a project belongs to a user specified.
	 * @param user the user who wants to create a project.
	 * @param projName the name of the new project.
	 * @return true if the project is created successfully, false otherwise.
	 */
    public boolean createProject(User user, String projName){
        boolean flag = false;
        
        for(int i = 0; i < user.getUserProjects().size(); ++i)
        	if(user.getUserProjects().get(i).getProjName().equals(projName)){
        		//System.out.println("same name project error.");
        		return false;
        	}
        
        
        String projDir = user.getUserDir() + java.io.File.separator + projName;
        java.io.File projfd = new java.io.File(projDir);
        flag = projfd.mkdir();
        if(!flag){
        	//System.out.println("create directory fail.");
            return false;
        }
        
        java.io.File readmefd = new java.io.File(projDir + java.io.File.separator + "README.txt");
        try {
			readmefd.createNewFile();
		} catch (IOException e) {
			//System.out.println("create readme fail.");
			return false;
		}
        
        /*Insert some template code.*/
        
        String zipDir = user.getUserDir() + java.io.File.separator + projName + ".zip";
        flag = zipManager.zipDirectory(projDir, zipDir);
        String projId = google.uploadFile(projName, user.getUserId(), zipDir);
        java.io.File zipfd = new java.io.File(zipDir);
        zipfd.delete();
        if(projId == null){
        	//System.out.println("upload to google fail.");
            return false;
        }
        
        Project proj = new Project(projId, projName);
        flag = mongo.insertProject(user, proj);
        if(!flag){
        	//System.out.println("insert to mongo fail.");
            return false;
        }

        readmefd.delete();
        projfd.delete();
        user.getUserProjects().add(proj);
        
        return true;
    }
    
	/**
	 * Remove a project from the system. By calling this method, both the project id 
	 * in the database and the actual project contain on Google Drive will be removed.
	 * There is another removeProject() method in the ProjectController Class, 
	 * the difference is that, this method is dealing with database and the one 
	 * in ProjectController deals with projects on the BPEL Runtime Server.
	 * @param user the user who wants to remove the project.
	 * @param proj the project to be removed.
	 * @return true if the project can be successfully removed, false otherwise.
	 */
    public boolean removeProject(User user, Project proj){
        boolean flag = false;
        
        flag = google.removeFile(proj.getProjId());
        if(!flag)
            return false;
        
        flag = mongo.removeProject(user, proj);
        if(!flag)
            return false;
        
        
        for(int i = 0; i < user.getUserProjects().size(); ++i){
            if(user.getUserProjects().get(i).getProjId().equals(proj.getProjId())) {
                user.getUserProjects().remove(i);
                break;
            }
        }
        
        return true;
    }
	
	/**
	 * Create a user. This method is used in registration. If a username is already 
	 * existed in the database, the method returns false.
	 * @param username the new username
	 * @param password the new password.
	 * @return true if the user account is successfully created; false otherwise.
	 */
	public boolean createUser(String username, String password) {
		if (mongo.userExist(username)) {
			return false;
		}
		String userID = google.createFolder(username);
		User user = new User(userID, username, "student", null);
		mongo.insertUser(user, password);
		
		
		return true;
	}
}
