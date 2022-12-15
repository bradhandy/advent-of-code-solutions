package com.dbjgb.advent._2015.puzzle._06;

import com.dbjgb.advent.Utility;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern INSTRUCTION_PATTERN =
      Pattern.compile("((?:turn (?:on|off))|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)");

  public static void main(String... args) throws Exception {
    // printTotalTurnedOnLights();
    printTotalBrightness();
  }

  private static void printTotalBrightness() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("_2015/puzzle/_06/input.txt")) {
      Map<Point, Integer> lightBrightness = new HashMap<>();
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher instructionMatcher = INSTRUCTION_PATTERN.matcher(line);
        if (instructionMatcher.matches()) {
          String action = instructionMatcher.group(1);
          Point lowerLeft =
              new Point(
                  Integer.parseInt(instructionMatcher.group(2)),
                  Integer.parseInt(instructionMatcher.group(3)));
          Point upperRight =
              new Point(
                  Integer.parseInt(instructionMatcher.group(4)),
                  Integer.parseInt(instructionMatcher.group(5)));

          for (int dx = 0; dx <= (upperRight.getX() - lowerLeft.getX()); dx++) {
            for (int dy = 0; dy <= (upperRight.getY() - lowerLeft.getY()); dy++) {
              Point currentPoint = lowerLeft.getLocation();
              currentPoint.translate(dx, dy);

              int brightness = Optional.ofNullable(lightBrightness.get(currentPoint)).orElse(0);
              if (action.endsWith("on")) {
                brightness += 1;
              } else if (action.endsWith("off")) {
                brightness = Math.max(brightness - 1, 0);
              } else {
                brightness += 2;
              }

              lightBrightness.put(currentPoint, brightness);
            }
          }
        }
      }

      System.out.printf(
          "Total brightness: %d\n",
          lightBrightness.values().stream().reduce(0, (total, increment) -> total + increment));
    }
  }

  private static void printTotalTurnedOnLights() throws IOException {
    try (BufferedReader inputReadee = Utility.openInputFile("_2015/puzzle/_06/input.txt")) {
      Set<Point> turnedOnLights = new HashSet<>();
      String line;
      while ((line = inputReadee.readLine()) != null) {
        Matcher instructionMatcher = INSTRUCTION_PATTERN.matcher(line);
        if (instructionMatcher.matches()) {
          String action = instructionMatcher.group(1);
          Point lowerLeft =
              new Point(
                  Integer.parseInt(instructionMatcher.group(2)),
                  Integer.parseInt(instructionMatcher.group(3)));
          Point upperRight =
              new Point(
                  Integer.parseInt(instructionMatcher.group(4)),
                  Integer.parseInt(instructionMatcher.group(5)));

          for (int dx = 0; dx <= (upperRight.getX() - lowerLeft.getX()); dx++) {
            for (int dy = 0; dy <= (upperRight.getY() - lowerLeft.getY()); dy++) {
              Point currentPoint = lowerLeft.getLocation();
              currentPoint.translate(dx, dy);

              if (action.endsWith("on")) {
                turnedOnLights.add(currentPoint);
              } else if (action.endsWith("off")) {
                turnedOnLights.remove(currentPoint);
              } else if (!turnedOnLights.remove(currentPoint)) {
                turnedOnLights.add(currentPoint);
              }
            }
          }
        }
      }

      System.out.printf("Number of lights on: %d\n", turnedOnLights.size());
    }
  }
}
