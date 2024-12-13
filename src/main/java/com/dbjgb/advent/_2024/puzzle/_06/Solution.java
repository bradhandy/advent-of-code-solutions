package com.dbjgb.advent._2024.puzzle._06;

import com.dbjgb.advent.Utility;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  public static void main(String... args) throws Exception {
    solvePartOne();
    solvePartTwo();
  }

  private static void solvePartOne() throws Exception {
    Grid grid = new Grid();

    while (grid.isGuardInGrid()) {
      grid.moveGuardToNextObstacle();
    }

    System.out.printf("Part One: %d\n", grid.getVisitedPoints().size());
//    Set<Point> obstacles = new LinkedHashSet<>();
//    Set<Point> visited = new HashSet<>();
//    Guard guard = new Guard(Direction.UP);
//    int gridWidth = -1;
//
//    int lineNumber = 0;
//    for (String line : Utility.readAllLines("_2024/puzzle/_06/input.txt")) {
//      gridWidth = line.length();
//
//      int obstacle = 0;
//      while (obstacle < line.length() && (obstacle = line.indexOf('#', obstacle)) != -1) {
//        Point point = new Point(obstacle, lineNumber);
//        obstacles.add(point);
//        obstacle++;
//      }
//
//      char direction = guard.getCurrentDirection().getMarker();
//      if (line.indexOf(direction) != -1) {
//        guard.setCurrentLocation(new Point(line.indexOf(direction), lineNumber));
//      }
//      lineNumber++;
//    }
//    guard.setSearchGrid(lineNumber, gridWidth);
//    guard.setObstacles(obstacles);
//
//    visited.add(guard.getCurrentLocation());
//    while (guard.isInTheGrid()) {
//      visited.addAll(guard.moveToObstacle());
//    }
//
//    System.out.printf("Part One: %d\n", visited.size());
  }

  private static void solvePartTwo() throws Exception {
    Grid grid = new Grid();

    while (grid.isGuardInGrid()) {
      grid.moveGuardToNextObstacle();
    }

    for (Point point : grid.getPotentialObstaclePlacements()) {
      System.out.printf("x = %d ; y = %d\n", point.getX(), point.getY());
    }
    System.out.printf("Part Two: %d\n", grid.getPotentialObstaclePlacements().size());
  }

  private static class Grid {

    private static final Pattern OBSTACLE_LOCATION = Pattern.compile("#");
    private static final Pattern GUARD_LOCATION = Pattern.compile("([<>^v])");

    private SortedSet<Point> verticallyOrderedObstacles = new TreeSet<>(Point.ORDER_BY_X);
    private SortedSet<Point> horizontallyOrderedObstacles = new TreeSet<>(Point.ORDER_BY_Y);
    private Set<Point> obstacles = new LinkedHashSet<>();
    private Integer width;
    private Integer height;
    private Guard guard;

    private Set<Point> visitedPoints = new LinkedHashSet<>();
    private Map<Point, Set<Direction>> visitedObstacles = new HashMap<>();
    private List<Path> pathsTaken = new ArrayList<>();
    private Set<Point> potentialObstaclePlacements = new HashSet<>();

    public Grid() throws Exception {
      List<String> gridLines = Utility.readAllLines("_2024/puzzle/_06/input.txt");
      width = gridLines.get(0).length();
      height = gridLines.size();

      for (int lineNumber = 0; lineNumber < gridLines.size(); lineNumber++) {
        String line = gridLines.get(lineNumber);
        Matcher obstacleMatcher = OBSTACLE_LOCATION.matcher(line);
        while (obstacleMatcher.find()) {
          Point point = new Point(obstacleMatcher.toMatchResult().start(), lineNumber);
          obstacles.add(point);
        }

        Matcher guardMatcher = GUARD_LOCATION.matcher(line);
        if (guardMatcher.find()) {
          MatchResult searchResult = guardMatcher.toMatchResult();
          guard = new Guard(Direction.fromCode(searchResult.group(1).charAt(0)));
          guard.setCurrentLocation(new Point(searchResult.start(), lineNumber));
          visitedPoints.add(guard.getCurrentLocation());
        }
      }

      verticallyOrderedObstacles.addAll(obstacles);
      horizontallyOrderedObstacles.addAll(obstacles);
    }

    public Set<Point> getPotentialObstaclePlacements() {
      return potentialObstaclePlacements;
    }

    public Set<Point> getVisitedPoints() {
      return visitedPoints;
    }

    public boolean isGuardInGrid() {
      return isPointInGrid(guard.getCurrentLocation());
    }

    private boolean isPointInGrid(Point point) {
      return point.getX() >= 0 && point.getX() < width && point.getY() >= 0 && point.getY() < height;
    }

    public void moveGuardToNextObstacle() {
      Point nextObstacle = findNextObstacle(guard.getCurrentLocation(), guard.getCurrentDirection());

      Set<Point> path = guard.moveToNextObstacle(nextObstacle, !isPointInGrid(nextObstacle));
      potentialObstaclePlacements.addAll(findPotentialCircuits(path, guard.getCurrentDirection()));
      visitedPoints.addAll(path);
      pathsTaken.add(new Path(guard.getCurrentDirection(), path));
      Set<Direction> visitedFrom = visitedObstacles.getOrDefault(nextObstacle, new HashSet<>());
      visitedFrom.add(guard.getCurrentDirection());
      visitedObstacles.putIfAbsent(nextObstacle, visitedFrom);
      guard.rotateToRight();
    }

    public Set<Point> findPotentialCircuits(Set<Point> potentialTurningPoints, Direction direction) {
      Set<Point> newObstaclePlacements = new HashSet<>();
      Map<Point, Set<Direction>> fakeVisitedObstacles = new HashMap<>(visitedObstacles);
      for (Point potentialTurningPoint : potentialTurningPoints) {
        Point potentialObstacle = direction.move(potentialTurningPoint);
        if (visitedPoints.contains(potentialObstacle) || obstacles.contains(potentialObstacle)) {
          continue;
        }

        obstacles.add(potentialObstacle);
        verticallyOrderedObstacles.add(potentialObstacle);
        horizontallyOrderedObstacles.add(potentialObstacle);
        fakeVisitedObstacles.put(potentialObstacle, new HashSet<>(Set.of(direction)));

        Direction newDirection = direction.rotate();
        Point targetObstacle = findNextObstacle(potentialTurningPoint, newDirection);
        while (isPointInGrid(targetObstacle)) {
          Set<Direction> visitedFrom = new HashSet<>(fakeVisitedObstacles.getOrDefault(targetObstacle, new HashSet<>()));
          if (visitedFrom.contains(newDirection)) {
            newObstaclePlacements.add(potentialObstacle);
            break;
          }

          visitedFrom.add(direction);
          fakeVisitedObstacles.put(targetObstacle, visitedFrom);
          targetObstacle = findNextObstacle(newDirection.reverse(targetObstacle), newDirection.rotate());
          newDirection = newDirection.rotate();
        }

        obstacles.remove(potentialObstacle);
        verticallyOrderedObstacles.remove(potentialObstacle);
        horizontallyOrderedObstacles.remove(potentialObstacle);
      }

      return newObstaclePlacements;
    }

    private Point findNextObstacle(Point start, Direction direction) {
      SortedSet<Point> sortedObstacles = (direction.vertical) ? verticallyOrderedObstacles: horizontallyOrderedObstacles;
      SortedSet<Point> nextObstacles =
          (direction.forward) ? sortedObstacles.tailSet(start) : sortedObstacles.subSet(sortedObstacles.first(), start);

      Point nextObstacle = null;
      if (!nextObstacles.isEmpty()) {
        nextObstacle = (direction.forward) ? nextObstacles.first() : nextObstacles.last();
      }

      if (nextObstacle == null || (direction.vertical && nextObstacle.getX() != start.getX()) || (!direction.vertical && nextObstacle.getY() != start.getY())) {
        nextObstacle = createOffGridPoint(start, direction);
      }

      return nextObstacle;
    }

    private Point createOffGridPoint(Point from, Direction direction) {
      int newGridPoint = direction.forward ? getForwardOffGridPoint(direction) : -1;
      if (direction.vertical) {
        return new Point(from.getX(), newGridPoint);
      }

      return new Point(newGridPoint, from.getY());
    }

    private int getForwardOffGridPoint(Direction direction) {
      return direction.vertical ? height : width;
    }

  }

  private static class Path {

    private final Direction direction;
    private final Set<Point> points;

    public Path(Direction direction, Set<Point> points) {
      this.direction = direction;
      this.points = points;
    }

    public boolean intersectsInDirection(Point point, Direction direction) {
      return this.direction == direction && points.contains(point);
    }
  }

  private static class Guard {

    private Direction currentDirection;
    private Point currentLocation;

    public Guard(Direction currentDirection) {
      this.currentDirection = currentDirection;
    }

    public Direction getCurrentDirection() {
      return currentDirection;
    }

    public Point getCurrentLocation() {
      return currentLocation;
    }

    public void setCurrentLocation(Point currentLocation) {
      this.currentLocation = currentLocation;
    }

    public void rotateToRight() {
      currentDirection = currentDirection.rotate();
    }

    public Set<Point> moveToNextObstacle(Point endExclusive, boolean offGrid) {
      Set<Point> pointsVisited = new HashSet<>();
      Point current = currentLocation;
      while (!current.equals(endExclusive)) {
        current = currentDirection.move(current);
        pointsVisited.add(current);
        if (offGrid || !current.equals(endExclusive)) {
          currentLocation = current;
        }
      }
      pointsVisited.remove(endExclusive);

      return pointsVisited;
    }
  }

  public enum Direction {
    UP('^', new Point(0, -1)), RIGHT('>', new Point(1, 0)), DOWN('v', new Point(0, 1)), LEFT('<', new Point(-1, 0));

    private static final Direction[] DIRECTIONS = values();
    private static final Map<Character, Direction> DIRECTIONS_BY_CODE = Maps.uniqueIndex(List.of(values()), Direction::getMarker);

    public static Direction fromCode(char directionCode) {
      return DIRECTIONS_BY_CODE.get(directionCode);
    }

    private final char marker;
    private final Point deltaPoint;
    private final boolean vertical;
    private final boolean forward;

    Direction(char marker, Point deltaPoint) {
      this.marker = marker;
      this.deltaPoint = deltaPoint;
      this.vertical = deltaPoint.getY() != 0;
      this.forward = (this.vertical) ? deltaPoint.getY() > 0 : deltaPoint.getX() > 0;
    }

    public Point move(Point start) {
      return new Point(start.getX() + deltaPoint.getX(), start.getY() + deltaPoint.getY());
    }

    public Point reverse(Point start) {
      Point reverseDelta = new Point(deltaPoint.getX() * -1, deltaPoint.getY() * -1);
      return new Point(start.getX() + reverseDelta.getX(), start.getY() + reverseDelta.getY());
    }

    public char getMarker() {
      return marker;
    }

    public Direction rotate() {
      return DIRECTIONS[(ordinal() + 1) % DIRECTIONS.length];
    }
  }

  private static class Point {

    public static final Comparator<Point> ORDER_BY_Y = (o1, o2) -> {
      int result = Integer.compare(o1.y, o2.y);
      if (result == 0) {
        return Integer.compare(o1.x, o2.x);
      }

      return result;
    };
    public static final Comparator<Point> ORDER_BY_X = (o1, o2) -> {
      int result = Integer.compare(o1.x, o2.x);
      if (result == 0) {
        return Integer.compare(o1.y, o2.y);
      }

      return result;
    };

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
      if (!(o instanceof Point point)) {
        return false;
      }
      return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }
  }
}
