package com.hzyi.google.photos.uploader;

import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.proto.Album;
import com.hzyi.google.photos.uploader.util.GooglePhotosClientFactory;
import java.io.IOException;

public class ListAlbumApp implements App<ListAlbumAppConfig> {

  @Override
  public void run(ListAlbumAppConfig config) {
    if (config.debug()) {
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
