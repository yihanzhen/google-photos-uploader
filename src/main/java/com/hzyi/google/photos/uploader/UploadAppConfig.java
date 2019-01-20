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

  public abstract String file(); // fileName

  public abstract String location();

  public abstract String time();

  public static UploadAppConfig fromCommandLine(CommandLine cl) {
    return new AutoValue_UploadAppConfig(
        cl.hasOption(UploaderMain.OPTION_DEBUG.getOpt()),
        cl.hasOption(UploaderMain.OPTION_UPLOAD_ALL.getOpt()),
        cl.hasOption(UploaderMain.OPTION_UPLOAD_APPEND.getOpt()),
        cl.getOptionValue(UploaderMain.OPTION_UPLOAD_ALBUM.getOpt()),
        cl.getOptionValue(UploaderMain.OPTION_UPLOAD_DIRECTORY.getOpt()),
        cl.getOptionValue(UploaderMain.OPTION_UPLOAD_FILE.getOpt()),
        cl.getOptionValue(UploaderMain.OPTION_UPLOAD_LOCATION.getOpt()),
        cl.getOptionValue(UploaderMain.OPTION_UPLOAD_TIME.getOpt()));
  }
}
