package com.dbjgb.advent._2020.puzzle._20;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cell cell = (Cell) o;
    return row == cell.row && column == cell.column;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, column);
  }
}
