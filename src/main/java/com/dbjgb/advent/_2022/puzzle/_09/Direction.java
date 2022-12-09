package com.dbjgb.advent._2022.puzzle._09;

import java.util.Map;
import java.util.function.Function;

public enum Direction implements Function<Cell, Cell> {

  LEFT(0, -1),
  UP(1, 0),
  RIGHT(0, 1),
  DOWN(-1, 0),
  NONE(0, 0);

  private final int deltaRow;
  private final int deltaColumn;

  private static final Map<Character, Direction> DIRECTIONS_BY_CODE =
      Map.of('L', LEFT, 'U', UP, 'R', RIGHT, 'D', DOWN);

  public static Direction valueByCode(Character code) {
    return DIRECTIONS_BY_CODE.get(code);
  }

  public static Direction valueByDeltaRow(int deltaRow) {
    if (deltaRow == 0) {
      return NONE;
    }

    return (deltaRow > 0) ? UP : DOWN;
  }

  public static Direction valueByDeltaColumn(int deltaColumn) {
    if (deltaColumn == 0) {
      return NONE;
    }

    return (deltaColumn > 0) ? RIGHT : LEFT;
  }

  Direction(int deltaRow, int deltaColumn) {
    this.deltaRow = deltaRow;
    this.deltaColumn = deltaColumn;
  }

  @Override
  public Cell apply(Cell cell) {
    return new Cell(cell.getRow() + deltaRow, cell.getColumn() + deltaColumn);
  }
}
