package com.dbjgb.advent.twenty.eighteen.puzzle.two;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Solution {

  public static void main(String... args) throws Exception {
    printChecksum();
    printContainerIdsWithNumberOfDifferences(1);
  }

  private static void printChecksum() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("eighteen/puzzle/two/input.txt")) {
      String line;
      int twoCharacterWords = 0;
      int threeCharacterWords = 0;
      while ((line = inputReader.readLine()) != null) {
        Map<Character, Integer> characterCount = new HashMap<>();
        for (int i = 0; i < line.length(); i++) {
          char currentCharacter = line.charAt(i);
          int currentCount = Optional.ofNullable(characterCount.get(currentCharacter)).orElse(0);
          characterCount.put(currentCharacter, currentCount + 1);
        }

        if (characterCount.values().stream().anyMatch(count -> count == 2)) {
          twoCharacterWords += 1;
        }
        if (characterCount.values().stream().anyMatch(count -> count == 3)) {
          threeCharacterWords += 1;
        }
      }

      System.out.printf(
          "Two Character Words: %d; Three Character Words: %d; Checksum: %d\n",
          twoCharacterWords, threeCharacterWords, twoCharacterWords * threeCharacterWords);
    }
  }

  private static void printContainerIdsWithNumberOfDifferences(final int distance)
      throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("eighteen/puzzle/two/input.txt")) {
      Set<String> identifiers = new HashSet<>();
      String line;
      while ((line = inputReader.readLine()) != null) {
        final String searchCode = line;
        Optional<String> correspondingIdentifier =
            identifiers.stream()
                .filter(identifier -> hammingDistance(searchCode, identifier, distance) == distance)
                .findAny();
        correspondingIdentifier.ifPresentOrElse(
            identifier -> {
              System.out.printf("IDs: %s, %s; Common Characters:  ", identifier, searchCode);
              for (int i = 0; i < identifier.length(); i++) {
                if (identifier.charAt(i) == searchCode.charAt(i)) {
                  System.out.print(identifier.charAt(i));
                }
              }
              System.out.println();
            },
            () -> identifiers.add(searchCode));
      }
    }
  }

  private static int hammingDistance(String left, String right, int maxDistance) {
    int distance = 0;
    for (int i = 0; i < left.length() && distance <= maxDistance; i++) {
      if (left.charAt(i) != right.charAt(i)) {
        distance += 1;
      }
    }

    return distance;
  }
}
