package com.hzyi.google.photos.uploader;

import com.google.auto.value.AutoValue;
import org.apache.commons.cli.CommandLine;

@AutoValue
public abstract class ListAlbumAppConfig implements AppConfig {

  @Override
  public String appName() {
    return "ListAlbumApp";
  }

  public abstract boolean debug();

  public static ListAlbumAppConfig fromCommandLine(CommandLine cl) {
    return new AutoValue_ListAlbumAppConfig.Builder()
        .debug(cl.hasOption(ListAlbumOptions.DEBUG.getOpt()))
        .build();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder debug(boolean val);

    public abstract ListAlbumAppConfig build();
  }
}
