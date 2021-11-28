package com.dbjgb.advent.twenty.puzzle.twenty;

import com.dbjgb.advent.Utility;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.SetMultimap;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dbjgb.advent.twenty.puzzle.twenty.Side.BOTTOM;
import static com.dbjgb.advent.twenty.puzzle.twenty.Side.BOTTOM_FLIPPED;
import static com.dbjgb.advent.twenty.puzzle.twenty.Side.LEFT;
import static com.dbjgb.advent.twenty.puzzle.twenty.Side.LEFT_FLIPPED;
import static com.dbjgb.advent.twenty.puzzle.twenty.Side.RIGHT;
import static com.dbjgb.advent.twenty.puzzle.twenty.Side.RIGHT_FLIPPED;
import static com.dbjgb.advent.twenty.puzzle.twenty.Side.TOP;
import static com.dbjgb.advent.twenty.puzzle.twenty.Side.TOP_FLIPPED;

enum Side {
  LEFT((rows, rowNumber) -> rowNumber, (columns, columnNumber) -> 0),
  LEFT_FLIPPED((rows, rowNumber) -> rows - (rowNumber + 1), (columns, columnNumber) -> 0),
  RIGHT((rows, rowNumber) -> rowNumber, (columns, columnNumber) -> columns - 1),
  RIGHT_FLIPPED(
      (rows, rowNumber) -> rows - (rowNumber + 1), (columns, columnNumber) -> columns - 1),
  TOP((rows, rowNumber) -> 0, (columns, columnNumber) -> columnNumber),
  TOP_FLIPPED((rows, rowNumber) -> 0, (columns, columnNumber) -> columns - (columnNumber + 1)),
  BOTTOM((rows, rowNumber) -> rows - 1, (columns, columnNumber) -> columnNumber),
  BOTTOM_FLIPPED(
      (rows, rowNumber) -> rows - 1, (columns, columnNumber) -> columns - (columnNumber + 1));

  private final BiFunction<Integer, Integer, Integer> rowNumberFunction;
  private final BiFunction<Integer, Integer, Integer> columnNumberFunction;

  Side(
      BiFunction<Integer, Integer, Integer> rowNumberFunction,
      BiFunction<Integer, Integer, Integer> columnNumberFunction) {
    this.rowNumberFunction = rowNumberFunction;
    this.columnNumberFunction = columnNumberFunction;
  }

  public int getRow(int rows, int rowNumber) {
    return rowNumberFunction.apply(rows, rowNumber);
  }

  public int getColumn(int columns, int columnNumber) {
    return columnNumberFunction.apply(columns, columnNumber);
  }
}

public class Solution {

  private static final Pattern TILE_ID_PATTERN = Pattern.compile("Tile (\\d+):");

  public static final void main(String... args) throws Exception {
    List<Tile> tiles = parseTiles();
    for (int i = 0; i < tiles.size(); i++) {
      Tile currentTile = tiles.get(i);

      for (int j = 0; j < tiles.size(); j++) {
        if (i == j) {
          continue;
        }

        Tile possibleMatch = tiles.get(j);
        for (Side side : EnumSet.of(LEFT, RIGHT, TOP, BOTTOM)) {
          if (currentTile.matchesSideToLeftSide(side, possibleMatch)) {
            currentTile.addCandidate(side, possibleMatch);
            possibleMatch.addCandidate(LEFT, currentTile);
          }
          if (currentTile.matchesSideToRightSide(side, possibleMatch)) {
            currentTile.addCandidate(side, possibleMatch);
            possibleMatch.addCandidate(RIGHT, currentTile);
          }
          if (currentTile.matchesSideToBottomSide(side, possibleMatch)) {
            currentTile.addCandidate(side, possibleMatch);
            possibleMatch.addCandidate(BOTTOM, currentTile);
          }
          if (currentTile.matchesSideToTopSide(side, possibleMatch)) {
            currentTile.addCandidate(side, possibleMatch);
            possibleMatch.addCandidate(TOP, currentTile);
          }
        }
      }
    }

    long idProduct = 1;
    for (Tile tile : tiles) {
      if (tile.hasTwoEmptySides()) {
        System.out.printf("corner: %d\n", tile.getId());
        for (Side side : EnumSet.of(LEFT, RIGHT, TOP, BOTTOM)) {
          if (tile.getNumberOfCandidatesForSide(side) == 0) {
            System.out.printf("No tile on the %s.\n", side);
          } else {
            System.out.printf("%s side tile is %d.\n", side, tile.getTileOnSide(side).getId());
          }
        }

        idProduct *= tile.getId();
      }
    }

    System.out.printf("ID Product:  %d\n", idProduct);
  }

  private static List<Tile> parseTiles() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/twenty/input.txt")) {
      List<Tile> tiles = new ArrayList<>();
      Tile tile;
      while ((tile = parseTile(inputReader)) != null) {
        tiles.add(tile);
      }

      return tiles;
    }
  }

  private static Tile parseTile(BufferedReader tileReader) throws IOException {
    String line = tileReader.readLine();
    if (Strings.isNullOrEmpty(line)) {
      return null;
    }

    Matcher tileIdMatcher = TILE_ID_PATTERN.matcher(line);
    if (!tileIdMatcher.matches()) {
      throw new IOException("Invalid tile file.");
    }

    long tileId = Long.parseLong(tileIdMatcher.group(1));
    char[][] data = new char[10][];
    int row = 0;
    while (!Strings.isNullOrEmpty(line = tileReader.readLine())) {
      data[row++] = line.toCharArray();
    }

    return new Tile(tileId, data);
  }
}

