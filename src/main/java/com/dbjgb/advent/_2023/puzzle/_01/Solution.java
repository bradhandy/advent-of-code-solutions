package com.dbjgb.advent._2023.puzzle._01;

import com.dbjgb.advent.Utility;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  public static class Part01 {
    public static void main(String... args) throws Exception {
      long total = 0;

      Pattern firstDigitPattern = Pattern.compile("^[a-z]*(\\d)");
      Pattern lastDigitPattern = Pattern.compile("(\\d)[a-z]*$");
      for (String line : Utility.readAllLines("_2023/puzzle/_01/input.txt")) {
        Matcher firstDigitMatcher = firstDigitPattern.matcher(line);
        if (!firstDigitMatcher.find()) {
          throw new IllegalStateException("Couldn't find match in line: " + line);
        }
        String firstDigit = firstDigitMatcher.group(1);
        Matcher lastDigitMatcher = lastDigitPattern.matcher(line);
        if (!lastDigitMatcher.find()) {
          throw new IllegalStateException("Couldn't find match in line: " + line);
        }
        String lastDigit = lastDigitMatcher.group(1);

        System.out.printf("%s%s, line: %s\n", firstDigit, lastDigit, line);
        total += Integer.parseInt(String.format("%s%s", firstDigit, lastDigit));
      }

      System.out.printf("\n\nTotal: %d\n", total);
    }
  }

  public static class Part02 {

    enum Digit {
      ONE(1),
      TWO(2),
      THREE(3),
      FOUR(4),
      FIVE(5),
      SIX(6),
      SEVEN(7),
      EIGHT(8),
      NINE(9);

      private static Map<String, Digit> NAME_TO_DIGIT = Maps.uniqueIndex(Set.of(Digit.values()), (digit) -> digit.name().toLowerCase());
      private static Map<String, Digit> VALUE_TO_DIGIT = Maps.uniqueIndex(Set.of(Digit.values()), (digit) -> String.valueOf(digit.getValue()));

      int value;

      Digit(int value) {
        this.value = value;
      }

      int getValue() {
        return value;
      }

      public static Digit byNameOrValue(String value) {
        return (NAME_TO_DIGIT.containsKey(value)) ? NAME_TO_DIGIT.get(value) : VALUE_TO_DIGIT.get(value);
      }
    }

    public static void main(String... args) throws Exception {
      long total = 0;

      Pattern firstDigitPattern = Pattern.compile("^[a-z]*?(one|two|three|four|five|six|seven|eight|nine|\\d)");

      // This pattern has to account for strings like: (:facepalm:)
      // oneight
      // twone
      // threeight
      // fiveight
      // sevenine
      // eighthree,eightwo
      // nineight
      Pattern lastDigitPattern = Pattern.compile("(one(?!ight)|two(?!ne)|three(?!ight)|four|five(?!ight)|six|seven(?!ine)|eight(?!(hree|wo))|nine(?!ight)|\\d)(?![a-z]*?(one|two|three|four|five|six|seven|eight|nine))[a-z]*?$");
      for (String line : Utility.readAllLines("_2023/puzzle/_01/input.txt")) {
        Matcher firstDigitMatcher = firstDigitPattern.matcher(line);
        if (!firstDigitMatcher.find()) {
          throw new IllegalStateException("Couldn't find match in line: " + line);
        }
        String firstDigitMatch = firstDigitMatcher.group(1);
        Digit firstDigit = Digit.byNameOrValue(firstDigitMatch);

        Matcher lastDigitMatcher = lastDigitPattern.matcher(line);
        if (!lastDigitMatcher.find()) {
          throw new IllegalStateException("Couldn't find match in line: " + line);
        }
        String lastDigitMatch = lastDigitMatcher.group(1);
        Digit lastDigit = Digit.byNameOrValue(lastDigitMatch);

        List<String> digits = getAllDigits(line);

        System.out.printf("first: %s; actual first: %s; last: %s; actual last: %s; combined: %s%s (%d%d); line: %s\n", firstDigitMatch, digits.get(0), lastDigitMatch, digits.get(digits.size() - 1), firstDigitMatch, lastDigitMatch, firstDigit.getValue(), lastDigit.getValue(), line);
        total += Integer.parseInt(String.format("%d%d", firstDigit.getValue(), lastDigit.getValue()));
      }

      System.out.printf("\n\nTotal: %d\n", total);
    }

    private static List<String> getAllDigits(String value) {
      Pattern allDigitsPattern = Pattern.compile("(one|two|three|four|five|six|seven|eight|nine|\\d)");
      Matcher allDigitsMatcher = allDigitsPattern.matcher(value);
      List<String> digits = Lists.newArrayList();
      while (allDigitsMatcher.find()) {
        digits.add(allDigitsMatcher.group(1));
      }

      return digits;
    }
  }
}
