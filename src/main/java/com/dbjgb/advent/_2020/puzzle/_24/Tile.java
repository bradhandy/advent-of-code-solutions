package com.dbjgb.advent._2020.puzzle._24;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Tile implements Comparable<Tile> {

  private final int row;
  private final int column;
  private final Map<Direction, Tile> neighbors;

  private boolean flipped;

  public Tile(int row, int column) {
    this.row = row;
    this.column = column;
    this.neighbors = new HashMap<>();
  }

  public Tile getNeighbor(Direction direction) {
    return neighbors.getOrDefault(
        direction, new Tile(row + direction.getDeltaRow(), column + direction.getDeltaColumn()));
  }

  public void setNeighbors(Map<Direction, Tile> neighbors) {
    this.neighbors.clear();
    this.neighbors.putAll(neighbors);
  }

  public long getBlackNeighbors() {
    return neighbors.values().stream().filter(Tile::isFlipped).count();
  }

  public int getRow() {
    return row;
  }

  public int getColumn() {
    return column;
  }

  public void flip() {
    this.flipped = !this.flipped;
  }

  public boolean isFlipped() {
    return this.flipped;
  }

  public String toString() {
    return String.format("{ row = %d, column = %d }", row, column);
  }

  @Override
  public int compareTo(Tile o) {
    int result = Integer.compare(row, o.row);
    if (result == 0) {
      return Integer.compare(column, o.column);
    }

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tile tile = (Tile) o;
    return row == tile.row && column == tile.column;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, column);
  }
}
