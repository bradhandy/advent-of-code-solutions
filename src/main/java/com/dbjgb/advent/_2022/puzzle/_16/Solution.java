package com.dbjgb.advent._2022.puzzle._16;

import com.dbjgb.advent.Utility;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {

  private static final Pattern VALUE_PATTERN =
      Pattern.compile("Valve ([^ ]+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)");
  private static final Pattern NEIGHBOR_PATTERN = Pattern.compile("([^, ]+)");
  private static final long MAX_MINUTES_SOLO = 30;
  private static final long MAX_MINUTES_WITH_PARTNER = 26;

  public static void main(String... args) throws Exception {
    Map<String, Valve> valves = new HashMap<>();
    Valve first = null;

    List<String> valveConfigurations = Utility.readAllLines("_2022/puzzle/_16/input.txt");
    for (String valveConfiguration : valveConfigurations) {
      Matcher valveMatcher = VALUE_PATTERN.matcher(valveConfiguration);
      if (!valveMatcher.matches()) {
        throw new IllegalStateException(
            String.format("Valve configuration didn't match: %s\n", valveConfiguration));
      }

      Valve valve = new Valve(valveMatcher.group(1), Integer.parseInt(valveMatcher.group(2)));
      first = valve.getName().equals("AA") ? valve : first;
      valves.put(valve.getName(), valve);

      Matcher neighborMatcher = NEIGHBOR_PATTERN.matcher(valveMatcher.group(3));
      while (neighborMatcher.find()) {
        String neighborName = neighborMatcher.group(1);
        if (valves.containsKey(neighborName)) {
          valve.addNeighbor(valves.get(neighborName));
        }
      }
    }

    Set<Valve> relevantValves =
        valves.values().stream()
            .filter(valve -> valve.getFlowRate() > 0)
            .collect(Collectors.toSet());
    Map<Valve, Map<Valve, Integer>> valveHopIndex = new HashMap<>();
    for (Valve valve : valves.values()) {
      Set<Valve> visitedValves = new HashSet<>(Set.of(valve));
      Set<Valve> currentValves = new HashSet<>(valve.getNeighbors());
      int hopCount = 1;
      do {
        for (Valve currentValve : new HashSet<>(currentValves)) {
          Map<Valve, Integer> hopIndex = valveHopIndex.getOrDefault(valve, new HashMap<>());
          if (!valveHopIndex.containsKey(valve)) {
            valveHopIndex.put(valve, hopIndex);
          }

          hopIndex.put(currentValve, hopCount);
          currentValves.addAll(currentValve.getNeighbors());
          visitedValves.add(currentValve);
        }
        hopCount++;
        currentValves.removeAll(visitedValves);
      } while (!currentValves.isEmpty());
    }

    System.out.println("Done indexing.");
    System.out.printf(
        "Pressure Released: %d\n",
        maxPressureReleasedSolo(first, relevantValves, valveHopIndex, 0L, MAX_MINUTES_SOLO));
    System.out.printf(
        "Pressure Released w/ Partner: %d\n",
        maxPressureReleasedWithPartner(first, relevantValves, valveHopIndex, 0L, MAX_MINUTES_WITH_PARTNER));
  }

  private static long maxPressureReleasedSolo(
      Valve current,
      Set<Valve> remainingRelevantValves,
      Map<Valve, Map<Valve, Integer>> valveHopIndex,
      long minutesPassed,
      long maxMinutes) {
    long pressureReleased = 0;
    for (Valve next : remainingRelevantValves) {
      long minutesPassedInSegment = (valveHopIndex.get(current).get(next) + 1);
      long totalMinutesPassed = minutesPassed + minutesPassedInSegment;
      if (totalMinutesPassed > maxMinutes) {
        return pressureReleased;
      }

      long pressureReleasedInSegment =
          ((maxMinutes - totalMinutesPassed) * next.getFlowRate());
      if (remainingRelevantValves.size() == 1) {
        return pressureReleasedInSegment;
      }

      Set<Valve> leftOverValves = new HashSet<>(remainingRelevantValves);
      leftOverValves.remove(next);

      pressureReleased =
          Math.max(
              pressureReleased,
              pressureReleasedInSegment
                  + maxPressureReleasedSolo(
                      next, leftOverValves, valveHopIndex, totalMinutesPassed, maxMinutes));
    }

    return pressureReleased;
  }

  private static long maxPressureReleasedWithPartner(
      Valve current,
      Set<Valve> remainingRelevantValves,
      Map<Valve, Map<Valve, Integer>> valveHopIndex,
      long minutesPassed,
      long maxMinutes) {
    return 0L;
  }

  private static List<Set<Valve>> splitRemainingValves(Set<Valve> remainingValves) {
    Set<Valve> humanValues = new HashSet<>();
    Set<Valve> elephantValues = new HashSet<>();
    Iterator<Valve> remainingValvesIterator = remainingValves.iterator();
    while (remainingValvesIterator.hasNext()) {
      humanValues.add(remainingValvesIterator.next());
      if (remainingValvesIterator.hasNext()) {
        elephantValues.add(remainingValvesIterator.next());
      }
    }

    return List.of(humanValues, elephantValues);
  }
}
