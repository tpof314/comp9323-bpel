/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import controller.tools.GoogleConnector;
import controller.tools.GoogleDriveBuilder;
import controller.tools.JavaZip;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 *
 * @author z3409413
 */
public class ZipTest {
	public static void main(String args[]) throws GeneralSecurityException, IOException, URISyntaxException {
		Drive drive = GoogleDriveBuilder.createDriveService();
		GoogleConnector google = new GoogleConnector(drive);
		
		List<File> files = google.retrieveAllFiles();
		for (File f : files) {
			System.out.println("======================");
			System.out.println("Create Date: " + f.getCreatedDate().toString());
			System.out.println("File ID: " + f.getId());
			System.out.println("File Type: " + f.getFileExtension());
			System.out.println("File Size: " + f.getFileSize());
			System.out.println("File Title: " + f.getTitle());
		}
		
		//google.downloadFile("0B942fHMEpI9sRFdLOXgtTHp2cU0", "lisa.zip");
//		JavaZip jzip = new JavaZip();
//		jzip.unZipFile("peter.zip", "duancy");
	}
}
