package com.dbjgb.advent.twenty.eighteen.puzzle.one;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Solution {

  public static void main(String... args) throws Exception {
    printFrequency();
    printFirstDuplicateFrequency();
  }

  private static void printFrequency() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("eighteen/puzzle/one/input.txt")) {
      String line;
      int frequency = 0;
      while ((line = inputReader.readLine()) != null) {
        frequency += Integer.parseInt(line);
      }

      System.out.printf("Frequency:  %d\n", frequency);
    }
  }

  private static void printFirstDuplicateFrequency() throws IOException {
    List<Integer> frequencyChanges = loadFrequenceChanges();
    Set<Integer> frequencies = new HashSet<>();

    int frequency = 0;
    boolean foundDuplicate = false;
    while (!foundDuplicate) {
      for (Integer frequencyChange : frequencyChanges) {
        frequency += frequencyChange;
        if (!frequencies.add(frequency)) {
          foundDuplicate = true;
          break;
        }
      }
    }

    System.out.printf("Frequency:  %d\n", frequency);
  }

  private static List<Integer> loadFrequenceChanges() throws IOException {
    List<Integer> frequencyChanges = new ArrayList<>();
    try (BufferedReader inputReader = Utility.openInputFile("eighteen/puzzle/one/input.txt")) {
      String line;
      while ((line = inputReader.readLine()) != null) {
        frequencyChanges.add(Integer.valueOf(line));
      }
    }

    return frequencyChanges;
  }
}
