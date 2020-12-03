package com.dbjgb.advent.twenty.seventeen.puzzle.two;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");

  public static void main(String... args) throws Exception {
    printSpreadsheetChecksum();
    printSpreadsheetChecksumOfEvenlyDivisibleNumber();
  }

  private static void printSpreadsheetChecksum() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("seventeen/puzzle/two/input.txt")) {
      String line;
      int sum = 0;

      while ((line = inputReader.readLine()) != null) {
        Matcher numberMatcher = NUMBER_PATTERN.matcher(line);
        int maximumNumber = Integer.MIN_VALUE;
        int minimumNumber = Integer.MAX_VALUE;

        while (numberMatcher.find()) {
          int currentNumber = Integer.parseInt(numberMatcher.group(1));
          maximumNumber = Math.max(maximumNumber, currentNumber);
          minimumNumber = Math.min(minimumNumber, currentNumber);
        }

        sum += (maximumNumber - minimumNumber);

        System.out.printf(
            "max = %d, min = %d, difference = %d\n",
            maximumNumber, minimumNumber, maximumNumber - minimumNumber);
      }

      System.out.printf("Checksum: %d\n", sum);
    }
  }

  private static void printSpreadsheetChecksumOfEvenlyDivisibleNumber() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("seventeen/puzzle/two/input.txt")) {
      String line;
      final AtomicInteger sum = new AtomicInteger(0);

      while ((line = inputReader.readLine()) != null) {
        Matcher numberMatcher = NUMBER_PATTERN.matcher(line);
        Set<Integer> integers = new HashSet<>();
        while (numberMatcher.find()) {
          int currentNumber = Integer.parseInt(numberMatcher.group(1));
          Optional<Integer> divisibleComplement =
              integers.stream()
                  .filter(
                      existingNumber ->
                          (existingNumber > currentNumber)
                              ? existingNumber % currentNumber == 0
                              : currentNumber % existingNumber == 0)
                  .findFirst();

          divisibleComplement
              .map(
                  number ->
                      (number > currentNumber) ? number / currentNumber : currentNumber / number)
              .ifPresentOrElse(
                  lineChecksum -> sum.set(sum.get() + lineChecksum),
                  () -> integers.add(currentNumber));
        }
      }

      System.out.printf("Checksum: %d\n", sum.get());
    }
  }
}
