package com.dbjgb.advent._2022.puzzle._15;

import com.dbjgb.advent.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern SENSOR_AND_BEACON_PATTERN =
      Pattern.compile(
          "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");

  public static void main(String... main) throws Exception {
    List<String> sensorInformationListing = Utility.readAllLines("_2022/puzzle/_15/input.txt");
    List<SensorCoverage> sensorCoverages = new ArrayList<>();
    Set<Cell> cellsCovered = new HashSet<>();

    for (String sensorInformation : sensorInformationListing) {
      Matcher sensorInformationMatcher = SENSOR_AND_BEACON_PATTERN.matcher(sensorInformation);
      if (!sensorInformationMatcher.matches()) {
        throw new IllegalStateException(
            String.format("Sensor information didn't match pattern: %s\n", sensorInformation));
      }

      SensorCoverage sensorCoverage =
          new SensorCoverage(
              Integer.parseInt(sensorInformationMatcher.group(2)),
              Integer.parseInt(sensorInformationMatcher.group(1)),
              Integer.parseInt(sensorInformationMatcher.group(4)),
              Integer.parseInt(sensorInformationMatcher.group(3)));

      sensorCoverages.add(sensorCoverage);
      cellsCovered.addAll(
          sensorCoverage.locationsCoveredInGrid(2_000_000, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    for (SensorCoverage coverage : sensorCoverages) {
      cellsCovered.remove(coverage.getBeacon());
    }

    System.out.printf("Cells covered on row 2000000: %d\n", cellsCovered.size());
    cellsCovered.clear();

    for (int y = 0; y <= 4_000_000; y++) {
      List<Range> coveredInRow = coveredInRowInGrid(sensorCoverages, y, 0, 4_000_000);
      if (coveredInRow.size() > 1) {
        long uncoveredColumn = coveredInRow.stream().map(Range::getEnd).reduce(Integer.MAX_VALUE, Math::min) + 1;
        System.out.printf("Uncovered at %d x %d: %d\n", uncoveredColumn, y, uncoveredColumn * 4_000_000 + y);
      }
    }
  }

  private static List<Range> coveredInRowInGrid(
      List<SensorCoverage> coverages, int row, int origin, int rightCorner) {
    List<Range> covered = new ArrayList<>();
    for (SensorCoverage coverage : coverages) {
      Range range = coverage.rangeCoveredInRowInGrid(row, origin, rightCorner);
      if (range != null) {
        covered.add(range);
      }
    }

    covered.sort(Comparator.comparing(Range::getStart));

    List<Range> mergedRanges = new ArrayList<>();
    for (int i = 0; i < covered.size();) {
      Range candidateRange = covered.get(i);
      int j = 1;
      while (j < covered.size() && candidateRange.overlaps(covered.get(j))) {
        candidateRange = candidateRange.merge(covered.remove(j));
      }

      mergedRanges.add(candidateRange);
      i = j;
    }
    covered.clear();

    return mergedRanges;
  }
}