class Tile {

  private final long id;
  private final char[][] data;

  private SetMultimap<Side, Tile> candidatesBySide;

  public Tile(long id, char[][] data) {
    this.id = id;
    this.data = data;
    this.candidatesBySide = HashMultimap.create();
  }

  public long getId() {
    return id;
  }

  public char[][] getData() {
    return data;
  }

  public int getNumberOfCandidatesForSide(Side side) {
    return candidatesBySide.get(side).size();
  }

  public Tile getTileOnSide(Side side) {
    return Iterables.getOnlyElement(candidatesBySide.get(side));
  }

  public void applyUniqueConstraints(Side side) {
    if (candidatesBySide.get(side).size() == 1) {
      Tile tile = Iterables.getOnlyElement(candidatesBySide.get(side));
      for (Side otherSide : EnumSet.complementOf(EnumSet.of(side))) {
        removeCandidate(otherSide, tile);
      }

      if (side == LEFT) {
        tile.keepCandidate(RIGHT, this);
      } else if (side == RIGHT) {
        tile.keepCandidate(LEFT, this);
      } else if (side == TOP) {
        tile.keepCandidate(BOTTOM, this);
      } else if (side == BOTTOM) {
        tile.keepCandidate(TOP, this);
      }
    }
  }

  public boolean hasTwoEmptySides() {
    int numberOfEmptySides = 0;
    if (getNumberOfCandidatesForSide(LEFT) == 0) {
      numberOfEmptySides += 1;
    }
    if (getNumberOfCandidatesForSide(RIGHT) == 0) {
      numberOfEmptySides += 1;
    }
    if (getNumberOfCandidatesForSide(TOP) == 0) {
      numberOfEmptySides += 1;
    }
    if (getNumberOfCandidatesForSide(BOTTOM) == 0) {
      numberOfEmptySides += 1;
    }

    return numberOfEmptySides == 2;
  }

  public void addCandidate(Side side, Tile candidate) {
    candidatesBySide.put(side, candidate);
  }

  public void removeCandidate(Side side, Tile candidate) {
    if (candidatesBySide.get(side).contains(candidate)) {
      candidatesBySide.remove(side, candidate);
      if (side == LEFT) {
        candidate.removeCandidate(RIGHT, this);
      } else if (side == RIGHT) {
        candidate.removeCandidate(LEFT, this);
      } else if (side == TOP) {
        candidate.removeCandidate(BOTTOM, this);
      } else if (side == BOTTOM) {
        candidate.removeCandidate(TOP, this);
      }
    }
  }

  public void keepCandidate(Side side, Tile candidate) {
    Set<Tile> others = new HashSet<>(candidatesBySide.get(side));
    others.remove(candidate);
    candidatesBySide.get(side).removeAll(others);

    for (Tile other : others) {
      if (side == LEFT) {
        other.removeCandidate(RIGHT, this);
      } else if (side == RIGHT) {
        other.removeCandidate(LEFT, this);
      } else if (side == TOP) {
        other.removeCandidate(BOTTOM, this);
      } else if (side == BOTTOM) {
        other.removeCandidate(TOP, this);
      }
    }
  }

  public boolean matchesSideToLeftSide(Side selfSide, Tile possibleMatchTile) {
    return matchesFromSideToTileSide(selfSide, possibleMatchTile, LEFT)
        || matchesFromSideToTileSide(selfSide, possibleMatchTile, LEFT_FLIPPED);
  }

  public boolean matchesSideToTopSide(Side selfSide, Tile possibleMatchTile) {
    return matchesFromSideToTileSide(selfSide, possibleMatchTile, TOP)
        || matchesFromSideToTileSide(selfSide, possibleMatchTile, TOP_FLIPPED);
  }

  public boolean matchesSideToBottomSide(Side selfSide, Tile possibleMatchTitle) {
    return matchesFromSideToTileSide(selfSide, possibleMatchTitle, BOTTOM)
        || matchesFromSideToTileSide(selfSide, possibleMatchTitle, BOTTOM_FLIPPED);
  }

  public boolean matchesSideToRightSide(Side selfSide, Tile possibleMatchTile) {
    return matchesFromSideToTileSide(selfSide, possibleMatchTile, RIGHT)
        || matchesFromSideToTileSide(selfSide, possibleMatchTile, RIGHT_FLIPPED);
  }

  public boolean matchesFromSideToTileSide(Side selfSide, Tile possibleMatchTile, Side tileSide) {
    if (selfSide == tileSide && this == possibleMatchTile) {
      return false;
    }

    char[][] rightTileData = possibleMatchTile.data;
    if (rightTileData.length != data.length) {
      return false;
    }

    boolean matches = true;
    for (int row = 0; row < data.length && matches; row++) {
      int selfRow = selfSide.getRow(data.length, row);
      int tileRow = tileSide.getRow(data.length, row);
      if (rightTileData[tileRow].length != data[selfRow].length) {
        return false;
      }

      int selfColumn = selfSide.getColumn(data[selfRow].length, row);
      int tileColumn = tileSide.getColumn(rightTileData[tileRow].length, row);
      matches &= data[selfRow][selfColumn] == rightTileData[tileRow][tileColumn];
    }

    return matches;
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
