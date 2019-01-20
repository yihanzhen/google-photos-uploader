package com.hzyi.google.photos.uploader.util;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.UserCredentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import java.io.IOException;

public class GooglePhotosClientFactory {

  public static PhotosLibraryClient createClient() throws IOException {
    return createClient(UserCredentialsFactory.getUserCredentials());
  }

  public static PhotosLibraryClient createClient(UserCredentials credentials) throws IOException {
    PhotosLibrarySettings settings =
        PhotosLibrarySettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
            .build();
    return PhotosLibraryClient.initialize(settings);
  }
}
