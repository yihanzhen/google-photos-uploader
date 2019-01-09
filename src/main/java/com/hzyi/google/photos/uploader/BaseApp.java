package com.hzyi.google.photos.uploader;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.UserCredentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;

public class BaseApp {

  private static final File DATA_STORE_DIR =
      new File(System.getProperty("user.home"), "./google-photos-uploader/store");
  private static final HttpTransport TRNASPORT = GoogleNetHttpTransport.newTrustedTransport();
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final LocalServerReceiver RECEIVER = new LocalServerReceiver();
  private static final List<String> SCOPES = new ArrayList();
  private static final String OPTION_DEBUG_SHORT_NAME = "d";
  private static final String CREDENTIALS_PATH = "./google-photos-uploader/credentials";

  protected final boolean debug;
  protected PhotosLibraryClient client;

  protected BaseApp(CommandLine cl) {
    if (cl.hasOption(OPTION_DEBUG_SHORT_NAME)) {
      debug = true;
    } else {
      debug = false;
    }
  }

  private static PhotosLibraryClient createClient() {
    PhotosLibrarySettings settings =
        PhotosLibrarySettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(authorize()))
            .build();
    return PhotosLibraryClient.initialize(settings);
  }

  private static Credentials authorize() {
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(
            JacksonFactory.getDefaultInstance(),
            new InputStreamReader(new FileInputStream(CREDENTIALS_PATH)));
    String clientId = clientSecrets.getDetails().getClientId();
    String clientSecret = clientSecrets.getDetails().getClientSecret();

    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(
                TRNASPORT, JacksonFactory.getDefaultInstance(), clientSecrets, SCOPES)
            .setAccessType("offline")
            .setApprovalPrompt("force")
            .setClientId(clientId)
            .build();

    Credential credential = new AuthorizationCodeInstalledApp(flow, RECEIVER).authorize("user");
    return UserCredentials.newBuilder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .setRefreshToken(credential.getRefreshToken())
        .build();
  }
}
