package com.dbjgb.advent._2020.puzzle._01;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.dbjgb.advent.Utility.openInputFile;

public class Solution {

  private static final Integer MAGIC_VALUE = 2020;

  public static void main(String... args) throws Exception {
    SortedSet<Integer> integers = parseInputFile();

    int[] twoNumbers = findTwoNumbersEqualToOutOf(MAGIC_VALUE, integers);
    System.out.printf(
        "2020 - %d = %d; %d * %d = %d\n",
        twoNumbers[0], twoNumbers[1], twoNumbers[0], twoNumbers[1], twoNumbers[0] * twoNumbers[1]);

    int[] threeNumbers = findThreeNumbersEqualToOutOf(MAGIC_VALUE, integers);
    System.out.printf(
        "%d * %d * %d = %d\n",
        threeNumbers[0],
        threeNumbers[1],
        threeNumbers[2],
        threeNumbers[0] * threeNumbers[1] * threeNumbers[2]);

    System.out.println("done.");
  }

  private static int[] findTwoNumbersEqualToOutOf(int total, SortedSet<Integer> integers) {
    Optional<Integer> difference =
        integers.stream()
            .filter(currentInteger -> integers.contains(total - currentInteger))
            .findFirst();

    return difference
        .map(differenceInteger -> new int[] {total - differenceInteger, differenceInteger})
        .orElse(new int[0]);
  }

  private static int[] findThreeNumbersEqualToOutOf(int total, SortedSet<Integer> integers) {
    SortedSet<Integer> reverseSortedIntegers = new TreeSet<>(Comparator.reverseOrder());
    reverseSortedIntegers.addAll(integers);

    Optional<Integer> topInteger =
        reverseSortedIntegers.stream()
            .filter(
                currentInteger ->
                    findTwoNumbersEqualToOutOf(total - currentInteger, integers).length == 2)
            .findFirst();
    return topInteger
        .map(
            largestInteger -> {
              int[] lowerTwoIntegers = findTwoNumbersEqualToOutOf(total - largestInteger, integers);
              if (lowerTwoIntegers.length == 2) {
                return new int[] {largestInteger, lowerTwoIntegers[0], lowerTwoIntegers[1]};
              }

              return lowerTwoIntegers;
            })
        .orElse(new int[0]);
  }

  private static SortedSet<Integer> parseInputFile() throws IOException {
    SortedSet<Integer> integers = new TreeSet<>();

    try (BufferedReader inputReader = openInputFile("_2020/puzzle/_01/input.txt")) {
      String line;
      while ((line = inputReader.readLine()) != null) {
        integers.add(Integer.valueOf(line));
      }
    }

    return integers;
  }
}
