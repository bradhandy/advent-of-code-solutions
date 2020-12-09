package com.dbjgb.advent.twenty.fifteen.puzzle.fourteen;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern REINDEER_VELOCITY_REST_PERIODS =
      Pattern.compile("([^ ]+).*?(\\d+).*?(\\d+).*?(\\d+).*");

  public static void main(String... args) throws Exception {
    printWinnersDistance();
    printWinningReindeersPoints();
  }

  private static void printWinningReindeersPoints() throws IOException {
    List<Reindeer> reindeer = parseReindeer();
    Map<String, Integer> points = new HashMap<>();

    for (int i = 1; i <= 2503; i++) {
      SortedMap<Integer, List<Reindeer>> distanceByReindeer = new TreeMap<>();
      for (Reindeer raceEntry : reindeer) {
        int distance = raceEntry.distanceInSeconds(i);
        List<Reindeer> reindeerAtDistance =
            distanceByReindeer.getOrDefault(distance, new ArrayList<>());
        reindeerAtDistance.add(raceEntry);
        distanceByReindeer.put(distance, reindeerAtDistance);
      }

      List<Reindeer> furthestReindeer = distanceByReindeer.get(distanceByReindeer.lastKey());
      for (Reindeer pointEarner : furthestReindeer) {
        Integer totalPoints = points.getOrDefault(pointEarner.getName(), 0);
        points.put(pointEarner.getName(), totalPoints + 1);
      }
    }

    SortedSet<Integer> scores = new TreeSet<>(points.values());
    System.out.printf("Most Points by a Reindeer:  %d\n", scores.last());
  }

  private static void printWinnersDistance() throws IOException {
    List<Reindeer> reindeer = parseReindeer();
    int maxDistance = 0;
    for (Reindeer raceEntry : reindeer) {
      maxDistance = Integer.max(maxDistance, raceEntry.distanceInSeconds(2503));
    }

    System.out.printf("The winner went %dkm.\n", maxDistance);
  }

  private static List<Reindeer> parseReindeer() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/fourteen/input.txt")) {
      List<Reindeer> reindeer = new ArrayList<>();
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher reindeerMatcher = REINDEER_VELOCITY_REST_PERIODS.matcher(line);
        if (reindeerMatcher.matches()) {
          reindeer.add(
              new Reindeer(
                  reindeerMatcher.group(1),
                  Integer.parseInt(reindeerMatcher.group(2)),
                  Integer.parseInt(reindeerMatcher.group(3)),
                  Integer.parseInt(reindeerMatcher.group(4))));
        }
      }

      return reindeer;
    }
  }

  private static class Reindeer {

    private final String name;
    private final int velocity;
    private final int activePeriodInSeconds;
    private final int restPeriodInSeconds;

    public Reindeer(String name, int velocity, int activePeriodInSeconds, int restPeriodInSeconds) {
      this.name = name;
      this.velocity = velocity;
      this.activePeriodInSeconds = activePeriodInSeconds;
      this.restPeriodInSeconds = restPeriodInSeconds;
    }

    public String getName() {
      return name;
    }

    public int distanceInSeconds(int seconds) {
      int combinedActiveAndRestInterval = activePeriodInSeconds + restPeriodInSeconds;
      int activeSecondsInFinalStretch =
          Integer.min(activePeriodInSeconds, seconds % combinedActiveAndRestInterval);
      return (velocity * activePeriodInSeconds * (seconds / combinedActiveAndRestInterval))
          + (velocity * activeSecondsInFinalStretch);
    }
  }
}
