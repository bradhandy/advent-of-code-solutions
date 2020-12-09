package com.dbjgb.advent.twenty.fifteen.puzzle.three;

import com.dbjgb.advent.Utility;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Solution {

  public static void main(String... args) throws Exception {
    printNumberOfHousesWithOnePresentJustSanta();
    printNumberOfHousesWithOnePresentSantaAndRoboSanta();
  }

  private static void printNumberOfHousesWithOnePresentJustSanta() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/three/input.txt")) {
      Set<Point> pointsVisited = new HashSet<>();
      String instructions = inputReader.readLine();

      Point currentLocation = new Point(0, 0);
      pointsVisited.add(currentLocation.getLocation());

      for (int i = 0; i < instructions.length(); i++) {
        Direction direction = Direction.fromCode(instructions.charAt(i));
        direction.translate(currentLocation);
        pointsVisited.add(currentLocation.getLocation());
      }

      System.out.printf("Houses w/ at least one present: %d\n", pointsVisited.size());
    }
  }

  private static void printNumberOfHousesWithOnePresentSantaAndRoboSanta() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/three/input.txt")) {
      Set<Point> pointsVisited = new HashSet<>();
      String instructions = inputReader.readLine();

      Point santaCurrentLocation = new Point(0, 0);
      Point roboSantaCurrentLocation = santaCurrentLocation.getLocation();
      pointsVisited.add(santaCurrentLocation.getLocation());

      for (int i = 0; i < instructions.length(); i++) {
        Direction direction = Direction.fromCode(instructions.charAt(i));
        Point newLocation =
            direction.translate((i % 2 == 0) ? santaCurrentLocation : roboSantaCurrentLocation);
        pointsVisited.add(newLocation.getLocation());
      }

      System.out.printf("Houses w/ at least one present: %d", pointsVisited.size());
    }
  }

  private enum Direction {
    NORTH(0, 1),
    WEST(-1, 0),
    EAST(1, 0),
    SOUTH(0, -1);

    private final int deltaX;
    private final int deltaY;

    Direction(int deltaX, int deltaY) {
      this.deltaX = deltaX;
      this.deltaY = deltaY;
    }

    public Point translate(Point point) {
      point.translate(deltaX, deltaY);
      return point;
    }

    public static Direction fromCode(char code) {
      if (code == '^') {
        return NORTH;
      }

      if (code == 'v') {
        return SOUTH;
      }

      if (code == '<') {
        return WEST;
      }

      return EAST;
    }
  }
}
