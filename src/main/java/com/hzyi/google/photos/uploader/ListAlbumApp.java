package com.hzyi.google.photos.uploader;

import org.apache.commons.cli.CommandLine;

public class ListAlbumApp extends BaseApp {
  
  private static final String OPTION_DEBUG_SHORT_NAME = "d";

  public ListAlbumApp(CommandLine cl) {
    super(cl);
  }



  public void run() {
    if (debug) {
      System.out.println("listing albums");
    }
  }
}