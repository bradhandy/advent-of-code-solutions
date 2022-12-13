package com.dbjgb.advent._2021.puzzle._05;

import com.dbjgb.advent.Utility;
import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Solution {

  private static final Pattern COORDINATE_PATTERN = Pattern.compile("(\\d+),(\\d+)");

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("_2021/puzzle/_05/input.txt")) {
      Multimap<Point, Integer> pointsEncountered = ArrayListMultimap.create();
      Set<Point> multiplePoints = new HashSet<>();
      List<Line> lines = new ArrayList<>();

      String inputLine;
      while ((inputLine = inputReader.readLine()) != null) {
        List<Point> coordinates =
            COORDINATE_PATTERN
                .matcher(inputLine)
                .results()
                .map(
                    result ->
                        new Point(
                            Integer.parseInt(result.group(1)), Integer.parseInt(result.group(2))))
                .collect(Collectors.toList());

        Line line = new Line(coordinates.get(0), coordinates.get(1));
        lines.add(line);
        line.pointStream()
            .forEach(
                point -> {
                  if (pointsEncountered.containsKey(point)) {
                    multiplePoints.add(point);
                  }
                  pointsEncountered.put(point, 1);
                });
      }

      System.out.printf("The number of points of overlap: %d\n", multiplePoints.size());
    }
  }

  private static class Point {

    private final int x;
    private final int y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Point point = (Point) o;
      return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(x, y);
    }
  }

  private static class Line {

    private final Point start;
    private final Point end;

    public Line(Point start, Point end) {
      this.start = start;
      this.end = end;
    }

    public boolean isHorizontal() {
      return start.y == end.y;
    }

    public boolean isVertical() {
      return start.x == end.x;
    }

    public Stream<Point> pointStream() {
      if (isHorizontal()) {
        int fromX = Math.min(start.x, end.x);
        int toX = Math.max(start.x, end.x);

        return IntStream.rangeClosed(fromX, toX).mapToObj(x -> new Point(x, start.y));
      } else if (isVertical()) {
        int fromY = Math.min(start.y, end.y);
        int toY = Math.max(start.y, end.y);

        return IntStream.rangeClosed(fromY, toY).mapToObj(y -> new Point(start.x, y));
      }

      int rise = Math.abs(start.x - end.x);
      int xDelta = start.x < end.x ? 1 : -1;
      int yDelta = start.y < end.y ? 1 : -1;

      return IntStream.rangeClosed(0, rise)
          .mapToObj(delta -> new Point(start.x + (delta * xDelta), start.y + (delta * yDelta)));
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Line line = (Line) o;
      return Objects.equal(start, line.start) && Objects.equal(end, line.end);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(start, end);
    }
  }
}
