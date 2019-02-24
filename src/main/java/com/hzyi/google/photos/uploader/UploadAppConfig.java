package com.hzyi.google.photos.uploader;

import com.google.auto.value.AutoValue;
import org.apache.commons.cli.CommandLine;

@AutoValue
public abstract class UploadAppConfig implements AppConfig {

  public String appName() {
    return "UploadApp";
  }

  public abstract boolean debug(); // runDebugMode

  public abstract boolean all(); // uploadAllPhotosInDirectory

  public abstract boolean append(); // appendPhotosToExistingAlbum

  public abstract String album(); // albumName

  public abstract String directory(); // directoryName

  // TODO: support files
  public abstract String file(); // fileName

  public abstract String location();

  public abstract String time();

  public static UploadAppConfig fromCommandLine(CommandLine cl) {
    return new AutoValue_UploadAppConfig(
        cl.hasOption(UploadOptions.DEBUG.getOpt()),
        cl.hasOption(UploadOptions.ALL.getOpt()),
        cl.hasOption(UploadOptions.APPEND.getOpt()),
        cl.getOptionValue(UploadOptions.ALBUM.getOpt()),
        cl.getOptionValue(UploadOptions.DIRECTORY.getOpt()),
        cl.getOptionValue(UploadOptions.FILE.getOpt()),
        cl.getOptionValue(UploadOptions.LOCATION.getOpt()),
        cl.getOptionValue(UploadOptions.TIME.getOpt()));
  }
}
