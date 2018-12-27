package com.hzyi.google.photos.uploader;

import com.google.common.base.Preconditions;
import org.apache.commons.cli.CommandLine;

public class ListPhotoApp {
  
  private static final String OPTION_ALBUM_SHORT_NAME = "ab";
  private static final String OPTION_DEBUG_SHORT_NAME = "d";

  private final boolean debug;
  private final String albumName;

  public ListPhotoApp(CommandLine cl) {
    Preconditions.checkArgument(
      cl.hasOption(OPTION_ALBUM_SHORT_NAME),
      "album name is required");
    albumName = cl.getOptionValue(OPTION_ALBUM_SHORT_NAME);
    if (cl.hasOption(OPTION_DEBUG_SHORT_NAME)) {
      debug = true;
    } else {
      debug = false;
    }
  }

  public void run() {
    if (debug) {
      System.out.println("listing photos in album [" + albumName + "]");
    }
  }
}