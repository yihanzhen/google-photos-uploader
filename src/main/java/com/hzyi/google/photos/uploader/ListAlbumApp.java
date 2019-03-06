package com.hzyi.google.photos.uploader;

import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.proto.Album;
import com.hzyi.google.photos.uploader.util.GooglePhotosClientFactory;
import com.hzyi.google.photos.uploader.util.LoggerFactory;
import java.io.IOException;
import java.util.logging.Logger;

public class ListAlbumApp implements App<ListAlbumAppConfig> {

  private Logger logger = LoggerFactory.getLogger(this);

  @Override
  public void run(ListAlbumAppConfig config) {
    if (config.debug()) {
      logger.info("[debugging mode] listing albums");
    } else {
      System.out.println("listing all the albums:");
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
