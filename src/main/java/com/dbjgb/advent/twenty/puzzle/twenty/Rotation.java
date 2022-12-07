package com.dbjgb.advent.twenty.puzzle.twenty;

public enum Rotation implements CoordinateTranslator {

  /*
   * rows = 10, columns = 10
   * 0, 0  = (columns - 1) - column, row = 9, 0
   * 1, 1  = (columns - 1) - column, row = 8, 1
   * 2, 2  = (columns - 1) - column, row = 7, 2
   * 9, 0  = (columns - 1) - column, row = 9, 9
   */
  LEFT() {
    @Override
    public Cell translate(int rows, int columns, Cell cell) {
      int row = cell.getRow();
      int column = cell.getColumn();

      return new Cell((columns - 1) - column, row);
    }
  },

  /*
   * rows = 10, columns = 10
   * 0, 0  = column, (rows - 1) - row = 0, 9
   * 1, 1  = column, (rows - 1) - row = 1, 8
   * 2, 2  = column, (rows - 1) - row = 2, 7
   * 9, 0  = column, (rows - 1) - row = 0, 0
   */
  RIGHT() {
    @Override
    public Cell translate(int rows, int columns, Cell cell) {
      int row = cell.getRow();
      int column = cell.getColumn();

      return new Cell(column, (row - 1) - row);
    }
  };
}
