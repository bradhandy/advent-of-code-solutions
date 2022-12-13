package com.dbjgb.advent._2021.puzzle._06;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Solution {

  public static void main(String... args) throws Exception {
    printTotalLanternFishAfterDays(80);
    printTotalLanternFishAfterDays(256);
  }

  private static void printTotalLanternFishAfterDays(int numberOfDays) throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("_2021/puzzle/_06/input.txt")) {
      String delimitedAges = inputReader.readLine();
      List<Integer> initialLanternFishAges =
          Arrays.stream(delimitedAges.split(","))
              .map(Integer::valueOf)
              .collect(Collectors.toList());

      long[] newFishPerDay = new long[numberOfDays];
      Arrays.fill(newFishPerDay, 0);

      for (int lanternFishAge : initialLanternFishAges) {
        for (int day = lanternFishAge; day <= (numberOfDays - 1); day += 7) {
          newFishPerDay[day]++;
        }
      }

      for (int day = 0; day <= (numberOfDays - 1); day++) {
        for (int newFishBucket = day + 9; newFishBucket <= (numberOfDays - 1); newFishBucket += 7) {
          newFishPerDay[newFishBucket] += newFishPerDay[day];
        }
      }

      System.out.printf(
          "Total new fish: %d\n",
          Arrays.stream(newFishPerDay).reduce(0, Long::sum) + initialLanternFishAges.size());
    }
  }
}
