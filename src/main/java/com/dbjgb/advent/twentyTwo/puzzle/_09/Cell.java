package com.dbjgb.advent.twentyTwo.puzzle._09;

import java.util.Objects;

public class Cell {

  private final int row;
  private final int column;

  public Cell(int row, int column) {
    this.row = row;
    this.column = column;
  }

  public int getRow() {
    return row;
  }

  public int getColumn() {
    return column;
  }

  public boolean adjacentTo(Cell cell) {
    return cell.getRow() >= (getRow() - 1)
        && cell.getRow() <= (getRow() + 1)
        && cell.getColumn() >= (getColumn() - 1)
        && cell.getColumn() <= (getColumn() + 1);
  }

  public int getRowDifferenceComparedTo(Cell cell) {
    return row - cell.getRow();
  }

  public int getColumnDifferenceComparedTo(Cell cell) {
    return column - cell.getColumn();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Cell cell = (Cell) o;
    return row == cell.row && column == cell.column;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, column);
  }
}
