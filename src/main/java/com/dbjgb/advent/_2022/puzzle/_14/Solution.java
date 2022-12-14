package com.dbjgb.advent._2022.puzzle._14;

import com.dbjgb.advent.Utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern COORDINATE_PATTERN = Pattern.compile("(\\d+),(\\d+)");

  public static void main(String... args) throws Exception {
    Cell lowerLeft = new Cell(Integer.MIN_VALUE, Integer.MAX_VALUE);
    Cell lowerRight = new Cell(Integer.MIN_VALUE, Integer.MIN_VALUE);
    Cell sandOrigin = new Cell(0, 500);
    int minRow = Integer.MAX_VALUE;
    int minColumn = Integer.MAX_VALUE;
    int maxRow = Integer.MIN_VALUE;
    int maxColumn = Integer.MIN_VALUE;

    List<String> lines = Utility.readAllLines("_2022/puzzle/_14/input.txt");
    Set<Cell> walls = new HashSet<>();
    for (String line : lines) {
      Matcher coordinateMatcher = COORDINATE_PATTERN.matcher(line);
      Cell lastCell = null;
      while (coordinateMatcher.find()) {
        int row = Integer.parseInt(coordinateMatcher.group(2));
        int column = Integer.parseInt(coordinateMatcher.group(1));

        Cell currentCell = new Cell(row, column);
        walls.add(currentCell);
        walls.addAll(calculatePointsInLine(lastCell, currentCell));
        lastCell = currentCell;

        lowerLeft =
            new Cell(Math.max(row, lowerLeft.getRow()), Math.min(column, lowerLeft.getColumn()));
        lowerRight = new Cell(lowerLeft.getRow(), Math.max(column, lowerRight.getColumn()));
        minRow = Math.min(minRow, row);
        minColumn = Math.min(minColumn, column);
        maxRow = Math.max(maxRow, row);
        maxColumn = Math.max(maxColumn, column);
      }
    }

    Set<Cell> sand =
        fillWithSand(sandOrigin, walls, new Cell(maxRow + 2, minColumn), new Cell(maxRow + 2, maxColumn));
    System.out.printf("Sand Units: %d\n", sand.size());

    sand.clear();
    addFloorTo(walls, sandOrigin, maxRow + 2);
    minColumn = walls.stream().map(Cell::getColumn).reduce(Integer.MAX_VALUE, Math::min);
    maxColumn = walls.stream().map(Cell::getColumn).reduce(Integer.MIN_VALUE, Math::max);
    maxRow = walls.stream().map(Cell::getRow).reduce(Integer.MIN_VALUE, Math::max);

    sand.addAll(fillWithSand(sandOrigin, walls, new Cell(maxRow + 1, minColumn - 1), new Cell(maxRow + 1, maxColumn + 1)));
    System.out.printf("Sand Units (until plugged): %d\n", sand.size());

    System.out.print("       ");
    for (int column = minColumn - 2; column <= maxColumn + 2; column++) {
      System.out.print((column == 500) ? '+' : '.');
    }
    System.out.print('\n');

    for (int row = 0; row <= maxRow + 2; row++) {
      System.out.printf("%05d: ", row);
      for (int column = minColumn - 2; column <= maxColumn + 2; column++) {
        Cell cell = new Cell(row, column);
        if (sand.contains(cell)) {
          System.out.print('o');
        } else if (walls.contains(cell)) {
          System.out.print('#');
        } else {
          System.out.print('.');
        }
      }
      System.out.print('\n');
    }
  }

  private static List<Cell> calculatePointsInLine(Cell lastCell, Cell currentCell) {
    if (lastCell == null) {
      return List.of();
    }

    List<Cell> cells = new ArrayList<>();
    int rowsDelta = (int) Math.signum(currentCell.getRow() - lastCell.getRow());
    int columnsDelta = (int) Math.signum(currentCell.getColumn() - lastCell.getColumn());
    Cell newCell = null;
    while (!(newCell = new Cell(lastCell.getRow() + rowsDelta, lastCell.getColumn() + columnsDelta))
        .equals(currentCell)) {
      cells.add(newCell);
      rowsDelta += (int) Math.signum(rowsDelta);
      columnsDelta += (int) Math.signum(columnsDelta);
    }

    return cells;
  }

  public static Set<Cell> fillWithSand(
      Cell source, Set<Cell> walls, Cell leftBoundary, Cell rightBoundary) {
    Set<Cell> grainsOfSand = new HashSet<>();
    while (true) {
      Cell sand = source;
      while (!canRest(sand, grainsOfSand, walls)) {
        if (canMoveDown(sand, grainsOfSand, walls)) {
          sand = sand.moveDown();
        } else if (canMoveDownLeft(sand, grainsOfSand, walls)) {
          sand = sand.moveDownLeft();
        } else {
          sand = sand.moveDownRight();
        }

        if (floatingIntoAbyss(leftBoundary, rightBoundary, sand)) {
          return grainsOfSand;
        }
      }

      grainsOfSand.add(sand);
      if (sand.equals(source)) {
        break;
      }
    }

    return grainsOfSand;
  }

  private static boolean floatingIntoAbyss(Cell leftBoundary, Cell rightBoundary, Cell sand) {
    int column = sand.getColumn();
    int row = sand.getRow();
    if (column == leftBoundary.getColumn()
        || column == rightBoundary.getColumn()
        || row == leftBoundary.getRow()) {
      return true;
    }
    return false;
  }

  public static boolean canRest(Cell sand, Set<Cell> currentPile, Set<Cell> walls) {
    return !canMoveDown(sand, currentPile, walls)
        && !canMoveDownLeft(sand, currentPile, walls)
        && !canMoveDownRight(sand, currentPile, walls);
  }

  public static boolean canMoveDown(Cell sand, Set<Cell> currentPile, Set<Cell> walls) {
    Cell below = sand.moveDown();
    return !currentPile.contains(below) && !walls.contains(below);
  }

  public static boolean canMoveDownLeft(Cell sand, Set<Cell> currentPile, Set<Cell> walls) {
    Cell belowLeft = sand.moveDownLeft();
    return !currentPile.contains(belowLeft) && !walls.contains(belowLeft);
  }

  public static boolean canMoveDownRight(Cell sand, Set<Cell> currentPile, Set<Cell> walls) {
    Cell belowRight = sand.moveDownRight();
    return !currentPile.contains(belowRight) && !walls.contains(belowRight);
  }

  public static void addFloorTo(Set<Cell> walls, Cell source, int maxRow) {
    walls.add(new Cell(maxRow, source.getColumn()));

    Cell leftCell = source.moveDownLeft();
    Cell rightCell = source.moveDownRight();
    while (leftCell.getRow() < maxRow && rightCell.getRow() < maxRow ) {
      walls.add(new Cell(maxRow, leftCell.getColumn()));
      walls.add(new Cell(maxRow, rightCell.getColumn()));
      leftCell = leftCell.moveDownLeft();
      rightCell = rightCell.moveDownRight();
    }

    walls.add(leftCell);
    walls.add(rightCell);
  }
}
