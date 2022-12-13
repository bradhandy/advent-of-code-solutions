package com.dbjgb.advent._2016.puzzle._03;

import com.dbjgb.advent.Utility;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {

  private static final Pattern TRIANGLE_DIMENSIONS_PATTERN = Pattern.compile("\\s*(\\d+)");

  public static void main(String... args) throws Exception {
    findPossibleValidTrianglesByRow();
    findPossibleTrianglesByColumn();
  }

  private static void findPossibleValidTrianglesByRow() throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("_2016/puzzle/_03/input.txt")) {
      List<List<Integer>> validDimensions = Lists.newArrayList();
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher dimensionsMatcher = TRIANGLE_DIMENSIONS_PATTERN.matcher(line);
        List<Integer> dimensions =
            dimensionsMatcher
                .results()
                .map(matchResult -> Integer.valueOf(matchResult.group(1)))
                .collect(Collectors.toList());
        boolean valid = true;
        for (int i = 0; i < dimensions.size() && valid; i++) {
          int numSides = dimensions.size();
          valid =
              valid
                  && (dimensions.get(i) + dimensions.get((i + 1) % numSides)
                      > dimensions.get((i + 2) % numSides));
        }

        if (valid) {
          validDimensions.add(dimensions);
        }
      }

      System.out.printf("There are %d valid dimensions.\n", validDimensions.size());
    }
  }

  private static void findPossibleTrianglesByColumn() throws Exception {
    try (LineNumberReader inputReader =
        new LineNumberReader(
            new InputStreamReader(Utility.openInputStream("_2016/puzzle/_03/input.txt")))) {
      List<List<Integer>> dimensions = Lists.newArrayList();
      String line;
      while ((line = inputReader.readLine()) != null) {
        int triangleSet = (inputReader.getLineNumber() - 1) / 3;
        Matcher dimensionsMatcher = TRIANGLE_DIMENSIONS_PATTERN.matcher(line);
        List<Integer> dimensionsLine =
            dimensionsMatcher
                .results()
                .map(matchResult -> Integer.valueOf(matchResult.group(1)))
                .collect(Collectors.toList());
        for (int i = 0; i < dimensionsLine.size(); i++) {
          int triangle = (triangleSet * 3) + i;
          if (dimensions.size() == triangle) {
            dimensions.add(Lists.newArrayList(dimensionsLine.get(i)));
          } else {
            dimensions.get(triangle).add(dimensionsLine.get(i));
          }
        }
      }

      int validDimensionsCount = 0;
      for (List<Integer> triangleDimensions : dimensions) {
        boolean valid = true;
        int numSides = triangleDimensions.size();
        for (int i = 0; i < numSides && valid; i++) {
          valid &=
              (triangleDimensions.get(i) + triangleDimensions.get((i + 1) % numSides)
                  > triangleDimensions.get((i + 2) % numSides));
        }

        if (valid) {
          validDimensionsCount++;
        }
      }

      System.out.printf("There are %d valid dimensions.\n", validDimensionsCount);
    }
  }
}
