package com.hzyi.google.photos.uploader;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.apache.commons.cli.CommandLine;

@AutoValue
abstract class SanitizeAppConfig implements AppConfig {

  public String appName() {
    return "SanitizeApp";
  }

  public abstract boolean debug();

  public abstract boolean recursive();

  public abstract ImmutableList<String> directories();

  public abstract ImmutableList<String> processedDirectories();

  public abstract String destinationDirectory();

  public static SanitizeAppConfig fromCommandLine(CommandLine cl) {
    return new AutoValue_SanitizeAppConfig.Builder()
        .debug(cl.hasOption(SanitizeOptions.DEBUG.getOpt()))
        .recursive(cl.hasOption(SanitizeOptions.RECURSIVE.getOpt()))
        .directories(ImmutableList.copyOf(cl.getOptionValues(SanitizeOptions.DIRECTORY.getOpt())))
        .processedDirectories(
            ImmutableList.copyOf(cl.getOptionValues(SanitizeOptions.PROCESSED_DIRECTORY.getOpt())))
        .destinationDirectory(cl.getOptionValue(SanitizeOptions.DESTINATION_DIRECTORY.getOpt()))
        .build();
  }

  public static SanitizeAppConfig.Builder newBuilder() {
    return new AutoValue_SanitizeAppConfig.Builder().debug(false).recursive(false);
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder debug(boolean val);

    public abstract Builder recursive(boolean val);

    public abstract Builder directories(ImmutableList<String> val);

    public abstract Builder processedDirectories(ImmutableList<String> val);

    public abstract Builder destinationDirectory(String val);

    public abstract SanitizeAppConfig build();
  }
}
