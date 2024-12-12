package com.dbjgb.advent._2024.puzzle._02;

import com.dbjgb.advent.Utility;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Solution {

  public static void main(String... args) throws Exception {
    solvePartOne();
    solvePartTwo();
  }

  private static void solvePartOne() throws Exception {
    long safeReports = 0;
    for (String line : Utility.readAllLines("_2024/puzzle/_02/input.txt")) {
      if (isSafeReportPartOne(line)) {
        safeReports++;
      }
    }

    System.out.printf("Part One: %d\n", safeReports);
  }

  private static boolean isSafeReportPartOne(String line) {
    AtomicInteger direction = new AtomicInteger(0);
    AtomicBoolean safe = new AtomicBoolean(true);
    Arrays.stream(line.split(" "))
        .map(Integer::parseInt)
        .reduce(-1, (x, y) -> {
          if (safe.get() && x != -1) {
            int newDirection = (int) Math.signum(y - x);
            boolean directionIsSafe = (direction.compareAndSet(0, newDirection)
                || direction.compareAndSet(newDirection, newDirection));
            boolean levelIsSafe = ((newDirection == 1 && ((x + 1) <= y && y <= (x + 3)))
                || (newDirection == -1 && ((x - 3) <= y && y <= (x - 1))));

            safe.set(directionIsSafe && levelIsSafe);
          }

          return y;
        });
    return safe.get();
  }

  private static void solvePartTwo() throws Exception {
    long safeReports = 0;
    for (String line : Utility.readAllLines("_2024/puzzle/_02/input.txt")) {
      if (isSafeReportPartTwo(line)) {
        safeReports++;
      }
    }

    System.out.printf("Part Two: %d\n", safeReports);
  }

  private static boolean isSafeReportPartTwo(String line) {
    String[] readings = line.split(" ");
    for (int r = 0; r < readings.length; r++) {
      ArrayList updatedReadings = new ArrayList(List.of(readings));
      updatedReadings.remove(r);

      if (isSafeReportPartOne(Joiner.on(" ").join(updatedReadings))) {
        return true;
      }
    }

    return false;
  }
}
