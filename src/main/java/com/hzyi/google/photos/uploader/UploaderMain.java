package com.hzyi.google.photos.uploader;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;

public class UploaderMain {

  private static final String GOAL_UPLOAD = "upload";
  private static final String GOAL_LIST = "list";

  private static final String OBJECT_LIST_ALBUM = "album";
  private static final String OBJECT_LIST_PHOTO = "photo";
  
  /**
   * Default album name will be derived according to the following strategy:
   * <ul>
   * <li> if a directory is specified, use the name of the directory plus whatever prefix given by -l and -t
   * <li> if a directory is not specified, use the system time to create a timestamp as the album name, plus whatever prefix given by -l and -t
   * </ul>
   */
  static final Option OPTION_UPLOAD_ALL =
      new Option("a", "all", false, "upload all photos in currect directory with default album names");
  static final Option OPTION_UPLOAD_ALBUM =
      new Option("ab", "album", false, "upload all photos to an album. If the album does not exist, create one");
  static final Option OPTION_UPLOAD_APPEND =
      new Option("ap", "append", false, "allows uploading photos to an existing album");
  static final Option OPTION_UPLOAD_FILE =
      new Option("f", "file", false, "upload a certain photo. This can be an absolute path or a relative path if -d exists");
  static final Option OPTION_UPLOAD_DIRECTORY =
      new Option("dir", "directory", false, "upload all photos in a directory");
  static final Option OPTION_UPLOAD_DEBUG =
      new Option("d", "debug", false, "print operations instead of doing the real uploads");
  static final Option OPTION_UPLOAD_LOCATION =
      new Option("l", "location", false, "prefix the location before album names");
  static final Option OPTION_UPLOAD_TIME =
      new Option("t", "time", false, "prefix the time to before the album names");

  static final Option OPTION_LIST_PHOTO_ALBUM =
      new Option("ab", "album", true, "listing all photos in this album");

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.err.println("invalid format!");
      System.exit(1);
    }
    String goal = args[0];
    if (GOAL_UPLOAD.equals(goal)) {
      uploadMain(args);
    } else if (GOAL_UPLOAD.equals(goal)) {
      listMain(args);
    } else {
      System.err.println("invalid goal [" + goal + "]!");
      System.exit(1);
    }
  }

  private static void uploadMain(String[] args) throws Exception {
    Options options = new Options();
    options.addOption(OPTION_UPLOAD_ALL);
    options.addOption(OPTION_UPLOAD_APPEND);
    options.addOption(OPTION_UPLOAD_ALBUM);
    options.addOption(OPTION_UPLOAD_FILE);
    options.addOption(OPTION_UPLOAD_DIRECTORY);
    options.addOption(OPTION_UPLOAD_DEBUG);
    options.addOption(OPTION_UPLOAD_LOCATION);
    options.addOption(OPTION_UPLOAD_TIME);

    CommandLine cl = (new DefaultParser()).parse(options, args);
    new UploadApp(cl).run();
  }

  private static void listMain(String[] args) throws Exception {
    Options options = new Options();
    String obj = args[1];
    if (OBJECT_LIST_ALBUM.equals(obj)) {
      CommandLine cl = (new DefaultParser()).parse(options, args);
      new ListAlbumApp(cl).run();
    } else if (OBJECT_LIST_PHOTO.equals(obj)) {
      options.addOption(OPTION_LIST_PHOTO_ALBUM);
      CommandLine cl = (new DefaultParser()).parse(options, args);
      new ListPhotoApp(cl).run();
    } else {
      System.err.println("invalid object [" + args[1] + "] for goal [" + args[0] + "]!");
      System.exit(1);
    }
  }
}