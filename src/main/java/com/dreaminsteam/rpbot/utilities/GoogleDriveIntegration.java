package com.dreaminsteam.rpbot.utilities;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collections;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.common.base.Preconditions;

public class GoogleDriveIntegration {

	  /**
	   * Be sure to specify the name of your application. If the application name is {@code null} or
	   * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
	   */
	  private static final String APPLICATION_NAME = "discord-rp-bot";

	  private static final String DIR_FOR_DOWNLOADS = "./src/main/resources";


	  /** Directory to store user credentials. */
	  private static final java.io.File DATA_STORE_DIR =
	      new java.io.File(System.getProperty("user.home"), ".store/drive_sample");

	  /**
	   * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
	   * globally shared instance across your application.
	   */
	  private static FileDataStoreFactory dataStoreFactory;

	  /** Global instance of the HTTP transport. */
	  private static HttpTransport httpTransport;

	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	  /** Global Drive API client. */
	  private static Drive drive;

	  /** Authorizes the installed application to access user's protected data. */
	  private static Credential authorize() throws Exception {
	    // load client secrets
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
	        new InputStreamReader(GoogleDriveIntegration.class.getResourceAsStream("/client_secrets.json")));
	    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
	        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
	      System.out.println(
	          "Enter Client ID and Secret from https://code.google.com/apis/console/?api=drive "
	          + "into drive-cmdline-sample/src/main/resources/client_secrets.json");
	      System.exit(1);
	    }
	    // set up authorization code flow
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, JSON_FACTORY, clientSecrets,
	        Collections.singleton(DriveScopes.DRIVE_FILE)).setDataStoreFactory(dataStoreFactory)
	        .build();
	    // authorize
	    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	  }

	  public static void main(String[] args) {
	    try {
	      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	      dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
	      // authorization
	      Credential credential = authorize();
	      // set up the global Drive instance
	      drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
	          APPLICATION_NAME).build();

	      FileList file = drive.files().list().execute();
//	      downloadFile(true, file);

//	      View.header1("Success!");
	      return;
	    } catch (IOException e) {
	      System.err.println(e.getMessage());
	    } catch (Throwable t) {
	      t.printStackTrace();
	    }
	    System.exit(1);
	  }


	  /** Downloads a file using either resumable or direct media download. */
	  private static void downloadFile(boolean useDirectDownload, File uploadedFile) throws IOException {
	    // create parent directory (if necessary)
	    java.io.File parentDir = new java.io.File(DIR_FOR_DOWNLOADS);
	    if (!parentDir.exists() && !parentDir.mkdirs()) {
	      throw new IOException("Unable to create parent directory");
	    }
	    OutputStream out = new FileOutputStream(new java.io.File(parentDir, "Spell List.csv"));

	    MediaHttpDownloader downloader =
	        new MediaHttpDownloader(httpTransport, drive.getRequestFactory().getInitializer());
	    downloader.setDirectDownloadEnabled(useDirectDownload);
//	    downloader.setProgressListener(new FileDownloadProgressListener());
	    downloader.download(new GenericUrl(uploadedFile.getDownloadUrl()), out);
	}
}