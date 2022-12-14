package com.dbjgb.advent._2022.puzzle._12;

import org.xguzm.pathfinding.grid.GridCell;

import java.util.Objects;

public class Cell extends GridCell {

  private final char height;

  public Cell(int row, int column, char height) {
    super(row, column);
    this.height = height;
  }

  public char getHeight() {
    return height;
  }

  public boolean reachableFrom(Cell cell) {
    if (cell.getHeight() == 'S') {
      return this.height == 'a';
    }

    if (this.height == 'E') {
      return cell.getHeight() == 'z';
    }

    return this.height <= (cell.getHeight() + 1);
  }
}
