package com.dbjgb.advent.twenty.puzzle.three;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;

public class Solution {

  public static void main(String... args) throws Exception {
    long oneByOneSlopeTrees = getNumberOfTreesForSlope(1, 1);
    long threeByOneSlopeTrees = getNumberOfTreesForSlope(3, 1);
    long fiveByOneSlopeTrees = getNumberOfTreesForSlope(5, 1);
    long sevenByOneSlopeTrees = getNumberOfTreesForSlope(7, 1);
    long oneByTwoSlopeTrees = getNumberOfTreesForSlope(1, 2);

    System.out.printf("Encountered %d trees (1, 1).\n", oneByOneSlopeTrees);
    System.out.printf("Encountered %d trees (3, 1).\n", threeByOneSlopeTrees);
    System.out.printf("Encountered %d trees (5, 1).\n", fiveByOneSlopeTrees);
    System.out.printf("Encountered %d trees (7, 1).\n", sevenByOneSlopeTrees);
    System.out.printf("Encountered %d trees (1, 2).\n", oneByTwoSlopeTrees);

    // original product:  3671623800
    System.out.printf(
        "Product is %d.",
        oneByOneSlopeTrees
            * threeByOneSlopeTrees
            * fiveByOneSlopeTrees
            * sevenByOneSlopeTrees
            * oneByTwoSlopeTrees);
  }

  private static long getNumberOfTreesForSlope(
      int horizontalDifference, int rowsToReadEachIteration) throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/three/input.txt")) {
      int currentPosition = 0;
      long numberOfTrees = 0;

      String line;
      while ((line = inputReader.readLine()) != null) {
        StringBuilder mutableLine = new StringBuilder(line);
        int characterPosition = (line.length() + currentPosition) % line.length();
        if (line.charAt(characterPosition) == '#') {
          mutableLine.setCharAt(characterPosition, 'X');
          numberOfTrees += 1;
        }
        // System.out.println(mutableLine);

        currentPosition += horizontalDifference;
        skipLines(inputReader, rowsToReadEachIteration - 1);
      }

      return numberOfTrees;
    }
  }

  private static void skipLines(BufferedReader inputReader, int linesToSkip) throws IOException {
    String line;
    for (int i = 0; i < linesToSkip && (line = inputReader.readLine()) != null; i++) {
      // System.out.println(line);
    }
  }
}
