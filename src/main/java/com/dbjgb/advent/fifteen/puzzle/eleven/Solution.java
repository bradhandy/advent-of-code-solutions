package com.dbjgb.advent.fifteen.puzzle.eleven;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern ILLEGAL_PASSWORD_CHARACTER_PATTERN = Pattern.compile("[iol]");

  public static void main(String... args) {
    String password = "hxbxwxba";
    String nextPassword = createNextPassword(password);
    String secondNextPassword = createNextPassword(nextPassword);

    System.out.println(nextPassword);
    System.out.println(secondNextPassword);
  }

  private static String createNextPassword(String password) {
    String nextPassword = password;
    do {
      nextPassword = increment(nextPassword);
    } while (!meetsRequirements(nextPassword));

    return nextPassword;
  }

  private static String increment(String password) {
    char[] passwordChars = password.toCharArray();
    int currentCharPosition = passwordChars.length - 1;
    do {
      passwordChars[currentCharPosition] = (char) ('a' + (((passwordChars[currentCharPosition] + 1) - 'a') % ('z' - 'a' + 1)));
    } while (passwordChars[currentCharPosition--] == 'a');

    return new String(passwordChars);
  }

  private static boolean meetsRequirements(String password) {
    if (password.length() != 8 || ILLEGAL_PASSWORD_CHARACTER_PATTERN.matcher(password).find()) {
      return false;
    }

    return hasEscalatingSequence(password) && hasTwoDifferentNonOverlappingPairs(password);
  }

  private static boolean hasTwoDifferentNonOverlappingPairs(String password) {
    char[] passwordChars = password.toCharArray();
    Set<String> sequences = new HashSet<>();
    for (int i = 0; i < (password.length() - 1); i++) {
      if (passwordChars[i] == passwordChars[i + 1]) {
        sequences.add(new String(Arrays.copyOfRange(passwordChars, i, i + 2)));
        i += 1;
      }
    }

    return sequences.size() > 1;
  }

  private static boolean hasEscalatingSequence(String password) {
    char[] passwordChars = password.toCharArray();
    for (int i = 0; i < (password.length() - 2); i++) {
      char current = passwordChars[i];
      char onDeck = passwordChars[i + 1];
      char inTheHole = passwordChars[i + 2];

      // cannot have an increasing sequence when the second and third letters are the first and
      // second of the alphabet.
      if (onDeck == 'a' || inTheHole == 'b') {
        continue;
      }

      if (current == (onDeck - 1) && onDeck == (inTheHole - 1)) {
        return true;
      }
    }

    return false;
  }
}
