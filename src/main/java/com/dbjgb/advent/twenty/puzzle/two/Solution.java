package com.dbjgb.advent.twenty.puzzle.two;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern PASSWORD_ENTRY_PATTERN =
      Pattern.compile("(\\d{1,})-(\\d{1,}) ([a-z]): (.+)");

  public static void main(String... args) throws Exception {
    printTotalValidWithOriginalPasswordRequirements();
    printTotalValidWithPositionalPasswordRequirements();
  }

  private static void printTotalValidWithOriginalPasswordRequirements() throws IOException {
    int totalValid = 0;

    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/two/input.txt")) {
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher passwordEntryMatcher = PASSWORD_ENTRY_PATTERN.matcher(line);
        if (passwordEntryMatcher.matches()) {
          Pattern validPasswordCharacterPattern =
              Pattern.compile(String.format("[^%s]", passwordEntryMatcher.group(3)));
          Matcher validPasswordCharacterMatcher =
              validPasswordCharacterPattern.matcher(passwordEntryMatcher.group(4));
          String remainingCharacters = validPasswordCharacterMatcher.replaceAll("");

          int minimum = Integer.valueOf(passwordEntryMatcher.group(1));
          int maximum = Integer.valueOf(passwordEntryMatcher.group(2));
          if (minimum <= remainingCharacters.length() && remainingCharacters.length() <= maximum) {
            totalValid += 1;
          }
        }
      }
    }

    System.out.printf("Total Valid (length): %d\n", totalValid);
  }

  private static void printTotalValidWithPositionalPasswordRequirements() throws IOException {
    int totalValid = 0;

    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/two/input.txt")) {
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher passwordEntryMatcher = PASSWORD_ENTRY_PATTERN.matcher(line);
        if (passwordEntryMatcher.matches()) {
          int firstPosition = Integer.valueOf(passwordEntryMatcher.group(1));
          int secondPosition = Integer.valueOf(passwordEntryMatcher.group(2));
          char expectedCharacter = passwordEntryMatcher.group(3).charAt(0);
          String password = passwordEntryMatcher.group(4);

          if (password.charAt(firstPosition - 1) == expectedCharacter
              ^ password.charAt(secondPosition - 1) == expectedCharacter) {
            totalValid += 1;
          }
        }
      }
    }

    System.out.printf("Total Valid (position): %d\n", totalValid);
  }
}
