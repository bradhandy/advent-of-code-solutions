package com.dbjgb.advent._2024.puzzle._08;

import com.dbjgb.advent.Utility;
import com.google.common.base.Joiner;
import com.google.common.collect.TreeMultimap;

import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  public static void main(String... args) throws Exception {
    solvePartOne();
    solvePartTwo();
  }

  private static void solvePartOne() throws Exception {
    AntennaGrid grid = new AntennaGrid();
    Set<Point> antinodes = grid.calculatePartOneAntiNodePositions();
    System.out.println("Part One: " + antinodes.size());
  }

  private static void solvePartTwo() throws Exception {
    AntennaGrid grid = new AntennaGrid();
    Set<Point> antinodes = grid.calculatePartTwoAntiNodePositions();
    System.out.println("Part Two: " + antinodes.size());
  }

  private static class AntennaGrid {

    private static final Pattern FREQUENCY_PATTERN = Pattern.compile("([^.])");

    private final int width;
    private final int height;
    private TreeMultimap<String, Point> antennaMap;

    public AntennaGrid() throws Exception {
      List<String> lines = Utility.readAllLines("_2024/puzzle/_08/input.txt");
      width = lines.get(0).length();
      height = lines.size();

      antennaMap = TreeMultimap.create(Comparator.naturalOrder(), Point.ORDER_BY_X);
      int lineNumber = 0;
      for (String line : lines) {
        Matcher frequencyMatcher = FREQUENCY_PATTERN.matcher(line);
        while (frequencyMatcher.find()) {
          antennaMap.put(frequencyMatcher.group(1), new Point(frequencyMatcher.toMatchResult().start(1), lineNumber));
        }
        lineNumber += 1;
      }
    }

    public Set<Point> calculatePartOneAntiNodePositions() {
      Set<Point> antiNodes = new TreeSet<>(Point.ORDER_BY_X);
      for (String frequency : antennaMap.keySet()) {
        for (Point point : antennaMap.get(frequency)) {
          NavigableSet<Point> sortedPoints = antennaMap.get(frequency);
          Set<Point> remainingAntennna = sortedPoints.subSet(point, false, sortedPoints.last(), true);
          for (Point inlinePoint : remainingAntennna) {
            int deltaX = point.getX() - inlinePoint.getX();
            int deltaY = point.getY() - inlinePoint.getY();
            Point left = new Point(point.getX() + deltaX, point.getY() + deltaY);
            Point right = new Point(inlinePoint.getX() - deltaX, inlinePoint.getY() + (deltaY * -1));
            if (isInGrid(left)) {
              antiNodes.add(left);
            }
            if (isInGrid(right)) {
              antiNodes.add(right);
            }
          }
        }
      }
      return antiNodes;
    }

    public Set<Point> calculatePartTwoAntiNodePositions() {
      Set<Point> antiNodes = new TreeSet<>(Point.ORDER_BY_X);
      for (String frequency : antennaMap.keySet()) {
        for (Point point : antennaMap.get(frequency)) {
          NavigableSet<Point> sortedPoints = antennaMap.get(frequency);
          Set<Point> remainingAntennna = sortedPoints.subSet(point, false, sortedPoints.last(), true);
          for (Point inlinePoint : remainingAntennna) {
            int deltaX = point.getX() - inlinePoint.getX();
            int deltaY = point.getY() - inlinePoint.getY();

            antiNodes.add(point);
            antiNodes.add(inlinePoint);
            Point left = new Point(point.getX() + deltaX, point.getY() + deltaY);
            if (isInGrid(left)) {
              do {
                antiNodes.add(left);
                left = new Point(left.getX() + deltaX, left.getY() + deltaY);
              } while (isInGrid(left));
            }
            Point right = new Point(inlinePoint.getX() - deltaX, inlinePoint.getY() + (deltaY * -1));
            if (isInGrid(right)) {
              do {
                antiNodes.add(right);
                right = new Point(right.getX() - deltaX, right.getY() + (deltaY * -1));
              } while (isInGrid(right));
            }
          }
        }
      }
      return antiNodes;
    }

    private boolean isInGrid(Point point) {
      return 0 <= point.getX() && point.getX() < width && 0 <= point.getY() && point.getY() < height;
    }
  }

  private static class Point {

    public static final Comparator<Point> ORDER_BY_X =
        Comparator.comparing(Point::getX).thenComparing(Point::getY);

    private final int x;
    private final int y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;
      Point point = (Point) o;
      return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }
  }
}
