package com.dbjgb.advent.twenty.fifteen.puzzle.two;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern DIMENSIONS_PATTERN = Pattern.compile("(\\d+)x(\\d+)x(\\d+)");

  public static void main(String... args) throws Exception {
    printTotalSquareFeetNeeded();
    printTotalRibbonLengthNeeded();
  }

  private static void printTotalSquareFeetNeeded() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/two/input.txt")) {
      long totalSurfaceArea = 0;
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher dimensionsMatcher = DIMENSIONS_PATTERN.matcher(line);
        if (dimensionsMatcher.matches()) {
          int length = Integer.valueOf(dimensionsMatcher.group(1));
          int width = Integer.valueOf(dimensionsMatcher.group(2));
          int height = Integer.valueOf(dimensionsMatcher.group(3));

          int lengthSurface = 2 * length * width;
          int widthSurface = 2 * width * height;
          int heightSurface = 2 * height * length;
          int minimumSurfaceSingleSide =
              Math.min(lengthSurface, Math.min(widthSurface, heightSurface)) / 2;

          totalSurfaceArea +=
              (lengthSurface + widthSurface + heightSurface + minimumSurfaceSingleSide);
        }
      }

      // original total:  1756282
      System.out.printf("Total Surface Area Required: %d\n", totalSurfaceArea);
    }
  }

  private static void printTotalRibbonLengthNeeded() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/two/input.txt")) {
      long totalRibbonLenth = 0;
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher dimensionsMatcher = DIMENSIONS_PATTERN.matcher(line);
        if (dimensionsMatcher.matches()) {
          int length = Integer.valueOf(dimensionsMatcher.group(1));
          int width = Integer.valueOf(dimensionsMatcher.group(2));
          int height = Integer.valueOf(dimensionsMatcher.group(3));

          int totalLength = 2 * length;
          int totalWidth = 2 * width;
          int totalHeight = 2 * height;
          int ribbonLength =
              totalLength
                  + totalWidth
                  + totalHeight
                  - Math.max(totalLength, Math.max(totalWidth, totalHeight));
          ribbonLength += (length * width * height);

          totalRibbonLenth += ribbonLength;
        }
      }

      // original total:  1756282
      System.out.printf("Total Ribbon Length Required: %d\n", totalRibbonLenth);
    }
  }
}
