package com.dbjgb.advent.twenty.twenty.puzzle.ten;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Solution {

  private static final Comparator<JoltageAdapter> JOLTAGE_ADAPTER_COMPARATOR =
      Comparator.comparingInt(JoltageAdapter::getJoltageRating);

  public static void main(String... args) throws Exception {
    printOneJoltAndThreeJoltDifferencesProduct();
    printTotalPathsAvailable();
  }

  private static void printTotalPathsAvailable() throws IOException {
    SortedSet<Integer> joltageRating = parseJoltageRatings();
    SortedSet<JoltageAdapter> adapters = new TreeSet<>(JOLTAGE_ADAPTER_COMPARATOR);
    adapters.addAll(
        joltageRating.stream()
            .map(joltage -> new JoltageAdapter(joltage, adapters))
            .collect(Collectors.toSet()));

    JoltageAdapter base = new JoltageAdapter(0, adapters);
    System.out.printf("Total Available Paths:  %d\n", base.getAvailablePaths());
  }

  private static void printOneJoltAndThreeJoltDifferencesProduct() throws IOException {
    SortedSet<Integer> joltageRatings = parseJoltageRatings();
    System.out.println(joltageRatings);

    int previousJoltage = 0;
    int oneJoltDifferences = 0;
    int threeJoltDifferences = 0;
    for (Integer joltage : joltageRatings) {
      int difference = joltage - previousJoltage;
      previousJoltage = joltage;

      if (difference == 1) {
        oneJoltDifferences += 1;
      } else if (difference == 3) {
        threeJoltDifferences += 1;
      }
    }

    System.out.printf(
        "1j differences * 3j differences = %d\n", oneJoltDifferences * threeJoltDifferences);
  }

  private static SortedSet<Integer> parseJoltageRatings() throws IOException {
    SortedSet<Integer> joltageRatings = new TreeSet<>();

    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/ten/input.txt")) {
      String line;
      while ((line = inputReader.readLine()) != null) {
        joltageRatings.add(Integer.parseInt(line));
      }
    }
    joltageRatings.add(joltageRatings.last() + 3);

    return joltageRatings;
  }

  private static class JoltageAdapter {

    private final int joltageRating;
    private final SortedSet<JoltageAdapter> otherJoltageRatings;

    private long availablePaths = -1;

    public JoltageAdapter(int joltageRating, SortedSet<JoltageAdapter> otherJoltageRatings) {
      this.joltageRating = joltageRating;
      this.otherJoltageRatings = otherJoltageRatings;
    }

    public JoltageAdapter(int joltageRating) {
      this(joltageRating, new TreeSet<>(JOLTAGE_ADAPTER_COMPARATOR));
    }

    public int getJoltageRating() {
      return joltageRating;
    }

    private long getAvailablePaths() {
      if (availablePaths == -1) {
        Set<JoltageAdapter> acceptableAdapters =
            otherJoltageRatings.subSet(
                new JoltageAdapter(joltageRating + 1), new JoltageAdapter(joltageRating + 4));

        availablePaths = 0;
        for (JoltageAdapter adapter : acceptableAdapters) {
          availablePaths += adapter.getAvailablePaths();
        }
        availablePaths = Long.max(availablePaths, acceptableAdapters.size());
      }

      return availablePaths;
    }
  }
}