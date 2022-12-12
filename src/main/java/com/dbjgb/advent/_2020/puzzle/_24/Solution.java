package com.dbjgb.advent._2020.puzzle._24;

import com.dbjgb.advent.Utility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern DIRECTION_PATTERN = Pattern.compile("([sn]?[we])");

  public static void main(String... args) throws Exception {
    List<String> instructions = Utility.readAllLines("_2020/puzzle/_24/input.txt");
    Tile referenceTile = new Tile(0, 0);
    Map<Tile, Tile> tiles = new TreeMap<>();

    tiles.put(referenceTile, referenceTile);

    for (String instruction : instructions) {
      Matcher instructionMatcher = DIRECTION_PATTERN.matcher(instruction);
      Tile targetTile = referenceTile;

      while (instructionMatcher.find()) {
        Direction direction = Direction.valueOf(instructionMatcher.group(1).toUpperCase());
        targetTile = targetTile.getNeighbor(direction);
        if (!tiles.containsKey(targetTile)) {
          tiles.put(targetTile, targetTile);
        }
      }

      targetTile = tiles.get(targetTile);

      targetTile.flip();
    }

    fillOutGrid(tiles);
    associateNeighbors(tiles);

    System.out.printf("Total Tiles: %d\n", tiles.values().size());
    System.out.printf(
        "Max Row: %d\n", tiles.values().stream().mapToInt(Tile::getRow).reduce(0, Math::max));
    System.out.printf(
        "Max Column: %d\n", tiles.values().stream().mapToInt(Tile::getColumn).reduce(0, Math::max));
    System.out.printf(
        "Min Row: %d\n", tiles.values().stream().mapToInt(Tile::getRow).reduce(0, Math::min));
    System.out.printf(
        "Min Column: %d\n", tiles.values().stream().mapToInt(Tile::getColumn).reduce(0, Math::min));
    System.out.printf("Black Tiles: %d\n", tiles.values().stream().filter(Tile::isFlipped).count());

    for (int i = 0; i < 100; i++) {
      Set<Tile> tilesToFlip = new HashSet<>();
      for (Tile tile : tiles.keySet()) {
        long blackNeighbors = tile.getBlackNeighbors();
        if ((tile.isFlipped() && (blackNeighbors == 0 || blackNeighbors > 2))
            || (!tile.isFlipped() && blackNeighbors == 2)) {
          tilesToFlip.add(tile);
        }
      }
      tilesToFlip.forEach(Tile::flip);
    }

    System.out.printf("Black Tiles: %d\n", tiles.values().stream().filter(Tile::isFlipped).count());
  }

  private static void fillOutGrid(Map<Tile, Tile> tiles) {
    int maxRow = 1000;
    int minRow = -1000;
    int maxColumn = 1000;
    int minColumn = -1000;

    for (int row = minRow; row <= maxRow; row++) {
      int startColumn = Math.abs(row) % 2;
      int eastColumn = startColumn;
      int westColumn =
          (startColumn == 0) ? startColumn : startColumn + Direction.W.getDeltaColumn();

      while (eastColumn <= maxColumn && westColumn >= minColumn) {
        Tile eastTile = new Tile(row, eastColumn);
        if (!tiles.containsKey(eastTile)) {
          tiles.put(eastTile, eastTile);
        }

        Tile westTile = new Tile(row, westColumn);
        if (!tiles.containsKey(westTile)) {
          tiles.put(westTile, westTile);
        }

        eastColumn = eastTile.getNeighbor(Direction.E).getColumn();
        westColumn = westTile.getNeighbor(Direction.W).getColumn();
      }
    }
  }

  private static void associateNeighbors(Map<Tile, Tile> tiles) {
    tiles
        .keySet()
        .forEach(
            tile -> {
              Map<Direction, Tile> neighbors = new HashMap<>();
              Arrays.stream(Direction.values())
                  .forEach(
                      direction -> {
                        Tile neighbor = tile.getNeighbor(direction);
                        if (tiles.containsKey(neighbor)) {
                          neighbors.put(direction, tiles.get(neighbor));
                        }
                      });
              tile.setNeighbors(neighbors);
            });
  }
}
