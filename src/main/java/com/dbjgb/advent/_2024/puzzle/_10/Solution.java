package com.dbjgb.advent._2024.puzzle._10;

import com.dbjgb.advent.Direction;
import com.dbjgb.advent.Point;
import com.dbjgb.advent.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Solution {

  public static void main(String... args) throws Exception {
    solvePartOne();
    solvePartTwo();
  }

  private static void solvePartOne() throws Exception {
    Grid grid = new Grid();

    for (Point trailHead : grid.getTrailHeads()) {
      grid.hikeTrailStartingAtContinuingFrom(trailHead, trailHead);
    }
    grid.waitForHikingToComplete();

    System.out.printf("Part One: %d\n", grid.getTrailHeadScores().stream().reduce(0, Integer::sum));
  }

  private static void solvePartTwo() throws Exception {
    Grid grid = new Grid();

    for (Point trailHead : grid.getTrailHeads()) {
      grid.hikeTrailStartingAtContinuingFrom(trailHead, trailHead);
    }
    grid.waitForHikingToComplete();

    System.out.printf("Part Two: %d\n", grid.getTrailHeadRatings().stream().reduce(0, Integer::sum));
  }

  private static class Grid {
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    List<Future<?>> tasks = new ArrayList<>();

    private Map<Point, Set<Point>> trailHeadScore = new HashMap<>();
    private Map<Point, AtomicInteger> trailHeadRating = new HashMap<>();
    private Map<Point, Integer> grid = new HashMap<>();

    public Grid() throws Exception {
      List<String> gridLines = Utility.readAllLines("_2024/puzzle/_10/input.txt");

      int lineNumber = 0;
      for (String gridLine : gridLines) {
        for (int i = 0; i < gridLine.length(); i++) {
          int altitude = gridLine.charAt(i) - '0';
          Point location = new Point(i, lineNumber);
          grid.put(location, altitude);
          if (altitude == 0) {
            trailHeadRating.put(location, new AtomicInteger(0));
            trailHeadScore.put(location, new HashSet<>());
          }
        }
        lineNumber += 1;
      }
    }

    public Set<Point> getTrailHeads() {
      return trailHeadScore.keySet();
    }

    private void hikeTrailStartingAtContinuingFrom(final Point trailHead, final Point currentLocation) {
      if (grid.get(currentLocation) == 9) {
        trailHeadRating.get(trailHead).incrementAndGet();
        Set<Point> trailEnds = trailHeadScore.get(trailHead);
        synchronized (trailEnds) {
          trailEnds.add(currentLocation);
        }
        return;
      }

      final int altitude = grid.get(currentLocation);
      Arrays.stream(Direction.allDirections())
          .map(direction -> direction.forward(currentLocation))
          .map(nextStep -> {
            if (grid.containsKey(nextStep)) {
              int newAltitude = grid.get(nextStep);
              if (newAltitude == altitude + 1) {
                return (Runnable) () -> hikeTrailStartingAtContinuingFrom(trailHead, nextStep);
              }
            }

            return null;
          })
          .filter(Objects::nonNull)
          .map(executorService::submit)
          .forEach(task -> {
            synchronized (tasks) {
              tasks.add(task);
            }
          });
    }

    private void waitForHikingToComplete() throws Exception {
      while (!tasks.isEmpty()) {
        synchronized (tasks) {
          Iterator<Future<?>> taskIterator = tasks.iterator();
          while (taskIterator.hasNext()) {
            if (taskIterator.next().isDone()) {
              taskIterator.remove();
            }
          }
        }
        Thread.sleep(100);
      }
      executorService.shutdown();
    }

    public Collection<Integer> getTrailHeadScores() {
      return trailHeadScore.values().stream().map(Set::size).toList();
    }

    public Collection<Integer> getTrailHeadRatings() {
      return trailHeadRating.values().stream().map(AtomicInteger::get).toList();
    }
  }
}
