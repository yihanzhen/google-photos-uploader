package com.hzyi.google.photos.uploader;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;

public class BaseApp {

  private static final String OPTION_DEBUG_SHORT_NAME = "d";

  protected final boolean debug;
  protected PhotosLibraryClient client;

  protected BaseApp(CommandLine cl) {
    if (cl.hasOption(OPTION_DEBUG_SHORT_NAME)) {
      debug = true;
    } else {
      debug = false;
    }
  }

  protected static PhotosLibraryClient createClient() throws IOException {
    PhotosLibrarySettings settings =
        PhotosLibrarySettings.newBuilder()
            .setCredentialsProvider(
                FixedCredentialsProvider.create(UserCredentialsFactory.getUserCredentials()))
            .build();
    return PhotosLibraryClient.initialize(settings);
  }
}
