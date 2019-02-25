package com.hzyi.google.photos.uploader;

import static com.google.common.io.Files.getFileExtension;
import static com.google.common.io.Files.getNameWithoutExtension;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class SanitizeApp implements App<SanitizeAppConfig> {

  private static final ImmutableSet<String> JPEC_EXTENTIONS =
      ImmutableSet.of("jpeg", "JPEG", "jpg", "JPG");
  private static final ImmutableSet<String> RAW_EXTENTIIONS =
      ImmutableSet.of("RAW", "raw", "NEF", "nef");

  private final HashMap<String, Path> jpegs = new HashMap<>();
  private final HashMap<String, Path> raws = new HashMap<>();
  // photos exported from Lightroom might have a suffix -0x, where x is a number
  // unable to handle this situation for now
  private final HashMap<String, Path> processed = new HashMap<>();

  public void run(SanitizeAppConfig config) {
    photos(config.directories(), config.recursive())
        .filter(SanitizeApp::isJpeg)
        .forEach(
            p ->
                addToMap(
                    getNameWithoutExtension(p.getFileName().toString()),
                    p.getFileName().toString(),
                    p,
                    jpegs));
    photos(config.directories(), config.recursive())
        .filter(SanitizeApp::isRaw)
        .forEach(
            p ->
                addToMap(
                    getNameWithoutExtension(p.getFileName().toString()),
                    p.getFileName().toString(),
                    p,
                    raws));
    photos(config.processedDirectories(), config.recursive())
        .filter(SanitizeApp::isJpeg)
        .forEach(
            p ->
                addToMap(
                    getNameWithoutExtension(p.getFileName().toString()),
                    p.getFileName().toString(),
                    p,
                    processed));
    String destRoot = config.destinationDirectory();
    setUpDestDirs(destRoot);
    processed
        .values()
        .stream()
        .forEach(p -> copy(p, Destination.EDITED_JPEG.toPath(destRoot), config.debug()));
    jpegs
        .values()
        .stream()
        .forEach(p -> copy(p, Destination.ORIGINAL_JEPG.toPath(destRoot), config.debug()));
    raws.entrySet()
        .stream()
        .filter(entry -> processed.containsKey(entry.getKey()))
        .map(entry -> entry.getValue())
        .forEach(p -> copy(p, Destination.EDITED_RAW.toPath(destRoot), config.debug()));
    raws.entrySet()
        .stream()
        .filter(entry -> !processed.containsKey(entry.getKey()))
        .filter(entry -> jpegs.containsKey(entry.getKey()))
        .map(entry -> entry.getValue())
        .forEach(p -> copy(p, Destination.ORIGINAL_RAW.toPath(destRoot), config.debug()));
  }

  enum Destination {
    ORIGINAL_JEPG("original/jpeg/"),
    ORIGINAL_RAW("original/raw/"),
    EDITED_JPEG("edited/jpeg/"),
    EDITED_RAW("edited/raw/");

    private String name;

    Destination(String name) {
      this.name = name;
    }

    Path toPath(String root) {
      try {
        return Paths.get(root).resolve(this.name);
      } catch (Exception e) {
        throw new IllegalStateException("Unable to get direction");
      }
    }
  }

  private void setUpDestDirs(String root) {
    try {
      Files.createDirectories(Paths.get(root).resolve("original/jpeg"));
      Files.createDirectories(Paths.get(root).resolve("original/raw"));
      Files.createDirectories(Paths.get(root).resolve("edited/jpeg"));
      Files.createDirectories(Paths.get(root).resolve("edited/raw"));
    } catch (IOException e) {
      throw new IllegalArgumentException("Illegal dest directory " + root + " due to: " + e);
    }
  }

  private void copy(Path from, Path toDir, boolean debug) {
    Path to = toDir.resolve(from.getFileName().toString());
    if (debug) {
      System.out.printf("Copying file: \"%s\"\n", from);
      System.out.printf("to \"%s\"\n", to);
      return;
    }
    try {
      Files.copy(from, to);
    } catch (IOException e) {
      throw new IllegalStateException(
          "Unable to copy " + from.toString() + " to " + to.toString() + " due to: " + e);
    }
  }

  private Stream<Path> photos(List<String> paths, boolean recursive) {
    Stream<Path> files = Stream.empty();
    for (String dir : paths) {
      try {
        if (recursive) {
          files = Stream.concat(files, Files.walk(Paths.get(dir)).filter(Files::isRegularFile));
        } else {
          files = Stream.concat(files, Files.list(Paths.get(dir)).filter(Files::isRegularFile));
        }
      } catch (IOException e) {
        throw new IllegalArgumentException("cannot open directory " + dir + " due to: " + e);
      }
    }
    return files;
  }

  private void addToMap(
      String photoName, String fullName, Path path, HashMap<String, Path> destMap) {
    Preconditions.checkArgument(
        destMap.put(photoName, path) == null, "Multiple photos named: %s", fullName);
  }

  private static boolean isJpeg(Path path) {
    return JPEC_EXTENTIONS.contains(getFileExtension(path.getFileName().toString()));
  }

  private static boolean isRaw(Path path) {
    return RAW_EXTENTIIONS.contains(getFileExtension(path.getFileName().toString()));
  }
}
