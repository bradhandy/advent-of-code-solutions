package com.dbjgb.advent._2020.puzzle._20;

public interface CoordinateTranslator {

  Cell translate (int rows, int columns, Cell cell);

  default char[][] applyTo(char[][] grid) {
    char[][] destination = new char[grid.length][grid[0].length];
    for (int row = 0; row < grid.length; row++) {
      for (int column = 0; column < grid[0].length; column++) {
        Cell translatedCell = translate(grid.length, grid[0].length, new Cell(row, column));
        destination[translatedCell.getRow()][translatedCell.getColumn()] = grid[row][column];
      }
    }

    return destination;
  }
}
