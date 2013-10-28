package controller.tools;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.activation.MimeType;

/**
 * A tool class for connecting to Google Drive.
 * In this IDE, users' code are saved in Google Drive. This class provides 
 * methods for downloading, uploading and updating files from Google Drive.
 * @author Haojie Huang
 */
public class GoogleConnector {
    private Drive service;

    public GoogleConnector(Drive service) {
        this.service = service;
    }

    /**
     * Download a file from Google Drive.
     *
     * @param service the Google drive service.
     * @param fileID the id of the file.
     * @param targetPath the location that the downloaded file will be saved.
     * @return true if the file is successfully downloaded; false otherwise.
     */
    public boolean downloadFile(String fileID, String targetPath) {
        try {
            File textFile = service.files().get(fileID).execute();
            InputStream inStream = this.getInputStream(textFile);

            java.io.File targerFile = new java.io.File(targetPath);
            OutputStream outStream = new FileOutputStream(targerFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            inStream.close();
            outStream.close();

            return true;
        } catch (IOException exp) {
            exp.printStackTrace();
            return false;
        }
    }

    /**
     * Upload a file to the Google Drive Server.
     *
     * @param service the Google Drive Service.
     * @param FileTitle title of the file shown in the google server.
     * @param UserDirectoryID the directory id to the user who uploads the file.
     * @param filePath the path of the file in local machine that's going to be
     * uploaded.
     * @return a file ID indicating the uploaded file. If upload fails, the
     * function returns null.
     */
    public String uploadFile(String FileTitle, String UserDirectoryID, String filePath) {
        File file = new File();
        file.setTitle(FileTitle);
        file.setDescription("Test Document File Description");
        file.setMimeType("text/plain");

        java.io.File fileContent = new java.io.File(filePath);
        FileContent mediaContent = new FileContent("text/plain", fileContent);

        ParentReference pntRef = new ParentReference().setId(UserDirectoryID);
        file.setParents(Arrays.asList(pntRef));
        try {
            File uploadFile = service.files().insert(file, mediaContent).execute();
            return uploadFile.getId();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
        return null;
    }

    /**
     * Refresh a file one Google Drive Server by uploading a new file.
     *
     * @param fileID the file id to be uploaded.
     * @param filePath the path.
     * @return true if uploaded successfully, false otherwise.
     */
    public boolean reuploadFile(String fileID, String filePath) {
        try {
            File file = service.files().get(fileID).execute();
            java.io.File fileContent = new java.io.File(filePath);
            FileContent mediaContent = new FileContent("text/plain", fileContent);

            File updatedFile = service.files().update(fileID, file, mediaContent).execute();
            return true;
        } catch (Exception exp) {
            exp.printStackTrace();
            return false;
        }
    }

    /**
     * Create a folder for a user in the Google Drive Server.
     *
     * @param service the Google Drive service.
     * @param username the name of the user.
     * @return the folder id.
     */
    public String createFolder(String username) {
        File body = new File();
        body.setTitle(username);
        body.setDescription(username + "'s working directory");
        body.setMimeType("application/vnd.google-apps.folder");

        try {
            File file = service.files().insert(body).execute();
            return file.getId();
        } catch (IOException exp) {
            exp.printStackTrace();
            return null;
        }
    }

    /**
     * Check if a user directory Id exists.
     *
     * @param service the Google Drive service.
     * @param userDirectoryID the user directory ID.
     * @return true if it exists, false otherwise.
     */
    public boolean folderIDExist(String userDirectoryID) {
        try {
            service.files().get(userDirectoryID).execute();
            return true;
        } catch (IOException exp) {
            return false;
        }
    }

    /**
     * Remove a file from Google Drive. The file will be permanently removed,
     * and cannot be recovered.
     *
     * @param service the Google Drive Service.
     * @param fileID the fileID.
     * @return true if the file has been successfully removed, false otherwise.
     */
    public boolean removeFile(String fileID) {
        try {
            service.files().delete(fileID).execute();
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
            return false;
        }

    }

    /* ========================= Helper Methods ========================= */
    private InputStream getInputStream(File file) {
        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
            try {
                HttpResponse resp =
                        service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
                        .execute();
                return resp.getContent();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public List<File> retrieveAllFiles() throws IOException {
        List<File> result = new ArrayList<File>();
        Files.List request = service.files().list();

        do {
            try {
                FileList files = request.execute();
                result.addAll(files.getItems());
                request.setPageToken(files.getNextPageToken());
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null
                && request.getPageToken().length() > 0);

        return result;
    }

    private void printFilesInFolder(Drive service, String folderId)
            throws IOException {
        Children.List request = service.children().list(folderId);
        do {
            try {
                ChildList children = request.execute();
                for (ChildReference child : children.getItems()) {
                    System.out.println("File Id: " + child.getId());
                }
                request.setPageToken(children.getNextPageToken());
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null
                && request.getPageToken().length() > 0);
    }
}
