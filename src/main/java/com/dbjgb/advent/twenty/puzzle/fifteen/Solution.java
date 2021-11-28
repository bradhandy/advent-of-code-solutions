package com.dbjgb.advent.twenty.puzzle.fifteen;

import java.util.LinkedHashMap;
import java.util.Map;

public class Solution {

  private static final LinkedHashMap<Integer, Integer> NUMBER_MAP =
      new LinkedHashMap<>(Map.of(12, 1, 20, 2, 0, 3, 6, 4, 1, 5, 17, 6, 7, 7));

  public static void main(String... args) {
    int nextNumber = 0;
    for (int i = NUMBER_MAP.size() + 1; i < 30_000_000L; i++) {
      int currentNumber = nextNumber;
      nextNumber = i - NUMBER_MAP.getOrDefault(nextNumber, i);
      NUMBER_MAP.put(currentNumber, i);
      if (i == 2019) {
        System.out.printf("The 2020th number is %d.\n", nextNumber);
      }
    }

    System.out.printf("The 30,000,000th number is %d.\n", nextNumber);
  }

}
