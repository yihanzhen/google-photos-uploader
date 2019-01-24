package com.hzyi.google.photos.uploader;

import static com.google.common.io.Files.getFileExtension;
import static com.google.common.io.Files.getNameWithoutExtension;

import com.google.auto.value.AutoValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SanitizeApp implements App<SanitizeAppConfig> {

  private static final String JPEG_EXT = "jpeg";
  private static final String NEF_EXT = "nef";

  private LinkedHashMap<String, Path> jpegs;
  private LinkedHashMap<String, Path> nefs;

  public void run(SanitizeAppConfig config) {
    config
        .directories()
        .stream()
        .map(SanitizeApp::pathOf)
        .filter(Objects::nonnull)
        .flatMap(
            d -> config.recursive() 
                ? Files.walk(d) 
                : Files.list(d).filter(Files::isRegularFile))
        .forEach(p -> pop(p, config));
    Set<String> processedJpegs =
        Files
            .list(pathOf(config.processedDirectory()))
            .filter(Files::isRegularFile)
            .map(getFileExtension(p.getFileName().toString()))
            .filter(name -> name.equals(JPEG_EXT))
            .collect(Collectors.toSet());
    
  }

  private void pop(Path path, SanitizeAppConfig config) {
    String fileName = path.getFileName().toString();
    String ext = getFileExtension(fileName).toLowerCase();
    String fileNameWithoutExt = getNameWithoutExtension(fileName);
    if (JPEG_EXT.equals(ext)) {
      jpegs.put(fileNameWithoutExt, path);
    } else if (NEF_EXT.equals(ext)) {
      nefs.put(fileNameWithoutExt, path);
    }
  }

  private void removeOrMoveToTrash(Path path, SanitizeAppConfig config) {

  }

  private static Path pathOf(String directoryName) {
    try {
      return Paths.get(directoryName);
    } catch (IOException e) {
      System.out.println("Illegal path: " + directoryName + ", skipping.");
    }
    return null;
  }

  
}