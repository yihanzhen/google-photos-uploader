package com.hzyi.google.photos.uploader;

import com.google.auto.value.AutoValue;
import java.util.Path;
import java.nio.Path;
import java.util.stream.Collectors;

public class SanitizeApp implements App<SanitizeAppConfig> {

  private LinkedHashMap<String, Path> jpegs;
  private LinkedHashMap<String, Path> nefs;

  public void run(SanitizeAppConfig config) {
    List<Path> dirs =
        config
            .directories()
            .stream()
            .map(Paths::get)
            .collect(Collectors.toList());
  }

  
}