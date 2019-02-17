package com.hzyi.google.photos.uploader;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
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

  private static final ImmutableList<Path> directories =
      ImmutableList.of(
          root, fromDir, fromDir1, fromDir2, processedDir, processedDir1, processedDir2, destDir);

  private static void deleteWrapper(Path path) {
    try {
      Files.delete(path);
    } catch (IOException e) {
      throw new RuntimeException("Unable to delete file due to: " + e);
    }
  }

  private static void createDirectoriesWrapper(Path path) {
    try {
      Files.createDirectories(path);
    } catch (IOException e) {
      throw new RuntimeException("Unable to create directories " + path + " due to: " + e);
    }
  }

  @Before
  public void setUp() throws Exception {
    if (Files.exists(root)) {
      Files.walk(root).sorted(Comparator.reverseOrder()).forEach(SanitizeAppTest::deleteWrapper);
    }
    directories.forEach(SanitizeAppTest::createDirectoriesWrapper);
  }

  @After
  public void tearDown() throws Exception {
    if (Files.exists(root)) {
      Files.walk(root).sorted(Comparator.reverseOrder()).forEach(SanitizeAppTest::deleteWrapper);
    }
  }

  @Test
  public void testMoveJpegs() throws Exception {
    Files.createFile(fromDir.resolve("1111.jpg"));
    Files.createFile(fromDir.resolve("2222.jpeg"));
    Files.createFile(fromDir1.resolve("3333.JPG"));
    Files.createFile(fromDir1.resolve("4444.JPEG"));
    SanitizeAppConfig config =
        SanitizeAppConfig.newBuilder()
            .directories(ImmutableList.of(fromDirStr, fromDirStr1))
            .processedDirectories(ImmutableList.of(processedDirStr))
            .destinationDirectory(destDirStr)
            .build();
    new SanitizeApp().run(config);
    assertThat(destSubdirectoryFileNames("original/jpeg"))
        .containsExactly("1111.jpg", "2222.jpeg", "3333.JPG", "4444.JPEG");
  }

  @Test
  public void testMoveJpegsAndRaws() throws Exception {
    Files.createFile(fromDir.resolve("1111.jpg"));
    Files.createFile(fromDir.resolve("2222.jpeg"));
    Files.createFile(fromDir1.resolve("3333.JPG"));
    Files.createFile(fromDir1.resolve("4444.JPEG"));
    Files.createFile(fromDir1.resolve("1111.nef"));
    Files.createFile(fromDir2.resolve("2222.NEF"));
    Files.createFile(fromDir2.resolve("3456.nef"));
    Files.createFile(fromDir2.resolve("4444.raw"));
    SanitizeAppConfig config =
        SanitizeAppConfig.newBuilder()
            .directories(ImmutableList.of(fromDirStr, fromDirStr1, fromDirStr2))
            .processedDirectories(ImmutableList.of(processedDirStr))
            .destinationDirectory(destDirStr)
            .build();
    new SanitizeApp().run(config);
    List<String> destOriginalJpegs = destSubdirectoryFileNames("original/jpeg");
    List<String> destOriginalRaws = destSubdirectoryFileNames("original/raw");
    assertThat(destOriginalJpegs).containsExactly("1111.jpg", "2222.jpeg", "3333.JPG", "4444.JPEG");
    assertThat(destOriginalRaws).containsExactly("1111.nef", "2222.NEF", "4444.raw");
  }

  @Test
  public void testMoveJpegsAndRawsWithProcessed() throws Exception {
    Files.createFile(fromDir.resolve("1111.jpg"));
    Files.createFile(fromDir1.resolve("2222.jpeg"));
    Files.createFile(fromDir2.resolve("4444.JPEG"));
    Files.createFile(fromDir1.resolve("1111.nef"));
    Files.createFile(fromDir1.resolve("2222.NEF"));
    Files.createFile(fromDir2.resolve("3456.nef"));
    Files.createFile(fromDir2.resolve("4444.raw"));
    Files.createFile(processedDir.resolve("1111.jpg"));
    Files.createFile(processedDir1.resolve("2222.jpeg"));
    SanitizeAppConfig config =
        SanitizeAppConfig.newBuilder()
            .directories(ImmutableList.of(fromDirStr, fromDirStr1, fromDirStr2))
            .processedDirectories(ImmutableList.of(processedDirStr, processedDirStr1))
            .destinationDirectory(destDirStr)
            .build();
    new SanitizeApp().run(config);
    assertThat(destSubdirectoryFileNames("original/jpeg"))
        .containsExactly("1111.jpg", "2222.jpeg", "4444.JPEG");
    assertThat(destSubdirectoryFileNames("original/raw")).containsExactly("4444.raw");
    assertThat(destSubdirectoryFileNames("edited/jpeg")).containsExactly("1111.jpg", "2222.jpeg");
    assertThat(destSubdirectoryFileNames("edited/raw")).containsExactly("1111.nef", "2222.NEF");
  }

  @Test
  public void testMoveJpegsAndRawsRecursive() throws Exception {
    Files.createFile(fromDir.resolve("1111.jpg"));
    Files.createDirectories(fromDir.resolve("sub"));
    Files.createFile(fromDir.resolve("sub/2222.jpeg"));
    Files.createFile(fromDir.resolve("sub/2222.raw"));
    Files.createFile(processedDir1.resolve("2222.jpeg"));
    SanitizeAppConfig config =
        SanitizeAppConfig.newBuilder()
            .recursive(true)
            .directories(ImmutableList.of(fromDirStr, fromDirStr1, fromDirStr2))
            .processedDirectories(ImmutableList.of(processedDirStr, processedDirStr1))
            .destinationDirectory(destDirStr)
            .build();
    new SanitizeApp().run(config);
    assertThat(destSubdirectoryFileNames("original/jpeg")).containsExactly("1111.jpg", "2222.jpeg");
    assertThat(destSubdirectoryFileNames("original/raw")).isEmpty();
    assertThat(destSubdirectoryFileNames("edited/jpeg")).containsExactly("2222.jpeg");
    assertThat(destSubdirectoryFileNames("edited/raw")).containsExactly("2222.raw");
  }

  private List<String> destSubdirectoryFileNames(String subdir) {
    Preconditions.checkArgument(
        ImmutableSet.of("original/jpeg", "original/raw", "edited/jpeg", "edited/raw")
            .contains(subdir),
        "Bad directory name: " + subdir);
    try {
      return Files.list(destDir.resolve(subdir))
          .map(p -> p.getFileName().toString())
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("Unable to list files in: " + subdir + " due to: " + e);
    }
  }
}
