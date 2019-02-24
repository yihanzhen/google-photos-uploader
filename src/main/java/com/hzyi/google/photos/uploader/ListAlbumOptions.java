package com.hzyi.google.photos.uploader;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

final class ListAlbumOptions {
  private ListAlbumOptions() {
    // static utility class
  }

  static final Option DEBUG =
      Option.builder("d")
          .longOpt("debug")
          .hasArg(false)
          .required(false)
          .desc("debugging mode")
          .build();

  static final Options LIST_ALBUM_OPTIONS = new Options().addOption(DEBUG);
}
