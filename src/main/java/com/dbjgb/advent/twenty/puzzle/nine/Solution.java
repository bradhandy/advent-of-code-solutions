package com.dbjgb.advent.twenty.puzzle.nine;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

public class Solution {

  public static void main(String... args) throws Exception {
    printFirstInvalidNumber();
    findEncryptionWeakness(1309761972L);
  }

  private static void printFirstInvalidNumber() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/nine/input.txt")) {
      SortedSet<Long> sortedAvailableIntegers = new TreeSet<>();
      Queue<Long> longQueue = new LinkedList<>();

      String line;
      while ((line = inputReader.readLine()) != null) {
        long currentValue = Long.parseLong(line);
        if (sortedAvailableIntegers.size() == 25) {
          boolean valid = false;
          for (Long availableLong : sortedAvailableIntegers) {
            if (sortedAvailableIntegers.contains(currentValue - availableLong)) {
              valid = true;
              break;
            }
          }

          if (!valid) {
            System.out.printf("Invalid value: %d\n", currentValue);
          }
        }

        longQueue.add(currentValue);
        sortedAvailableIntegers.add(currentValue);
        if (sortedAvailableIntegers.size() > 25) {
          sortedAvailableIntegers.remove(longQueue.remove());
        }
      }
    }
  }

  private static void findEncryptionWeakness(long firstInvalidValue) throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/nine/input.txt")) {
      Queue<Long> parsedNumbers = new LinkedList<>();
      long currentTotal = 0;
      String line;
      while ((line = inputReader.readLine()) != null) {
        long currentValue = Long.parseLong(line);
        currentTotal += currentValue;
        parsedNumbers.add(currentValue);

        while (currentTotal > firstInvalidValue) {
          currentTotal -= parsedNumbers.remove();
        }

        if (currentTotal == firstInvalidValue) {
          SortedSet<Long> sortedNumbers = new TreeSet<>(parsedNumbers);
          System.out.printf("Encryption weakness is %d.\n", sortedNumbers.first() + sortedNumbers.last());
          break;
        }
      }
    }
  }
}
