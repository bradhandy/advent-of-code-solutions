package com.dbjgb.advent._2024.puzzle._06;

import com.dbjgb.advent.Direction;
import com.dbjgb.advent.Point;
import com.dbjgb.advent.Utility;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
  }

  private static void solvePartTwo() throws Exception {
    Grid grid = new Grid();

    while (grid.isGuardInGrid()) {
      grid.moveGuardToNextObstacle();
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
    private Map<Point, Set<GridDirection>> visitedObstacles = new HashMap<>();
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
          guard = new Guard(GridDirection.fromCode(searchResult.group(1).charAt(0)));
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
      Set<GridDirection> visitedFrom = visitedObstacles.getOrDefault(nextObstacle, new HashSet<>());
      visitedFrom.add(guard.getCurrentDirection());
      visitedObstacles.putIfAbsent(nextObstacle, visitedFrom);
      guard.rotateToRight();
    }

    public Set<Point> findPotentialCircuits(Set<Point> potentialTurningPoints, GridDirection gridDirection) {
      Set<Point> newObstaclePlacements = new HashSet<>();

      for (Point potentialTurningPoint : potentialTurningPoints) {
        if (visitedPoints.contains(potentialTurningPoint)) {
          continue;
        }

        Map<Point, Set<GridDirection>> visitedObstacles = new HashMap<>(this.visitedObstacles);

        GridDirection currentGridDirection = gridDirection;
        Point currentObstacle = potentialTurningPoint;
        verticallyOrderedObstacles.add(potentialTurningPoint);
        horizontallyOrderedObstacles.add(potentialTurningPoint);
        while (isPointInGrid(currentObstacle)) {
          Set<GridDirection> sourceGridDirections = new HashSet<>(visitedObstacles.getOrDefault(currentObstacle, new HashSet<>()));
          if (sourceGridDirections.contains(currentGridDirection)) {
            newObstaclePlacements.add(potentialTurningPoint);
            break;
          }

          sourceGridDirections.add(currentGridDirection);
          visitedObstacles.put(currentObstacle, sourceGridDirections);

          Point traversablePoint = currentGridDirection.reverse(currentObstacle);
          currentGridDirection = currentGridDirection.rotate();
          currentObstacle = findNextObstacle(traversablePoint, currentGridDirection);
        }
        verticallyOrderedObstacles.remove(potentialTurningPoint);
        horizontallyOrderedObstacles.remove(potentialTurningPoint);
      }

      return newObstaclePlacements;
    }

    private Point findNextObstacle(Point start, GridDirection gridDirection) {
      SortedSet<Point> sortedObstacles = (gridDirection.vertical) ? verticallyOrderedObstacles: horizontallyOrderedObstacles;
      SortedSet<Point> nextObstacles =
          (gridDirection.forward) ? sortedObstacles.tailSet(start) : sortedObstacles.subSet(sortedObstacles.first(), start);

      Point nextObstacle = null;
      if (!nextObstacles.isEmpty()) {
        nextObstacle = (gridDirection.forward) ? nextObstacles.first() : nextObstacles.last();
      }

      if (nextObstacle == null || (gridDirection.vertical && nextObstacle.getX() != start.getX()) || (!gridDirection.vertical && nextObstacle.getY() != start.getY())) {
        nextObstacle = createOffGridPoint(start, gridDirection);
      }

      return nextObstacle;
    }

    private Point createOffGridPoint(Point from, GridDirection gridDirection) {
      int newGridPoint = gridDirection.forward ? getForwardOffGridPoint(gridDirection) : -1;
      if (gridDirection.vertical) {
        return new Point(from.getX(), newGridPoint);
      }

      return new Point(newGridPoint, from.getY());
    }

    private int getForwardOffGridPoint(GridDirection gridDirection) {
      return gridDirection.vertical ? height : width;
    }

  }

  private static class Guard {

    private GridDirection currentGridDirection;
    private Point currentLocation;

    public Guard(GridDirection currentGridDirection) {
      this.currentGridDirection = currentGridDirection;
    }

    public GridDirection getCurrentDirection() {
      return currentGridDirection;
    }

    public Point getCurrentLocation() {
      return currentLocation;
    }

    public void setCurrentLocation(Point currentLocation) {
      this.currentLocation = currentLocation;
    }

    public void rotateToRight() {
      currentGridDirection = currentGridDirection.rotate();
    }

    public Set<Point> moveToNextObstacle(Point endExclusive, boolean offGrid) {
      Set<Point> pointsVisited = new LinkedHashSet<>();
      Point current = currentLocation;
      while (!current.equals(endExclusive)) {
        current = currentGridDirection.move(current);
        pointsVisited.add(current);
        if (offGrid || !current.equals(endExclusive)) {
          currentLocation = current;
        }
      }
      pointsVisited.remove(endExclusive);

      return pointsVisited;
    }
  }

  public enum GridDirection {
    UP('^', true, false), RIGHT('>', false, true), DOWN('v', true, true), LEFT('<', false, false);

    private static final GridDirection[] GRID_DIRECTIONS = values();
    private static final Map<Character, GridDirection> DIRECTIONS_BY_CODE = Maps.uniqueIndex(List.of(values()), GridDirection::getMarker);

    public static GridDirection fromCode(char directionCode) {
      return DIRECTIONS_BY_CODE.get(directionCode);
    }

    private final char marker;
    private final boolean vertical;
    private final boolean forward;

    GridDirection(char marker, boolean vertical, boolean forward) {
      this.marker = marker;
      this.vertical = vertical;
      this.forward = forward;
    }

    public Point move(Point start) {
      Direction direction = Direction.byName(toString());
      return direction.forward(start);
    }

    public Point reverse(Point start) {
      Direction direction = Direction.byName(toString());
      return direction.reverse(start);
    }

    public char getMarker() {
      return marker;
    }

    public GridDirection rotate() {
      return GRID_DIRECTIONS[(ordinal() + 1) % GRID_DIRECTIONS.length];
    }
  }

}
