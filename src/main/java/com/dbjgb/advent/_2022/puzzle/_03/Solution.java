package com.dbjgb.advent._2022.puzzle._03;

import com.dbjgb.advent.Utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {

  private static final int LOWERCASE_DIFFERENCE = 96;
  private static final int UPPERCASE_DIFFERENCE = 38;

  public static void main(String... args) throws IOException, URISyntaxException {
    List<String> ruckSacks = Utility.readAllLines("_2022/puzzle/_03/input.txt");
    long totalPriority = 0;
    long elfGroupPriority = 0;
    List<char[]> elfGroup = new ArrayList<>();
    for (String ruckSack : ruckSacks) {
      char[] contents = ruckSack.toCharArray();
      int compartmentLength = contents.length / 2;
      Arrays.sort(contents, compartmentLength, contents.length);
      for (int i = 0; i < compartmentLength; i++) {
        if (Arrays.binarySearch(contents, compartmentLength, contents.length, contents[i]) >= 0) {
          totalPriority += calculatePriority(contents[i]);
          break;
        }
      }

      if (elfGroup.size() < 2) {
        Arrays.sort(contents);
        elfGroup.add(contents);
        continue;
      }

      for (int i = 0; i < contents.length; i++) {
        if (Arrays.binarySearch(elfGroup.get(0), contents[i]) >= 0 && Arrays.binarySearch(elfGroup.get(1), contents[i]) >= 0) {
          elfGroupPriority += calculatePriority(contents[i]);
          break;
        }
      }
      elfGroup.clear();
    }

    System.out.printf("Total priority %d.\n", totalPriority);
    System.out.printf("Total Elf Group priority %d.\n", elfGroupPriority);
  }

  private static int calculatePriority(char value) {
    int priority = ((int) value) - LOWERCASE_DIFFERENCE;
    if (priority < 0) {
      priority = ((int) value) - UPPERCASE_DIFFERENCE;
    }

    return priority;
  }
}
