package com.hzyi.google.photos.uploader;

import com.google.common.base.Preconditions;
import org.apache.commons.cli.CommandLine;

public class ListPhotoApp extends BaseApp {
  
  private static final String OPTION_ALBUM_SHORT_NAME = "ab";

  private final String albumName;

  public ListPhotoApp(CommandLine cl) {
    super(cl);
    Preconditions.checkArgument(
      cl.hasOption(OPTION_ALBUM_SHORT_NAME),
      "album name is required");
    albumName = cl.getOptionValue(OPTION_ALBUM_SHORT_NAME);
  }

  public void run() {
    if (debug) {
      System.out.println("listing photos in album [" + albumName + "]");
    }
  }
}