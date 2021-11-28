package com.dbjgb.advent.fifteen.puzzle.eight;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern HEX_ESCAPE_PATTERN = Pattern.compile("\\\\x[0-9a-f]{2}");
  private static final Pattern ESCAPED_CHARACTER = Pattern.compile("\\\\[\"\\\\]");
  private static final Pattern BACKSLASH_CHARACTER = Pattern.compile("\\\\");
  private static final Pattern QUOTED_CHARACTER = Pattern.compile("\"");

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/eight/input.txt")) {
      int rawNumberOfCharacters = 0;
      int numberUnescapedOfCharacters = 0;
      int numberOfOverEscapedCharacter = 0;

      String line;
      while ((line = inputReader.readLine()) != null) {
        rawNumberOfCharacters += line.length();
        numberUnescapedOfCharacters += countUnescapedCharacters(line);
        numberOfOverEscapedCharacter += countEscapedCharacters(line);
      }

      System.out.printf(
          "Difference in characters:  %d\n", rawNumberOfCharacters - numberUnescapedOfCharacters);
      System.out.printf(
          "Difference in characters:  %d\n", numberOfOverEscapedCharacter - rawNumberOfCharacters);
    }
  }

  private static int countUnescapedCharacters(String line) {
    Matcher escapedCharacterMatcher = ESCAPED_CHARACTER.matcher(line);
    Matcher hexMatcher = HEX_ESCAPE_PATTERN.matcher(escapedCharacterMatcher.replaceAll("-"));

    String unescapedQuotedString = hexMatcher.replaceAll("-");

    // account for removing the two double quotes surrounding the string without actually removing
    // them.
    return unescapedQuotedString.length() - 2;
  }

  private static int countEscapedCharacters(String line) {
    Matcher backslashMatcher = BACKSLASH_CHARACTER.matcher(line);
    Matcher quoteMatcher = QUOTED_CHARACTER.matcher(backslashMatcher.replaceAll("\\\\\\\\"));

    String escapedString = quoteMatcher.replaceAll("\\\\\"");

    // account for adding the two double quotes surrounding the string without actually adding them.
    return escapedString.length() + 2;
  }
}
