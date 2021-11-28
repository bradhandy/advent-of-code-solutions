package com.dbjgb.advent.fifteen.puzzle.one;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;

public class Solution {

  public static void main(String... args) throws Exception {
    printFinalFloor();
    printPositionOfBasementInstruction();
  }

  private static void printFinalFloor() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/one/input.txt")) {
      String line = inputReader.readLine();
      String missingDownInstructions = line.replaceAll("\\)", "");
      int floorsUp = missingDownInstructions.length();
      int floorsDown = line.length() - missingDownInstructions.length();

      System.out.printf("Floor: %d\n", floorsUp - floorsDown);
    }
  }

  private static void printPositionOfBasementInstruction() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/one/input.txt")) {
      String line = inputReader.readLine();
      int currentFloor = 0;
      int position;

      // when the loop completes, position will have the correct value even though we started at
      // zero.  this is due to the incrementing statement being executed on the final pass before
      // the end condition is evaluated.
      for (position = 0; position < line.length() && currentFloor >= 0; position++) {
        currentFloor += (line.charAt(position) == '(') ? 1 : -1;
      }

      System.out.printf("Position: %d\n", position);
    }
  }

}
