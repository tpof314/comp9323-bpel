/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.dataController;

import java.io.FileWriter;
import java.io.IOException;

import model.Directory;
import model.File;

public class DirectoryController {
    /**
     * Create a file in a specific directory.
     * @param dir the directory to save the created file.
     * @param fileName name of the new file.
     * @return the created file.
     */
    public File createFile(Directory dir, String fileName) {
        String filePath = dir.getPath() + java.io.File.separator + fileName;
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

        dir.getFiles().add(newFile);
        
        return newFile;
    }
    
    /**
     * Upload a file to a working directory. After the files has been uploaded, the
     * whole project will be uploaded to Google Drive.
     * @param dir the directory to upload files.
     * @param filePaths an array of files in user's local computer, which are the 
     *        the files that the user attempts to upload.
     * @return true if all the files uploaded successfully, false otherwise.
     */
    public File uploadFile(Directory dir, String fileName, String content) {
    	String filePath = dir.getPath() + java.io.File.separator + fileName;
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

        dir.getFiles().add(newFile);
        
        return newFile;
    }
}
