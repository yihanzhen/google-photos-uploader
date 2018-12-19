package com.hzyi.google.photos.uploader;

import org.apache.commons.cli.Option;

public class UploaderMain {

  private static final String GOAL_UPLOAD = "upload";
  private static final String GOAL_LIST = "list";

  private static final Option OPTION_UPLOAD_ALL =
      new Option("-a", "-all", false, "upload all photos with default album names");
  private static final Option OPTION_UPLOAD_ALBUM =
      new Option("-ab", "--album", true, "upload all photos to an existing album");
  private static final Option OPTION_UPLOAD_FILE =
      new Option("-f", "--file", true, "upload a certain photo");
  private static final Option OPTION_UPLOAD_DIRECTORY =
      new Option("-dir", "--directory", true, "upload all photos in a directory");
  private static final Option OPTION_UPLOAD_DEBUG =
      new Option("-d", "--debug", false, "print operations instead of doing the real uploads");
  private static final Option OPTION_UPLOAD_LOCATION =
      new Option("-l", "--location", true, "prefix the location before album names");
  private static final Option OPTION_UPLOAD_TIME =
      new Option("-t", "--time", true, "prefix the time to before the album names");
      

  public static void main(String[] args) {

  }


}