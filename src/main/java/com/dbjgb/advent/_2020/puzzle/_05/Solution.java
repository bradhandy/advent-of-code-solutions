package com.dbjgb.advent._2020.puzzle._05;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Solution {

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("_2020/puzzle/_05/input.txt")) {
      Set<Integer> candidates = new TreeSet<>();
      Set<Integer> existingIds = new TreeSet<>();
      int maxId = Integer.MIN_VALUE;
      String line;
      while ((line = inputReader.readLine()) != null) {
        int row = findEndpoint(line.substring(0, line.length() - 3), 128);
        int column = findEndpoint(line.substring(line.length() - 3), 8);

        int currentId = row * 8 + column;
        maxId = Math.max(currentId, maxId);

        existingIds.add(currentId);
        candidates.addAll(List.of(currentId - 1, currentId + 1));
        candidates.removeAll(existingIds);
      }

      System.out.printf("Maximum ID: %d\n", maxId);
      System.out.printf("My seat is %d.\n", List.copyOf(candidates).get(1));
    }
  }

  private static int findEndpoint(String sequence, int availableNumbers) {
    int lower = 0;
    int upper = availableNumbers - 1;
    int remainingNumbers = availableNumbers;

    for (int i = 0; i < sequence.length(); i++) {
      Half half = Half.forCode(sequence.charAt(i));
      remainingNumbers /= 2;

      if (half == Half.UPPER) {
        lower = lower + remainingNumbers;
      } else {
        upper = upper - remainingNumbers;
      }
    }

    return lower;
  }

  private enum Half {
    UPPER,
    LOWER;

    public static Half forCode(char code) {
      return (code == 'B' || code == 'R') ? UPPER : LOWER;
    }
  }
}
