package com.hzyi.google.photos.uploader;

import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.proto.Album;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;

public class ListAlbumApp extends BaseApp {

  private static final String OPTION_DEBUG_SHORT_NAME = "d";

  public ListAlbumApp(CommandLine cl) {
    super(cl);
  }

  public void run() {
    if (debug) {
      System.out.println("listing albums");
    } else {
      try (PhotosLibraryClient client = GooglePhotosClientFactory.createClient()) {
        for (Album album : client.listAlbums().iterateAll()) {
          System.out.println(album.getTitle());
        }
      } catch (IOException e) {
        throw new RuntimeException("unable to create client due to: " + e);
      }
    }
  }
}
