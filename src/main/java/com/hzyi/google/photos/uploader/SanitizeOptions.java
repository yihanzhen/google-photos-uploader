package com.hzyi.google.photos.uploader;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

final class SanitizeOptions {
  private SanitizeOptions() {
    // static utility class
  }

  static final Option DEBUG =
      Option.builder("d")
          .longOpt("debug")
          .hasArg(false)
          .required(false)
          .desc("print operations instead of handling files")
          .build();

  static final Option RECURSIVE =
      Option.builder("r")
          .longOpt("recursive")
          .hasArg(false)
          .required(false)
          .desc("detect photos recursively")
          .build();

  static final Option DIRECTORY =
      Option.builder("dir")
          .longOpt("directory")
          .hasArg()
          .required()
          .desc("directories where photos to organize are located")
          .build();

  static final Option PROCESSED_DIRECTORY =
      Option.builder("p")
          .longOpt("processed")
          .hasArg()
          .required()
          .desc("directory where processed photo are located")
          .build();

  static final Option DESTINATION_DIRECTORY =
      Option.builder("dest")
          .longOpt("destination")
          .hasArg()
          .required()
          .desc("destination root directory where organized photos are located")
          .build();

  static final Options SANITIZE_OPTIONS =
      new Options()
          .addOption(DEBUG)
          .addOption(RECURSIVE)
          .addOption(DIRECTORY)
          .addOption(PROCESSED_DIRECTORY)
          .addOption(DESTINATION_DIRECTORY);
}
