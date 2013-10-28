/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.dataController;

import com.google.api.services.drive.Drive;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import controller.tools.GoogleConnector;
import controller.tools.GoogleDriveBuilder;
import controller.tools.JavaZip;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Iterator;
import javax.ws.rs.core.MediaType;

import model.Directory;
import model.File;
import model.Project;
import model.User;

/**
 * Project Controller.
 * A controller for operating project models in the system. These operations 
 * includes uploading a file to a project, saving a project, 
 * removing a project etc.
 * Most of these operation are connected to Google Drive.
 * @author Peizhi Shao
 */
public class ProjectController {
    //The google drive connector;

    GoogleConnector google;
    JavaZip zipManager;

    public ProjectController() throws GeneralSecurityException, IOException, URISyntaxException {
        Drive drive = null;
        drive = GoogleDriveBuilder.createDriveService();

        this.google = new GoogleConnector(drive);

        zipManager = new JavaZip();
    }

    /**
     * Create a file in the project root directory.
     *
     * @param proj the project to be downloaded.
     * @param fileName name of the new file.
     * @return the created file.
     */
    public File createFile(Project proj, String fileName) {
        String filePath = proj.getPath() + java.io.File.separator + fileName;
        java.io.File fd = new java.io.File(filePath);

        try {
            fd.createNewFile();
        } catch (IOException ex) {
            return null;
        }

        File newFile = new File(fileName, filePath);

        try {
            FileWriter fw = new FileWriter(newFile.getPath());
            fw.write("<!-- This is an empty file, please add some comments here. -->");
            fw.close();
        } catch (IOException ex) {
            fd.delete();
            return null;
        }

        proj.getFiles().add(newFile);

        return newFile;
    }

    /**
     * Upload a file or files to the project home directory.
     *
     * @param dir the directory to upload files.
     * @param filePaths an array of files in user's local computer, which are
     * the the files that the user attempts to upload.
     * @return true if all the files uploaded successfully, false otherwise.
     */
    public File uploadFile(Project proj, String fileName, String content) {
    	String filePath = proj.getPath() + java.io.File.separator + fileName;
        java.io.File fd = new java.io.File(filePath);

        try {
            fd.createNewFile();
        } catch (IOException ex) {
            return null;
        }

        File newFile = new File(fileName, filePath);

        try {
            FileWriter fw = new FileWriter(newFile.getPath());
            fw.write(content);
            fw.close();
        } catch (IOException ex) {
            fd.delete();
            return null;
        }

        proj.getFiles().add(newFile);

        return newFile;
    }


    /**
     * Download a project to user's machine.
     *
     * @param proj the project to be downloaded.
     * @return true if the project is downloaded successfully, false otherwise.
     */
    public boolean downloadFile(Project proj) {
        return false;
    }

    /**
     * Load a project from Heroku web server. This project is assumed to be
     * firstly downloaded from Google Drive.
     *
     * @param projName the name of the project to be loaded.
     * @return a Project instance that contains a the file & directory
     * structure.
     */
    public Project loadProject(User user, String projName, String projId) {
        Project proj = new Project(projId, projName);

        boolean flag = google.downloadFile(projId, user.getUserDir() + projName + ".zip");
        if (!flag) {
            return null;
        }

        flag = zipManager.unZipFile(user.getUserDir() + projName + ".zip", user.getUserDir());
        if (!flag) {
            return null;
        }

        java.io.File downloadedZip = new java.io.File(user.getUserDir() + projName + ".zip");
        downloadedZip.delete();

        proj.setPath(user.getUserDir() + java.io.File.separator + projName);

        Directory dirBpel = new Directory("bpel", proj.getPath());
        Directory dirWsdl = new Directory("wsdl", proj.getPath());
        Directory dirInput = new Directory("input", proj.getPath());

        java.io.File projDir = new java.io.File(proj.getPath());
        String files[] = projDir.list();

        //We are trying to show the files as structured directories to customers.
        for (int i = 0; i < files.length; ++i) {
            File file = new File(files[i], proj.getPath() + java.io.File.separator + files[i]);

            if (files[i].endsWith(".bpel")) {
                dirBpel.getFiles().add(file);
            } else if (files[i].endsWith(".wsdl")) {
                dirWsdl.getFiles().add(file);
            } else if (files[i].endsWith(".soap")) {
                dirInput.getFiles().add(file);
            } else {
                proj.getFiles().add(file);
            }
        }

        proj.getDirs().add(dirBpel);
        proj.getDirs().add(dirWsdl);
        proj.getDirs().add(dirInput);

        return proj;
    }

    /**
     * Save all the files that are currently editing. Then save the whole
     * project to Google Drive.
     *
     * @param proj current project.
     * @param files the files that are currently editing.
     * @param newContents the new contents of the files.
     * @param Filenum the number of files that need to be saved.
     * @return true if the save operation succeeds, false otherwise.
     */
    public boolean saveToServer(Project proj) {
        boolean flag = false;

        flag = zipManager.zipDirectory(proj.getPath(), proj.getPath() + java.io.File.separator + proj.getProjName() + ".zip");
        if (!flag) {
            return false;
        }

        flag = google.reuploadFile(proj.getProjId(), proj.getPath() + java.io.File.separator + proj.getProjName() + ".zip");
        if (!flag) {
            return false;
        }
        
        java.io.File fd = new java.io.File(proj.getPath() + java.io.File.separator + proj.getProjName() + ".zip");
        fd.delete();

        proj.setState(1);

        return true;
    }

	/**
	 * Remove a project from BPEL Runtime Server. After a user leave the IDE, 
	 * the copy he left on the BPEL Runtime Server must be removed by invoking 
	 * this method.
	 * @param project the project to be removed.
	 * @return return if the project in the BPEL Runtime Server is
	 *         successfully removed, false otherwise.
	 */
    public boolean removeProject(Project project) {
        //Delete the bpel runtime.
        if (!project.getUri().equals("")) {
            ClientConfig config = new DefaultClientConfig();
            Client client = Client.create(config);
            WebResource service = client.resource(project.getUri());
            ClientResponse response;

            response = service.accept(MediaType.APPLICATION_XML).delete(ClientResponse.class);
            if (response.getStatus() == 500) {
                return false;
            }
        }

        //Delete all the files.
        FileController fc = new FileController();
        Iterator<File> files;
        java.io.File fd;

        files = project.getFiles().iterator();
        while (files.hasNext()) 
            fc.remove((File) files.next());
        
        for (int i = 0; i < project.getDirs().size(); ++i) {
            files = project.getDirs().get(i).getFiles().iterator();

            while (files.hasNext()) 
                fc.remove((File) files.next());
            
            fd = new java.io.File(project.getDirs().get(i).getPath());
            fd.delete();
        }

        fd = new java.io.File(project.getPath());
        fd.delete();
        
        return true;
    }
}
