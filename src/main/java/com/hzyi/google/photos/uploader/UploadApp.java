package com.hzyi.google.photos.uploader;

import com.google.api.gax.rpc.ApiException;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.proto.Album;
import com.google.photos.library.v1.proto.BatchCreateMediaItemsResponse;
import com.google.photos.library.v1.proto.NewMediaItem;
import com.google.photos.library.v1.upload.UploadMediaItemRequest;
import com.google.photos.library.v1.upload.UploadMediaItemResponse;
import com.google.photos.library.v1.util.NewMediaItemFactory;
import com.hzyi.google.photos.uploader.util.GooglePhotosClientFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UploadApp implements App<UploadAppConfig> {

  public void run(UploadAppConfig config) {
    Path workingDir = getWorkingDirectory(config);
    List<Path> mediaItems = getMediaItems(config, workingDir);
    String albumName = getAlbumName(config, workingDir);
    if (config.debug()) {
      runDebug(workingDir, mediaItems, albumName);
    } else {
      runActually(config, workingDir, mediaItems, albumName);
    }
  }

  private void runDebug(Path workingDir, List<Path> mediaItems, String albumName) {
    System.out.println("in working directory: " + workingDir);
    System.out.printf("getting %d media items with the following names:\n", mediaItems.size());
    mediaItems.forEach(m -> System.out.println(m.getFileName().toString()));
    System.out.println("uploading them to album: " + albumName);
  }

  private void runActually(
      UploadAppConfig config, Path workingDir, List<Path> mediaItems, String albumName) {
    try (PhotosLibraryClient client = GooglePhotosClientFactory.createClient()) {
      List<NewMediaItem> newMediaItems =
          mediaItems
              .stream()
              .map(p -> uploadOneMediaItem(client, p))
              .filter(Objects::nonNull)
              .map(NewMediaItemFactory::createNewMediaItem)
              .collect(Collectors.toList());
      Album album = getAlbumByName(client, albumName, config.append());
      BatchCreateMediaItemsResponse response =
          client.batchCreateMediaItems(album.getId(), newMediaItems);
    } catch (IOException e) {
      throw new RuntimeException("Unable to create photos client due to: " + e.getCause());
    }
  }

  private Album getAlbumByName(PhotosLibraryClient client, String name, boolean append) {
    List<Album> albums =
        Lists.newArrayList(client.listAlbums().iterateAll())
            .stream()
            .filter(a -> a.getTitle().equals(name))
            .collect(Collectors.toList());
    Preconditions.checkArgument(albums.size() <= 1, "multiple albums found.");
    Preconditions.checkArgument(
        append || albums.isEmpty(), "album %s already exists, specify -ap to add to this album.");
    return albums.get(0);
  }

  private Path getWorkingDirectory(UploadAppConfig config) {
    String dir = config.directory();
    if (dir == null) {
      dir = System.getProperty("user.dir");
    }
    Path path = Paths.get(dir);
    Preconditions.checkArgument(Files.exists(path), "Path %s does not exist.", dir);
    Preconditions.checkArgument(Files.isDirectory(path), "Path %s is not a directory.", dir);
    return path;
  }

  private List<Path> getMediaItems(UploadAppConfig config, Path dir) {
    String filename = config.file();
    boolean all = config.all();
    Preconditions.checkArgument(
        filename != null || all,
        "No file specified to upload. Add -f for a certain file or -a for all files in the directory.");
    Preconditions.checkArgument(
        filename == null || !all,
        "Cannot specify both -a and -f. Add -f for a certain file or -a for all files in the directory.");
    if (all) {
      try {
        return Files.list(dir).filter(Files::isRegularFile).collect(Collectors.toList());
      } catch (IOException e) {
        throw new RuntimeException("Unable to read from directory due to: " + e);
      }
    }
    Path file = dir.resolve(filename);
    Preconditions.checkArgument(Files.isRegularFile(file), "File %s does not exist.", filename);
    return ImmutableList.<Path>of(file);
  }

  private String getAlbumName(UploadAppConfig config, Path dir) {
    String albumName = config.album();
    if (albumName == null) {
      albumName = dir.getFileName().toString();
    }
    String location = config.location();
    if (location != null) {
      albumName = location + " - " + albumName;
    }
    String time = config.time();
    if (time != null) {
      albumName = time + " - " + albumName;
    }
    return albumName;
  }

  private String uploadOneMediaItem(PhotosLibraryClient client, Path file) {
    try {
      UploadMediaItemRequest request =
          UploadMediaItemRequest.newBuilder()
              .setFileName(file.getFileName().toString())
              .setDataFile(new RandomAccessFile(file.toFile(), "r"))
              .build();

      UploadMediaItemResponse response = client.uploadMediaItem(request);
      if (response.getError().isPresent()) {
        UploadMediaItemResponse.Error error = response.getError().get();
        System.out.println("Unable to upload file to google photos: " + file);
        System.out.println("The reason is: " + error.getCause());
        System.out.println("Skipping this file.");
        return null;
      } else {
        return response.getUploadToken().get();
      }
    } catch (ApiException | FileNotFoundException e) {
      System.out.println("Unable to upload file to google photos: " + file);
      System.out.println("The reason is: " + e);
      System.out.println("Skipping this file.");
    }
    return null;
  }

  private void createOneMediaItem() {}
}
