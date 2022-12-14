package com.dbjgb.advent._2022.puzzle._14;

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

  public Cell moveDown() {
    return new Cell(row + 1, column);
  }

  public Cell moveDownLeft() {
    return new Cell(row + 1, column - 1);
  }

  public Cell moveDownRight() {
    return new Cell(row + 1, column + 1);
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
