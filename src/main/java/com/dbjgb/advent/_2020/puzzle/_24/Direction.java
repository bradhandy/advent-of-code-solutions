package com.dbjgb.advent._2020.puzzle._24;

public enum Direction {

  W(0, -2), NW(1, -1), NE(1, 1), E(0, 2), SE(-1, 1), SW(-1, -1);

  private final int deltaRow;
  private final int deltaColumn;

  Direction(int deltaRow, int deltaColumn) {
    this.deltaRow = deltaRow;
    this.deltaColumn = deltaColumn;
  }

  public int getDeltaRow() {
    return deltaRow;
  }

  public int getDeltaColumn() {
    return deltaColumn;
  }
}
