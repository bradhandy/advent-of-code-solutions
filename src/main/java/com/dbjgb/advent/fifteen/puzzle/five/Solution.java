package com.dbjgb.advent.fifteen.puzzle.five;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern DISALLOWED_SEQUENCES_PATTERN = Pattern.compile("(ab|cd|pq|xy)");
  private static final Pattern DOUBLE_LETTER_PATTERN = Pattern.compile("([a-z])\\1");
  private static final Pattern VOWEL_PATTERN = Pattern.compile("[aeiou]");
  private static final Pattern DOUBLE_TWO_CHARACTER_PATTERN = Pattern.compile("([a-z]{2}).*?\\1");
  private static final Pattern SEPARATED_REPEAT_PATTERN = Pattern.compile("([a-z]).\\1");

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/five/input.txt")) {
      int totalOriginalNiceStrings = 0;
      int totalNewNiceStrings = 0;
      String line;
      while ((line = inputReader.readLine()) != null) {
        if (!hasDisallowedSequences(line) && hasDoubleLetter(line) && hasThreeVowels(line)) {
          totalOriginalNiceStrings += 1;
        }
        if (hasDoubleCharacterSequenceRepeat(line) && hasSeparatedCharacterRepeat(line)) {
          totalNewNiceStrings += 1;
        }
      }

      System.out.printf("Total Original Nice Strings: %d\n", totalOriginalNiceStrings);
      System.out.printf("Total New Nice Strings: %d\n", totalNewNiceStrings);
    }
  }

  private static boolean hasDisallowedSequences(String value) {
    return DISALLOWED_SEQUENCES_PATTERN.matcher(value).find();
  }

  private static boolean hasDoubleLetter(String value) {
    return DOUBLE_LETTER_PATTERN.matcher(value).find();
  }

  private static boolean hasThreeVowels(String value) {
    Matcher vowelMatcher = VOWEL_PATTERN.matcher(value);
    int count = 0;

    while (count < 3 && vowelMatcher.find()) {
      count += 1;
    }

    return count == 3;
  }

  private static boolean hasDoubleCharacterSequenceRepeat(String value) {
    return DOUBLE_TWO_CHARACTER_PATTERN.matcher(value).find();
  }

  private static boolean hasSeparatedCharacterRepeat(String value) {
    return SEPARATED_REPEAT_PATTERN.matcher(value).find();
  }
}
