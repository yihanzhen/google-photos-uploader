package com.hzyi.google.photos.uploader;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class SanitizeAppTest {

  private static final String fromDirStr = "/tmp/from/";
  private static final String fromDirStr1 = "/tmp/from1/";
  private static final String fromDirStr2 = "/tmp/from2/";
  private static final String processedDirStr = "/tmp/processed/";
  private static final String processedDirStr1 = "/tmp/processed1/";
  private static final String processedDirStr2 = "/tmp/processed2/";
  private static final String destDirStr = "/tmp/dest";
  
  private static final Path fromDir = Paths.get(fromDirStr);
  private static final Path fromDir1 = Paths.get(fromDirStr1);
  private static final Path fromDir2 = Paths.get(fromDirStr2);
  private static final Path processedDir = Paths.get(processedDirStr);
  private static final Path processedDir1 = Paths.get(processedDirStr1);
  private static final Path processedDir2 = Paths.get(processedDirStr2);
  private static final Path destDir = Paths.get(destDirStr);

  @Before
  public static setUp() {
    fromDirectory();
    fromDirectories();
    processedDirectory();
    processedDirectories();
    destDirectory();
  } 

  @Test
  public void testMoveJpegs() {}

  @Test
  public void testMoveJpegsAndRaws() {}

  @Test
  public void testRemoveUnwantedRaws() {}
}