package com.hzyi.google.photos.uploader;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

final class UploadOptions {
  private UploadOptions() {
    // static utility class
  }

  static final Option DEBUG =
      Option.builder("d")
          .longOpt("debug")
          .hasArg(false)
          .required(false)
          .desc("print operations instead of doing the real uploads")
          .build();

  static final Option ALL =
      Option.builder("a")
          .longOpt("all")
          .hasArg(false)
          .required(false)
          .desc("upload all photos in currect directory")
          .build();
  static final Option ALBUM =
      Option.builder("ab")
          .longOpt("album")
          .hasArg()
          .required(false)
          .desc("upload all photos to an album")
          .build();
  static final Option APPEND =
      Option.builder("ap")
          .longOpt("append")
          .hasArg(false)
          .required(false)
          .desc("allow uploading to an existing album")
          .build();
  static final Option FILE =
      Option.builder("f")
          .longOpt("file")
          .hasArg()
          .required(false)
          .desc(
              "upload a certain photo. This can be an absolute path or a relative path if -d exists")
          .build();
  static final Option DIRECTORY =
      Option.builder("dir")
          .longOpt("directory")
          .hasArg()
          .required(false)
          .desc("change working directory to that given by this flag")
          .build();

  static final Option LOCATION =
      Option.builder("l")
          .longOpt("location")
          .hasArg()
          .required(false)
          .desc("prefix the location before the album name")
          .build();

  static final Option TIME =
      Option.builder("t")
          .longOpt("time")
          .hasArg()
          .required(false)
          .desc("prefix the time before the album name")
          .build();

  static final Options UPLOAD_OPTIONS =
      new Options()
          .addOption(DEBUG)
          .addOption(ALL)
          .addOption(APPEND)
          .addOption(ALBUM)
          .addOption(FILE)
          .addOption(DIRECTORY)
          .addOption(TIME)
          .addOption(LOCATION);
}
