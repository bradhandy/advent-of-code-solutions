package com.dbjgb.advent._2021.puzzle._02;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("(up|down|forward) (\\d+)");

  public static void main(String... args) throws Exception {
    printDepthAndPosition();
    printDepthAndPositionUsingAim();
  }

  private static void printDepthAndPosition() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("_2021/puzzle/_02/input.txt")) {
      int forwardPosition = 0;
      int depth = 0;

      String instruction;
      while ((instruction = inputReader.readLine()) != null) {
        if (instruction.trim().isEmpty()) {
          continue;
        }

        MatchResult result =
            INSTRUCTION_PATTERN.matcher(instruction).results().findFirst().orElse(null);
        if (result == null) {
          throw new IllegalStateException("Instruction required.");
        }

        int changeInPosition = Integer.parseInt(result.group(2));
        String direction = result.group(1);
        if (direction.equals("forward")) {
          forwardPosition += changeInPosition;
        } else {
          depth += (changeInPosition * (direction.equals("up") ? -1 : 1));
        }
      }

      System.out.printf(
          "forward position = %d; depth = %d; product = %d\n",
          forwardPosition, depth, forwardPosition * depth);
    }
  }

  private static void printDepthAndPositionUsingAim() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("_2021/puzzle/_02/input.txt")) {
      int forwardPosition = 0;
      int depth = 0;
      int aim = 0;

      String instruction;
      while ((instruction = inputReader.readLine()) != null) {
        if (instruction.trim().isEmpty()) {
          continue;
        }

        MatchResult result =
            INSTRUCTION_PATTERN.matcher(instruction).results().findFirst().orElse(null);
        if (result == null) {
          throw new IllegalStateException("Instruction required.");
        }

        int changeInPosition = Integer.parseInt(result.group(2));
        String direction = result.group(1);
        if (direction.equals("forward")) {
          forwardPosition += changeInPosition;
          depth += (changeInPosition * aim);
        } else {
          aim += (changeInPosition * (direction.equals("up") ? -1 : 1));
        }
      }

      System.out.printf(
          "forward position = %d; depth = %d; product = %d\n",
          forwardPosition, depth, forwardPosition * depth);
    }
  }
}
