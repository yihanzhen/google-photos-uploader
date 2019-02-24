package com.hzyi.google.photos.uploader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;

public class UploaderMain {

  private static final String GOAL_UPLOAD = "upload";
  private static final String GOAL_LIST = "list";
  private static final String GOAL_SANITIZE = "sanitize";
  private static final String OBJECT_LIST_ALBUM = "album";

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.err.println("invalid format!");
      System.exit(1);
    }
    String goal = args[0];
    if (GOAL_UPLOAD.equals(goal)) {
      uploadMain(args);
    } else if (GOAL_LIST.equals(goal)) {
      listMain(args);
    } else if (GOAL_SANITIZE.equals(goal)) {
      sanitizeMain(args);
    } else {
      System.err.println("invalid goal [" + goal + "]!");
      System.exit(1);
    }
  }

  private static void uploadMain(String[] args) throws Exception {
    CommandLine cl = (new DefaultParser()).parse(UploadOptions.UPLOAD_OPTIONS, args);
    new UploadApp().run(UploadAppConfig.fromCommandLine(cl));
  }

  private static void listMain(String[] args) throws Exception {
    String obj = args[1];
    if (OBJECT_LIST_ALBUM.equals(obj)) {
      CommandLine cl = (new DefaultParser()).parse(ListAlbumOptions.LIST_ALBUM_OPTIONS, args);
      new ListAlbumApp().run(ListAlbumAppConfig.fromCommandLine(cl));
    } else {
      System.err.println("invalid object [" + args[1] + "] for goal [" + args[0] + "]!");
      System.exit(1);
    }
  }

  private static void sanitizeMain(String[] args) throws Exception {
    CommandLine cl = (new DefaultParser()).parse(SanitizeOptions.SANITIZE_OPTIONS, args);
    new SanitizeApp().run(SanitizeAppConfig.fromCommandLine(cl));
  }
}
