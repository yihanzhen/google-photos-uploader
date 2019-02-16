package com.hzyi.google.photos.uploader;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SanitizeAppTest {
  private static final String rootStr = "/tmp/google-photos-uploader/sanitize";
  private static final String fromDirStr = "/tmp/google-photos-uploader/sanitize/from/";
  private static final String fromDirStr1 = "/tmp/google-photos-uploader/sanitize/from1/";
  private static final String fromDirStr2 = "/tmp/google-photos-uploader/sanitize/from2/";
  private static final String processedDirStr = "/tmp/google-photos-uploader/sanitize/processed/";
  private static final String processedDirStr1 = "/tmp/google-photos-uploader/sanitize/processed1/";
  private static final String processedDirStr2 = "/tmp/google-photos-uploader/sanitize/processed2/";
  private static final String destDirStr = "/tmp/google-photos-uploader/sanitize/dest";

  private static final Path root = Paths.get(rootStr);
  private static final Path fromDir = Paths.get(fromDirStr);
  private static final Path fromDir1 = Paths.get(fromDirStr1);
  private static final Path fromDir2 = Paths.get(fromDirStr2);
  private static final Path processedDir = Paths.get(processedDirStr);
  private static final Path processedDir1 = Paths.get(processedDirStr1);
  private static final Path processedDir2 = Paths.get(processedDirStr2);
  private static final Path destDir = Paths.get(destDirStr);

  private static void deleteWrapper(Path path) {
    try {
      Files.delete(path);
    } catch (IOException e) {
      throw new RuntimeException("Unable to delete file due to: " + e);
    }
  }

  @Before
  public void setUp() throws Exception {
    if (Files.exists(root)) {
      Files.walk(root).sorted(Comparator.reverseOrder()).forEach(SanitizeAppTest::deleteWrapper);
    } else {
      Files.createDirectories(root);
    }
  }

  @After
  public void tearDown() throws Exception {
    if (Files.exists(root)) {
      Files.walk(root).sorted(Comparator.reverseOrder()).forEach(SanitizeAppTest::deleteWrapper);
    }
  }

  @Test
  public void testMoveJpegs() throws Exception {
    Files.createDirectories(fromDir);
    Files.createDirectories(fromDir1);
    Files.createDirectories(destDir);
    Files.createDirectories(processedDir);
    Files.createFile(fromDir.resolve("1111.jpg"));
    Files.createFile(fromDir.resolve("2222.jpeg"));
    Files.createFile(fromDir1.resolve("3333.JPG"));
    Files.createFile(fromDir1.resolve("4444.JPEG"));
    SanitizeAppConfig config =
        SanitizeAppConfig.newBuilder()
            .debug(false)
            .force(false)
            .recursive(false)
            .directories(ImmutableList.of(fromDirStr, fromDirStr1))
            .processedDirectories(ImmutableList.of(processedDirStr))
            .destinationDirectory(destDirStr)
            .build();
    new SanitizeApp().run(config);
    assertThat(
            Files.list(destDir.resolve("original/jpeg"))
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList()))
        .containsExactly("1111.jpg", "2222.jpeg", "3333.JPG", "4444.JPEG");
  }

  @Test
  public void testMoveJpegsAndRaws() {}

  @Test
  public void testRemoveUnwantedRaws() {}
}
