package com.hzyi.google.photos.uploader;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class SanitizeAppConfig implements AppConfig {

  public String appName() {
    return "SanitizeApp";
  }

  public abstract boolean debug();

  public abstract boolean recursive();

  public abstract boolean force();

  public abstract List<String> directories();

  public abstract String processedDirectory();

  public abstract String destinationDirectory();

  public static SanitizeAppConfig fromCommandLine(CommandLine cl) {
    
  }
}