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
import com.google.auth.oauth2.UserCredentials;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

class UserCredentialsFactory {

  private static final File DATA_STORE_DIR =
      new File(System.getProperty("user.home"), "./google-photos-uploader/store");
  private static final HttpTransport TRNASPORT;
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final LocalServerReceiver RECEIVER = new LocalServerReceiver();
  private static final List<String> SCOPES =
      ImmutableList.of("https://www.googleapis.com/auth/photoslibrary");
  private static final String OPTION_DEBUG_SHORT_NAME = "d";
  private static final String CREDENTIALS_PATH =
      "/Users/hanzhenyi/.google-photos-uploader/credentials/client-credentials.json";

  static {
    try {
      TRNASPORT = GoogleNetHttpTransport.newTrustedTransport();
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException("Unable to create http channel due to: " + e);
    }
  }

  public static UserCredentials getUserCredentials() {
    GoogleClientSecrets clientSecrets;
    try {
      clientSecrets =
          GoogleClientSecrets.load(
              JSON_FACTORY, new InputStreamReader(new FileInputStream(CREDENTIALS_PATH)));
    } catch (IOException e) {
      throw new RuntimeException("Unable to load client credentials due to " + e);
    }

    String clientId = clientSecrets.getDetails().getClientId();
    String clientSecret = clientSecrets.getDetails().getClientSecret();

    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(
                TRNASPORT, JacksonFactory.getDefaultInstance(), clientSecrets, SCOPES)
            .setAccessType("offline")
            .setApprovalPrompt("force")
            .setClientId(clientId)
            .build();

    Credential credential;
    try {
      credential = new AuthorizationCodeInstalledApp(flow, RECEIVER).authorize("user");
    } catch (IOException e) {
      throw new RuntimeException("unable to authorize user due to " + e);
    }

    return UserCredentials.newBuilder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .setRefreshToken(credential.getRefreshToken())
        .build();
  }
}
