package com.dbjgb.advent.twenty.puzzle.twenty;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

class Tile {

  private final long id;
  private char[][] data;

  private final Map<Side, Tile> matchesBySide;

  public Tile(long id, char[][] data) {
    this.id = id;
    this.data = data;
    this.matchesBySide = new HashMap<>();
  }

  public long getId() {
    return id;
  }

  public char[][] getData() {
    return data;
  }

  public Tile getTileOnSide(Side side) {
    return matchesBySide.get(side);
  }

  public boolean hasTwoEmptySides() {
    int numberOfEmptySides = 0;
    for (Side side : Side.getAllSides()) {
      if (matchesBySide.get(side) == null) {
        numberOfEmptySides += 1;
      }
    }

    return numberOfEmptySides == 2;
  }

  public void addCandidate(Side side, Tile candidate) {
    assert matchesBySide.get(side) == null : "Multiple matches found.";
    matchesBySide.put(side, candidate);
  }

  public void removeCandidate(Side side, Tile candidate) {
    if (matchesBySide.get(side) == candidate) {
      matchesBySide.remove(side, candidate);
      candidate.removeCandidate(side.oppositeSide(), this);
    }
  }

  public boolean matchesFromSideToTileSide(Side selfSide, Tile possibleMatchTile, Side tileSide) {
    if (this == possibleMatchTile) {
      return false;
    }

    String selfSideValue = selfSide.sideValue(getData());
    String matchingSideValue = tileSide.sideValue(possibleMatchTile.getData());
    String reverseMatchingSideValue = new StringBuilder(matchingSideValue).reverse().toString();
    return selfSideValue.equals(matchingSideValue) || selfSideValue.equals(reverseMatchingSideValue);
  }

  public void orientSideToTileSide(Side selfSide, Tile tile, Side side) {
    String matchingSideValue = side.sideValue(tile.getData());
    StringBuilder selfSideValue = new StringBuilder(selfSide.sideValue(getData()));
    if (!matchingSideValue.equals(selfSideValue.toString()) && !matchingSideValue.equals(selfSideValue.reverse().toString())) {
      throw new IllegalStateException(String.format("%d side %s does not match %d side %s.\n", tile.getId(), side, getId(), selfSide));
    }

    Side oppositeSide = side.oppositeSide();
    if (oppositeSide != selfSide) {
      while (oppositeSide != selfSide) {
        this.data = Rotation.LEFT.applyTo(data);
        selfSide = selfSide.translate(Rotation.LEFT);
      }
    }
    selfSideValue = new StringBuilder(selfSide.sideValue(getData()));
    if (matchingSideValue.equals(selfSideValue.toString())) {
      this.data = selfSide.flipDirection().applyTo(this.data);
    }

    matchesBySide.put(oppositeSide, tile);
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
    return id == tile.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
