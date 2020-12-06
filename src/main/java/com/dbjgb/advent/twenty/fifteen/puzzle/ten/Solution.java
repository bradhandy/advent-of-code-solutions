package com.dbjgb.advent.twenty.fifteen.puzzle.ten;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern DIGIT_PATTERN = Pattern.compile("((\\d)(?:\\2)*)");
  private static final String BASE_VALUE = "1113222113";

  public static void main(String... args) {
    System.out.printf(
        "The final pattern length after 40 times is %d.\n",
        applyLookAndSayAlgorithm(40).length());
    System.out.printf(
        "The final pattern length after 50 times is %d.\n",
        applyLookAndSayAlgorithm(50).length());
  }

  private static String applyLookAndSayAlgorithm(int iterations) {
    String currentValue = BASE_VALUE;
    for (int iteration = 0; iteration < iterations; iteration++) {
      StringBuilder nextValueBuilder = new StringBuilder();
      Matcher digitMatcher = DIGIT_PATTERN.matcher(currentValue);
      while (digitMatcher.find()) {
        nextValueBuilder.append(digitMatcher.group(1).length()).append(digitMatcher.group(2));
      }
      currentValue = nextValueBuilder.toString();
    }
    return currentValue;
  }
}
