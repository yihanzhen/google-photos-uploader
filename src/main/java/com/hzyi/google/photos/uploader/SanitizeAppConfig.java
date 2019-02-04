package com.hzyi.google.photos.uploader;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.commons.cli.CommandLine;

@AutoValue
abstract class SanitizeAppConfig implements AppConfig {

  public String appName() {
    return "SanitizeApp";
  }

  public abstract boolean debug();

  public abstract boolean recursive();

  public abstract boolean force();

  public abstract List<String> directories();

  public abstract List<String> processedDirectories();

  public abstract String destinationDirectory();

  public static SanitizeAppConfig fromCommandLine(CommandLine cl) {
    return new AutoValue_SanitizeAppConfig(
        cl.hasOption(SanitizeOptions.DEBUG.getOpt()),
        cl.hasOption(SanitizeOptions.RECURSIVE.getOpt()),
        cl.hasOption(SanitizeOptions.FORCE.getOpt()),
        ImmutableList.copyOf(cl.getOptionValues(SanitizeOptions.DIRECTORY.getOpt())),
        ImmutableList.copyOf(cl.getOptionValues(SanitizeOptions.PROCESSED_DIRECTORY.getOpt())),
        cl.getOptionValue(SanitizeOptions.DESTINATION_DIRECTORY.getOpt()));
  }
}
