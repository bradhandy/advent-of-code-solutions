package com.dbjgb.advent._2016.puzzle._01;

import com.dbjgb.advent.Utility;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("([LR])(\\d+)");

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("_2016/puzzle/_01/input.txt")) {
      String instructions = inputReader.readLine();
      Matcher instructionMatcher = INSTRUCTION_PATTERN.matcher(instructions);

      List<Line2D.Double> path = new ArrayList<>();
      Point location = new Point(0, 0);
      Point intersection = null;
      Compass facing = Compass.NORTH;
      while (instructionMatcher.find()) {
        Walking walking = Walking.fromCode(instructionMatcher.group(1));
        facing = facing.newHeading(walking);

        Point origin = location.getLocation();
        Integer blocks = Integer.valueOf(instructionMatcher.group(2));
        facing.translate(location, blocks);
        // System.out.printf("Walked %s for %d blocks...\n", facing.name(), blocks);

        if (intersection == null) {
          Line2D.Double mostRecentPath = new Line2D.Double(origin, location.getLocation());
          System.out.printf(
              "Path: (%.1f, %.1f) -> (%.1f, %.1f)\n",
              mostRecentPath.getX1(),
              mostRecentPath.getY1(),
              mostRecentPath.getX2(),
              mostRecentPath.getY2());
          final Compass currentCompassHeading = facing;

          // the most recently added path to the collection will end where the current path start.
          // we need to exclude this path by taking a sublist of the collection which excludes the
          // final element.
          intersection =
              path.subList(0, Math.max(0, path.size() - 1)).stream()
                  .filter(previousPathSegment -> mostRecentPath.intersectsLine(previousPathSegment))
                  .map(segment -> currentCompassHeading.intersectionPoint(mostRecentPath, segment))
                  .findFirst()
                  .orElse(null);
          path.add(mostRecentPath);
        }
      }

      System.out.printf("Ending Point: x = %.1f, y = %.1f\n", location.getX(), location.getY());
      System.out.printf(
          "Blocks away from HQ:  %.1f\n", Math.abs(location.getX()) + Math.abs(location.getY()));
      if (intersection != null) {
        System.out.printf(
            "Intersection Point: x = %.1f, y = %.1f\n", intersection.getX(), intersection.getY());
        System.out.printf(
            "First intersection:  %.1f\n",
            Math.abs(intersection.getX()) + Math.abs(intersection.getY()));
      }
    }
  }

  private enum Compass {
    NORTH(0, 1, "WEST", "EAST"),
    SOUTH(0, -1, "EAST", "WEST"),
    EAST(1, 0, "NORTH", "SOUTH"),
    WEST(-1, 0, "SOUTH", "NORTH");

    private final int translateX;
    private final int translateY;
    private final String left;
    private final String right;

    Compass(int translateX, int translateY, String left, String right) {
      this.translateX = translateX;
      this.translateY = translateY;
      this.left = left;
      this.right = right;
    }

    public void translate(Point point, int blocks) {
      point.translate(translateX * blocks, translateY * blocks);
    }

    public Compass newHeading(Walking walking) {
      return Compass.valueOf((walking == Walking.LEFT) ? left : right);
    }

    public Point intersectionPoint(Line2D mostRecentPath, Line2D intersectingPath) {
      System.out.printf(
          "(%.1f, %.1f) -> (%.1f, %.1f); (%.1f, %.1f) -> (%.1f, %.1f)\n",
          mostRecentPath.getX1(),
          mostRecentPath.getY1(),
          mostRecentPath.getX2(),
          mostRecentPath.getY2(),
          intersectingPath.getX1(),
          intersectingPath.getY1(),
          intersectingPath.getX2(),
          intersectingPath.getY2());

      if (translateX != 0) {
        return new Point((int) intersectingPath.getX1(), (int) mostRecentPath.getY1());
      } else if (translateY != 0) {
        return new Point((int) mostRecentPath.getX1(), (int) intersectingPath.getY1());
      }

      return null;
    }
  }

  private enum Walking {
    LEFT,
    RIGHT;

    public static Walking fromCode(String code) {
      if (code.equals("L")) {
        return LEFT;
      } else if (code.equals("R")) {
        return RIGHT;
      }

      throw new IllegalArgumentException(String.format("Invalid walking direction: %s", code));
    }
  }
}
