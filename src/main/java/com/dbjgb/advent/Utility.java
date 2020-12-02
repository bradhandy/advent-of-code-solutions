package com.dbjgb.advent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class Utility {

  public static BufferedReader openInputFile(String path) {
    String fullPath = "com/dbjgb/advent/twenty/" + path;
    InputStream input =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(fullPath);
    return new BufferedReader(new InputStreamReader(input));
  }

  private Utility() {}
}
