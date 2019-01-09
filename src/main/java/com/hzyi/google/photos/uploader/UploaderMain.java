package com.hzyi.google.photos.uploader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class UploaderMain {

  private static final String GOAL_UPLOAD = "upload";
  private static final String GOAL_LIST = "list";

  private static final String OBJECT_LIST_ALBUM = "album";
  private static final String OBJECT_LIST_PHOTO = "photo";

  /**
   * Default album name will be derived according to the following strategy:
   *
   * <ul>
   *   <li>if a directory is specified, use the name of the directory plus whatever prefix given by
   *       -l and -t
   *   <li>if a directory is not specified, use the system time to create a timestamp as the album
   *       name, plus whatever prefix given by -l and -t
   * </ul>
   */
  static final Option OPTION_DEBUG =
      Option.builder("d")
          .longOpt("debug")
          .hasArg(false)
          .required(false)
          .desc("print operations instead of doing the real uploads")
          .build();

  static final Option OPTION_UPLOAD_ALL =
      Option.builder("a")
          .longOpt("all")
          .hasArg(false)
          .required(false)
          .desc("upload all photos in currect directory with default album names")
          .build();
  static final Option OPTION_UPLOAD_ALBUM =
      Option.builder("ab")
          .longOpt("album")
          .hasArg()
          .required(false)
          .desc("upload all photos to an album")
          .build();
  static final Option OPTION_UPLOAD_APPEND =
      Option.builder("ap")
          .longOpt("append")
          .hasArg(false)
          .required(false)
          .desc("allow uploading to an existing album")
          .build();
  static final Option OPTION_UPLOAD_FILE =
      Option.builder("f")
          .longOpt("file")
          .hasArg()
          .required(false)
          .desc(
              "upload a certain photo. This can be an absolute path or a relative path if -d exists")
          .build();
  static final Option OPTION_UPLOAD_DIRECTORY =
      Option.builder("dir")
          .longOpt("directory")
          .hasArg()
          .required(false)
          .desc("change working directory to that given by this flag")
          .build();

  static final Option OPTION_UPLOAD_LOCATION =
      Option.builder("l")
          .longOpt("location")
          .hasArg()
          .required(false)
          .desc("prefix the location before the album name")
          .build();

  static final Option OPTION_UPLOAD_TIME =
      Option.builder("t")
          .longOpt("time")
          .hasArg()
          .required(false)
          .desc("prefix the time before the album name")
          .build();

  static final Option OPTION_LIST_PHOTO_ALBUM =
      Option.builder("ab")
          .longOpt("album")
          .hasArg()
          .required(true)
          .desc("list all photos in this album")
          .build();

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
    } else {
      System.err.println("invalid goal [" + goal + "]!");
      System.exit(1);
    }
  }

  private static void uploadMain(String[] args) throws Exception {
    Options options = new Options();
    options.addOption(OPTION_DEBUG);
    options.addOption(OPTION_UPLOAD_ALL);
    options.addOption(OPTION_UPLOAD_APPEND);
    options.addOption(OPTION_UPLOAD_ALBUM);
    options.addOption(OPTION_UPLOAD_FILE);
    options.addOption(OPTION_UPLOAD_DIRECTORY);
    options.addOption(OPTION_UPLOAD_LOCATION);
    options.addOption(OPTION_UPLOAD_TIME);

    CommandLine cl = (new DefaultParser()).parse(options, args);
    new UploadApp(cl).run();
  }

  private static void listMain(String[] args) throws Exception {
    Options options = new Options();
    options.addOption(OPTION_DEBUG);
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
