package com.hzyi.google.photos.uploader;

import org.apache.commons.cli.CommandLine;

public class ListAlbumApp {
  
  private static final String OPTION_DEBUG_SHORT_NAME = "d";
  
  private final boolean debug;

  public ListAlbumApp(CommandLine cl) {
    if (cl.hasOption(OPTION_DEBUG_SHORT_NAME)) {
      debug = true;
    } else {
      debug = false;
    }
  }



  public void run() {
    if (debug) {
      System.out.println("list albums");
    }
  }
}