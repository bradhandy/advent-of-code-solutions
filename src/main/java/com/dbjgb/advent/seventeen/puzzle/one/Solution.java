package com.dbjgb.advent.seventeen.puzzle.one;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;

public class Solution {

  public static void main(String... args) throws Exception {
    printSumOfNumbersMatchingNextNumber();
    printSumOfNumbersMatchingOpposingNumber();
  }

  private static void printSumOfNumbersMatchingNextNumber() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("seventeen/puzzle/one/input.txt")) {
      String numbers = inputReader.readLine();
      int sum = 0;

      if (numbers.charAt(0) == numbers.charAt(numbers.length() - 1)) {
        sum += Integer.parseInt(numbers.substring(numbers.length() - 1));
      }

      for (int i = 0; i < numbers.length() - 1; i++) {
        sum +=
            (numbers.charAt(i) == numbers.charAt(i + 1))
                ? Integer.parseInt(numbers.substring(i, i + 1))
                : 0;
      }

      System.out.printf("%d\n", sum);
    }
  }

  private static void printSumOfNumbersMatchingOpposingNumber() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("seventeen/puzzle/one/input.txt")) {
      String numbers = inputReader.readLine();
      int sum = 0;

      for (int currentCharacter = 0; currentCharacter < numbers.length() - 1; currentCharacter++) {
        int matchingCharacterPosition =
            (currentCharacter + (numbers.length() / 2)) % numbers.length();
        sum +=
            (numbers.charAt(currentCharacter) == numbers.charAt(matchingCharacterPosition))
                ? Integer.parseInt(numbers.substring(currentCharacter, currentCharacter + 1))
                : 0;
      }

      System.out.printf("%d\n", sum);
    }
  }
}
