package com.dbjgb.advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class Utility {

  public static BufferedReader openInputFile(String path) {
    return new BufferedReader(new InputStreamReader(openInputStream(path)));
  }

  public static InputStream openInputStream(String path) {
    String fullPath = "com/dbjgb/advent/" + path;
    InputStream input =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(fullPath);
    return input;
  }

  public static List<String> readAllLines(String path) throws URISyntaxException, IOException {
    String fullPath = "com/dbjgb/advent/" + path;
    URI fileUri = Thread.currentThread().getContextClassLoader().getResource(fullPath).toURI();

    return Files.readAllLines(Path.of(fileUri));
  }

  public static String readEntireFile(String path) throws URISyntaxException, IOException {
    String fullPath = "com/dbjgb/advent/" + path;
    URI fileUri = Thread.currentThread().getContextClassLoader().getResource(fullPath).toURI();

    return Files.readString(Path.of(fileUri));
  }

  private Utility() {}
}
