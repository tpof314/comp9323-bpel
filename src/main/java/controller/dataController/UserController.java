/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.dataController;

import com.google.api.services.drive.Drive;
import controller.tools.GoogleConnector;
import controller.tools.GoogleDriveBuilder;
import controller.tools.MongoDBConnector;
import controller.tools.JavaZip;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import model.Project;
import model.User;

/**
 *
 * @author Administrator
 */
public class UserController {
    MongoDBConnector mongo;
    GoogleConnector google;
    JavaZip zipManager;

    public UserController() throws GeneralSecurityException, IOException, URISyntaxException {
        Drive drive = null;
        drive = GoogleDriveBuilder.createDriveService();

        this.google = new GoogleConnector(drive);
        this.mongo = new MongoDBConnector();
        this.zipManager = new JavaZip();
    }
    
    public User getUserByName(String name, String password){
        
        return mongo.getUserByName(name, password);
    }
    
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
}
