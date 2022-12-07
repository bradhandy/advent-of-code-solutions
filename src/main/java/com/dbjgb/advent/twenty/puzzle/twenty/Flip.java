package com.dbjgb.advent.twenty.puzzle.twenty;

public enum Flip implements CoordinateTranslator {

  HORIZONTAL() {
    @Override
    public Cell translate(int rows, int columns, Cell cell) {
      int row = cell.getRow();
      int column = cell.getColumn();

      return new Cell(row, (columns - 1) - column);
    }
  },
  VERTICAL() {
    @Override
    public Cell translate(int rows, int columns, Cell cell) {
      int row = cell.getRow();
      int column = cell.getColumn();

      return new Cell((rows - 1) - row, column);
    }
  };

}
