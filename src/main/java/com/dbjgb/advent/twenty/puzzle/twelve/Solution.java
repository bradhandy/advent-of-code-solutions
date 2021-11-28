package com.dbjgb.advent.twenty.puzzle.twelve;

import com.dbjgb.advent.Utility;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;

enum Heading {
  NORTH(Movement.NORTH),
  EAST(Movement.EAST),
  SOUTH(Movement.SOUTH),
  WEST(Movement.WEST);

  private static final Heading[] HEADINGS = Heading.values();

  private final Movement movement;

  Heading(Movement movement) {
    this.movement = movement;
  }

  public static Point moveAlongHeading(Point point, int unitsToMove, Heading heading) {
    return heading.movement.move(point, unitsToMove);
  }

  public Heading turnToNewHeading(SteeringDirection direction, int degrees) {

    /*
     * in order to make the array of Headings a wrapping list, we need to calculate what the index
     * of the next Heading should be.  in order to do this we need to know how many turns to make
     * which should (degrees / 90).  However, if we going to the LEFT, we need to subtract this
     * many turns which would leave us with a negative number.  So we add the number of turns to the
     * length of the HEADINGS array to translate the LEFT turns into RIGHT turns.
     */
    int normalizedNumberOfPositions =
        Math.abs((HEADINGS.length + ((degrees / 90) * direction.getDirection())) % HEADINGS.length);
    int newHeadingOrdinal = (ordinal() + normalizedNumberOfPositions) % HEADINGS.length;

    return HEADINGS[newHeadingOrdinal];
  }
}

enum SteeringDirection {
  LEFT(-1),
  RIGHT(1);

  private final int direction;

  SteeringDirection(int direction) {
    this.direction = direction;
  }

  public static SteeringDirection fromCode(char steeringDirection) {
    switch (steeringDirection) {
      case 'L':
        return LEFT;
      case 'R':
        return RIGHT;
    }

    return null;
  }

  public int getDirection() {
    return direction;
  }

  public Point rotatePoint(Point point, int degrees) {
    if ((this == LEFT && degrees == 90) || (this == RIGHT && degrees == 270)) {
      return new Point((int) -point.getY(), (int) point.getX());
    } else if ((this == LEFT && degrees == 270) || (this == RIGHT && degrees == 90)) {
      return new Point((int) point.getY(), (int) -point.getX());
    } else if (degrees == 180) {
      return new Point((int) -point.getX(), (int) -point.getY());
    }

    return point;
  }
}

enum Movement {
  NORTH(0, 1),
  EAST(1, 0),
  SOUTH(0, -1),
  WEST(-1, 0);

  private final int deltaX;
  private final int deltaY;

  Movement(int deltaX, int deltaY) {
    this.deltaX = deltaX;
    this.deltaY = deltaY;
  }

  public static Movement fromCode(char movement) {
    switch (movement) {
      case 'N':
        return NORTH;
      case 'E':
        return EAST;
      case 'S':
        return SOUTH;
      case 'W':
        return WEST;
    }

    return null;
  }

  public Point move(Point point, int unitsToMove) {
    Point mutablePoint = point.getLocation();
    mutablePoint.translate(deltaX * unitsToMove, deltaY * unitsToMove);

    return mutablePoint;
  }
}

public class Solution {

  public static void main(String... args) throws Exception {
    moveShipAccordingToDirections();
    moveShipAccordingToWaypointAndDirections();
  }

  private static void moveShipAccordingToWaypointAndDirections() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/twelve/input.txt")) {
      Point currentLocation = new Point(0, 0);
      Point waypoint = new Point(10, 1);
      Heading currentHeading = Heading.EAST;

      String line;
      while ((line = inputReader.readLine()) != null) {
        Movement movement = Movement.fromCode(line.charAt(0));
        int argument = Integer.parseInt(line.substring(1));
        if (movement != null) {
          waypoint = movement.move(waypoint, argument);
          continue;
        }

        SteeringDirection direction = SteeringDirection.fromCode(line.charAt(0));
        if (direction != null) {
          waypoint = direction.rotatePoint(waypoint, argument);
          continue;
        }

        for (int i = 0; i < argument; i++) {
          currentLocation.translate((int) waypoint.getX(), (int) waypoint.getY());
        }
      }

      System.out.printf(
          "Manhattan Distance:  %d\n",
          Math.abs((int) currentLocation.getX()) + Math.abs((int) currentLocation.getY()));
    }
  }

  private static void moveShipAccordingToDirections() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/twelve/input.txt")) {
      Point currentLocation = new Point(0, 0);
      Heading currentHeading = Heading.EAST;

      String line;
      while ((line = inputReader.readLine()) != null) {
        Movement movement = Movement.fromCode(line.charAt(0));
        int argument = Integer.parseInt(line.substring(1));
        if (movement != null) {
          currentLocation = movement.move(currentLocation, argument);
          continue;
        }

        SteeringDirection direction = SteeringDirection.fromCode(line.charAt(0));
        if (direction != null) {
          currentHeading = currentHeading.turnToNewHeading(direction, argument);
          continue;
        }

        currentLocation = Heading.moveAlongHeading(currentLocation, argument, currentHeading);
      }

      System.out.printf(
          "Manhattan Distance:  %d\n",
          Math.abs((int) currentLocation.getX()) + Math.abs((int) currentLocation.getY()));
    }
  }
}
