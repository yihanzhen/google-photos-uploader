package com.hzyi.google.photos.uploader.util;

import com.hzyi.google.photos.uploader.App;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class LoggerFactory {

  private LoggerFactory() {
    // util class
  }

  public static Logger getLogger(App app) {
    Logger logger = Logger.getLogger(app.getClass().getName());
    logger.setLevel(Level.ALL);
    logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
    return logger;
  }
}
