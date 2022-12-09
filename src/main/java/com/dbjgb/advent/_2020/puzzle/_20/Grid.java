package com.dbjgb.advent._2020.puzzle._20;

import com.google.common.collect.Iterables;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.dbjgb.advent._2020.puzzle._20.Side.BOTTOM;
import static com.dbjgb.advent._2020.puzzle._20.Side.LEFT;
import static com.dbjgb.advent._2020.puzzle._20.Side.RIGHT;
import static com.dbjgb.advent._2020.puzzle._20.Side.TOP;

public class Grid {

  private Set<Tile> tiles;
  private Set<Tile> unprocessedTiles;
  private Tile[][] tileLayout;

  Grid(List<Tile> tiles) {
    this.tiles = Set.copyOf(tiles);

    matchTiles();
  }

  public Set<Tile> getCorners() {
    int lastColumn = tileLayout[0].length - 1;
    int lastRow = tileLayout.length - 1;
    return Set.of(
        tileLayout[0][0],
        tileLayout[0][lastColumn],
        tileLayout[lastRow][0],
        tileLayout[lastRow][lastColumn]);
  }

  public Set<Tile> getTiles() {
    return tiles;
  }

  public char[][] concatenate(boolean retainBorders) {
    int gridSize = Iterables.get(tiles, 0).getData().length * tileLayout.length;
    int borders = retainBorders ? 0 : tileLayout.length * 2;
    int borderOffset = retainBorders ? 0 : 1;
    char[][] grid = new char[gridSize - borders][gridSize - borders];
    for (int row = 0; row < tileLayout.length; row++) {
      for (int dataRow = borderOffset;
          dataRow < tileLayout[row][0].getData().length - borderOffset;
          dataRow++) {
        for (int column = 0; column < tileLayout[row].length; column++) {
          int dataAreaLength = tileLayout[row][0].getData().length - (borderOffset * 2);
          int gridRow = (dataRow - borderOffset) + (row * dataAreaLength);
          System.arraycopy(
              tileLayout[row][column].getData()[dataRow],
              borderOffset,
              grid[gridRow],
              column * dataAreaLength,
              dataAreaLength);
        }
      }
    }

    return grid;
  }

  public void printGrid() {
    for (int row = 0; row < tileLayout.length; row++) {
      for (int dataRow = 0; dataRow < tileLayout[row][0].getData().length; dataRow++) {
        for (int column = 0; column < tileLayout[row].length; column++) {
          System.out.print(tileLayout[row][column].getData()[dataRow]);
          System.out.print(' ');
        }
        System.out.print('\n');
      }
      System.out.print('\n');
    }
  }

  private void matchTiles() {
    this.unprocessedTiles = new HashSet<>(tiles);
    Tile firstTile = Iterables.getFirst(this.unprocessedTiles, null);
    assert firstTile != null : "There should be a tile.";

    unprocessedTiles.remove(firstTile);

    matchTilesInDirectionStartingWith(RIGHT, firstTile);
    matchTilesInDirectionStartingWith(LEFT, firstTile);
    matchTilesInDirectionStartingWith(TOP, firstTile);
    matchTilesInDirectionStartingWith(BOTTOM, firstTile);
    matchTilesInRowInDirectionRelativeTo(TOP, firstTile);
    matchTilesInRowInDirectionRelativeTo(BOTTOM, firstTile);

    fillGrid(firstTile);
  }

  private void fillGrid(Tile firstTile) {
    int gridSize = (int) Math.sqrt(tiles.size());
    this.tileLayout = new Tile[gridSize][gridSize];
    int row = getConnectionsInDirection(firstTile, TOP);
    int column = getConnectionsInDirection(firstTile, LEFT);

    tileLayout[row][column] = firstTile;
    Cell currentCell = new Cell(row, column);
    fillRowInDirectionRelativeTo(LEFT, firstTile, currentCell, 0, -1);
    fillRowInDirectionRelativeTo(RIGHT, firstTile, currentCell, 0, 1);
    fillRowsInDirectionRelativeTo(TOP, firstTile, currentCell, -1, 0);
    fillRowsInDirectionRelativeTo(BOTTOM, firstTile, currentCell, 1, 0);
  }

  private void fillRowsInDirectionRelativeTo(
      Side direction, Tile relativeTile, Cell relativeCell, int deltaRow, int deltaColumn) {
    Tile currentTile = relativeTile;
    Cell currentCell = relativeCell;
    while ((currentTile = currentTile.getTileOnSide(direction)) != null) {
      currentCell =
          new Cell(currentCell.getRow() + deltaRow, currentCell.getColumn() + deltaColumn);
      tileLayout[currentCell.getRow()][currentCell.getColumn()] = currentTile;
      fillRowInDirectionRelativeTo(LEFT, currentTile, currentCell, 0, -1);
      fillRowInDirectionRelativeTo(RIGHT, currentTile, currentCell, 0, 1);
    }
  }

  private void fillRowInDirectionRelativeTo(
      Side direction, Tile relativeTile, Cell relativeCell, int deltaRow, int deltaColumn) {
    Tile currentTile = relativeTile;
    Cell currentCell = relativeCell;
    while ((currentTile = currentTile.getTileOnSide(direction)) != null) {
      currentCell =
          new Cell(currentCell.getRow() + deltaRow, currentCell.getColumn() + deltaColumn);
      tileLayout[currentCell.getRow()][currentCell.getColumn()] = currentTile;
    }
  }

  private int getConnectionsInDirection(Tile startingTile, Side direction) {
    int numberOfConnections = 0;
    Tile currentTile = startingTile;
    while ((currentTile = currentTile.getTileOnSide(direction)) != null) {
      numberOfConnections += 1;
    }

    return numberOfConnections;
  }

  private void matchTilesInRowInDirectionRelativeTo(Side direction, Tile relativeStartTile) {
    Tile currentTile = relativeStartTile;
    while ((currentTile = currentTile.getTileOnSide(direction)) != null) {
      matchTilesInDirectionStartingWith(RIGHT, currentTile);
      matchTilesInDirectionStartingWith(LEFT, currentTile);
    }
  }

  private void matchTilesInDirectionStartingWith(Side direction, Tile firstTile) {
    Tile currentTile = firstTile;
    Iterator<Tile> unprocessedTileIterator = unprocessedTiles.iterator();
    while (unprocessedTileIterator.hasNext()) {
      Tile unmatchedTile = unprocessedTileIterator.next();
      for (Side unmatchedSide : Side.getAllSides()) {
        if (currentTile.matchesFromSideToTileSide(direction, unmatchedTile, unmatchedSide)) {
          assert currentTile.getTileOnSide(direction) == null : "Already matched.";
          currentTile.addCandidate(direction, unmatchedTile);
          unmatchedTile.orientSideToTileSide(unmatchedSide, currentTile, direction);
          currentTile = unmatchedTile;
          unprocessedTileIterator.remove();
          unprocessedTileIterator = unprocessedTiles.iterator();
        }
      }
    }
  }
}
