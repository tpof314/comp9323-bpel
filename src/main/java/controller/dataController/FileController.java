
package controller.dataController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import model.File;

/**
 * File Controller.
 * This controller is used to control file models in the system. In the system, 
 * operation with files only happens in the web server side.
 * @author Peizhi Shao
 */
public class FileController {
    /**
     * Get the content of a file.
     * @param file the file to be opened.
     * @return the content of the file
     */
    public String open(File file) {
        String fileContent = new String();
        
        try {
            FileReader fr = new FileReader(file.getPath());
            BufferedReader br = new BufferedReader(fr);
            
            String line = new String();
            while((line = br.readLine()) != null)
                fileContent += line + '\n';
            
            br.close();
            fr.close();
            
        } catch (IOException ex) {
            return null;
        }
        
        return fileContent;
    }
    
    /**
     * Replace a file's content with 'newContent'.
     * @param file the file to be saved.
     * @param newContent the new content typed by the user.
     * @return true if save successfully, false otherwise.
     */  
    public boolean save(File file, String newContent) {
        try {
            FileWriter fw = new FileWriter(file.getPath());
            fw.write(newContent);
            fw.close();
        } catch (IOException ex) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Remove a file in local project.
     * @param file the file need to be removed.
     * @return true if remove successfully, false otherwise.
     */
    public boolean remove(File file) {
        boolean removeSuccess = false;
        
        java.io.File fd = new java.io.File(file.getPath());
        removeSuccess = fd.delete();
        
        return removeSuccess;
    }
}
