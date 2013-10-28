package controller.tools;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * To connect with Google Drive, a key must be specified. This class contains 
 * all IDs that the BPEL IDE need to connect to google.
 * @author Haojie Huang.
 */
public class GoogleDriveBuilder {

    private static final String SERVICE_ACCOUNT_EMAIL = "64684360303-9sfj7njggcidusas46h2494k74gl1vlq@developer.gserviceaccount.com";
    private static final String SERVICE_ACCOUNT_PKCS12_FILE_PATH = "894fcaaceffb126c0adf321fcfd9e409d2fd810c-privatekey.p12";

    /**
     * Initialize an authorized Google drive object, which is used for
     * communicating with the Google Drive Service.
     *
     * @return the Google Drive object.
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws URISyntaxException
     */
    public static Drive createDriveService() throws GeneralSecurityException,
            IOException, URISyntaxException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
                .setServiceAccountPrivateKeyFromP12File(new java.io.File(SERVICE_ACCOUNT_PKCS12_FILE_PATH))
                .setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE))
                .build();
        Drive service = new Drive.Builder(httpTransport, jsonFactory, null)
                .setHttpRequestInitializer(credential).build();
        return service;
    }
}
