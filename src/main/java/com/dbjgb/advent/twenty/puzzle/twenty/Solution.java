package com.dbjgb.advent.twenty.puzzle.twenty;

import com.dbjgb.advent.Utility;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dbjgb.advent.twenty.puzzle.twenty.Side.BOTTOM;
import static com.dbjgb.advent.twenty.puzzle.twenty.Side.LEFT;
import static com.dbjgb.advent.twenty.puzzle.twenty.Side.RIGHT;
import static com.dbjgb.advent.twenty.puzzle.twenty.Side.TOP;

public class Solution {

  private static final Pattern TILE_ID_PATTERN = Pattern.compile("Tile (\\d+):");

  public static void main(String... args) throws Exception {
    List<Tile> tiles = parseTiles();
    for (int i = 0; i < tiles.size(); i++) {
      Tile currentTile = tiles.get(i);

      for (int j = 0; j < tiles.size(); j++) {
        if (i == j) {
          continue;
        }

        Tile possibleMatch = tiles.get(j);
        for (Side side : Side.getAllSides()) {
          for (Side matchSide : Side.getAllSides()) {
            if (currentTile.matchesFromSideToTileSide(side, possibleMatch, matchSide)) {
              currentTile.addCandidate(side, possibleMatch);
              possibleMatch.addCandidate(matchSide, currentTile);
            }
          }
        }
      }
    }

    Tile startingTile = Iterables.getFirst(tiles, null);
    orientDirectionStartingWith(TOP, startingTile);
    orientDirectionStartingWith(RIGHT, startingTile);
    orientDirectionStartingWith(LEFT, startingTile);
    orientDirectionStartingWith(BOTTOM, startingTile);

    Tile currentTile = startingTile;
    while ((currentTile = currentTile.getTileOnSide(TOP)) != null) {
      orientDirectionStartingWith(RIGHT, currentTile);
      orientDirectionStartingWith(LEFT, currentTile);
    }

    currentTile = startingTile;
    while ((currentTile = currentTile.getTileOnSide(BOTTOM)) != null) {
      orientDirectionStartingWith(RIGHT, currentTile);
      orientDirectionStartingWith(LEFT, currentTile);
    }

    long idProduct = 1;
    for (Tile tile : tiles) {
      if (tile.hasTwoEmptySides()) {
        System.out.printf("corner: %d\n", tile.getId());
        for (Side side : Side.getAllSides()) {
          if (tile.getTileOnSide(side) == null) {
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

  private static void orientDirectionStartingWith(Side direction, Tile startingTile) {
    Tile tile = startingTile;
    while (tile.getTileOnSide(direction) != null) {
      Tile tileToOrient = tile.getTileOnSide(direction);
      tileToOrient.orientToTileSide(tile, direction);
      tile = tileToOrient;
    }
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

