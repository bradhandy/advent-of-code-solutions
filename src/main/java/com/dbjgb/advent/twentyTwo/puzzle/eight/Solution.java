package com.dbjgb.advent.twentyTwo.puzzle.eight;

import com.dbjgb.advent.Utility;

import java.util.List;

public class Solution {

  public static void main(String... args) throws Exception {
    List<String> gridLines = Utility.readAllLines("twentyTwo/puzzle/eight/input.txt");
    char[][] grid = new char[gridLines.size()][gridLines.get(0).length()];
    for (int i = 0; i < grid.length; i++) {
      String line = gridLines.get(i);
      System.arraycopy(line.toCharArray(), 0, grid[i], 0, line.length());
    }

    int treesVisible = (99 * 2) + (97 * 2);
    int maxScenicScore = 0;
    for (int row = 1; row < grid.length - 1; row++) {
      for (int column = 1; column < grid[row].length - 1; column++) {
        maxScenicScore = Math.max(maxScenicScore, scenicScoreForLocation(grid, row, column));
        if (visibleFromLeftOrRight(grid, row, column) || visibleFromTopOrBottom(grid, row, column)) {
          treesVisible += 1;
        }
      }
    }

    System.out.printf("Trees Visible: %d\n", treesVisible);
    System.out.printf("Max scenic score: %d\n", maxScenicScore);
  }

  private static boolean visibleFromLeftOrRight(char[][] grid, int startRow, int startColumn) {
    return visibleFromDirection(grid, startRow, startColumn, 0, -1)
        || visibleFromDirection(grid, startRow, startColumn, 0, 1);
  }

  private static boolean visibleFromTopOrBottom(char[][] grid, int startRow, int startColumn) {
    return visibleFromDirection(grid, startRow, startColumn, -1, 0)
        || visibleFromDirection(grid, startRow, startColumn, 1, 0);
  }

  private static boolean visibleFromDirection(char[][] grid, int startRow, int startColumn, int deltaRow, int deltaColumn) {
    int row = startRow;
    int column = startColumn;

    char value = grid[row][column];
    while (row > 0 && row < grid.length - 1 && column > 0 && column < grid[row].length - 1) {
      row += deltaRow;
      column += deltaColumn;
      char comparisonValue = grid[row][column];
      if (comparisonValue >= value) {
        return false;
      }
    }

    return true;
  }

  private static int scenicScoreForLocation(char[][] grid, int startRow, int startColumn) {
    int scenicScore = scenicScoreForDirection(grid, startRow, startColumn, 0, -1)
        * scenicScoreForDirection(grid, startRow, startColumn, 0, 1)
        * scenicScoreForDirection(grid, startRow, startColumn, -1, 0)
        * scenicScoreForDirection(grid, startRow, startColumn, 1, 0);

//    System.out.printf("Scenic Score (%d, %d): %d\n", startRow, startColumn, scenicScore);
    return scenicScore;
  }

  private static int scenicScoreForDirection(char[][] grid, int startRow, int startColumn, int deltaRow, int deltaColumn) {
    int row = startRow;
    int column = startColumn;

    char value = grid[row][column];
    char comparisonValue = '\0';
    int treesVisible = 0;
    while (row > 0 && row < grid.length - 1 && column > 0 && column < grid[row].length - 1 && comparisonValue < value) {
      row += deltaRow;
      column += deltaColumn;
      comparisonValue = grid[row][column];
      if (comparisonValue <= value) {
        treesVisible += 1;
      }
    }

//    System.out.printf("Scenic Score (%d, %d, rowDelta = %d, columnDelta = %d): %d\n", startRow, startColumn, deltaRow, deltaColumn, treesVisible);
    return treesVisible;
  }
}
