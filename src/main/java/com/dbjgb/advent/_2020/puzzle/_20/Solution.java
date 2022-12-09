package com.dbjgb.advent._2020.puzzle._20;

import com.dbjgb.advent.Utility;
import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern TILE_ID_PATTERN = Pattern.compile("Tile (\\d+):");
  private static final List<Cell> SEA_MONSTER_CELLS =
      List.of(
          new Cell(0, 18),
          new Cell(1, 0),
          new Cell(1, 5),
          new Cell(1, 6),
          new Cell(1, 11),
          new Cell(1, 12),
          new Cell(1, 17),
          new Cell(1, 18),
          new Cell(1, 19),
          new Cell(2, 1),
          new Cell(2, 4),
          new Cell(2, 7),
          new Cell(2, 10),
          new Cell(2, 13),
          new Cell(2, 16));

  public static void main(String... args) throws Exception {
    Grid grid = new Grid(parseTiles());

    long idProduct = 1;
    for (Tile tile : grid.getCorners()) {
      idProduct *= tile.getId();
    }

    System.out.printf("ID Product:  %d\n", idProduct);

    // grid.printGrid();

    char[][] concatenatedGrid = grid.concatenate(false);
    int seaMonstersFound = findSeaMonsters(concatenatedGrid);
    int seaMonsterCellsTaken = seaMonstersFound * SEA_MONSTER_CELLS.size();
    int waves = 0;
    for (int row = 0; row < concatenatedGrid.length; row++) {
      for (int column = 0; column < concatenatedGrid[row].length; column++) {
        if (concatenatedGrid[row][column] == '#') {
          waves += 1;
        }
      }
    }

    System.out.printf("Sea roughness is %d.\n", waves - seaMonsterCellsTaken);
  }

  private static List<Tile> parseTiles() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("_2020/puzzle/_20/input.txt")) {
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

  private static int findSeaMonsters(char[][] picture) {
    char[][] photo = picture;
    int lengthFurthestRight =
        photo[0].length
            - SEA_MONSTER_CELLS.stream().mapToInt(Cell::getColumn).reduce(0, Math::max);
    int lengthFurthestDown =
        photo.length - SEA_MONSTER_CELLS.stream().mapToInt(Cell::getRow).reduce(0, Math::max);

    int seaMonstersFound = 0;
    int rotations = 0;
    int flips = 0;
    do {
      for (int row = 0; row < lengthFurthestDown; row++) {
        for (int column = 0; column < lengthFurthestRight; column++) {
          boolean found = true;
          for (Cell cell : SEA_MONSTER_CELLS) {
            Cell seaMonsterBodyCell = new Cell(cell.getRow() + row, cell.getColumn() + column);
            found &= photo[seaMonsterBodyCell.getRow()][seaMonsterBodyCell.getColumn()] == '#';
          }

          if (found) {
            seaMonstersFound += 1;
          }
        }
      }

      if (rotations < 4) {
        photo = Rotation.LEFT.applyTo(photo);
        rotations += 1;
        if (flips < 1 && rotations == 4) {
          photo = Flip.VERTICAL.applyTo(photo);
          rotations = 0;
          flips += 1;
        }
      }
    } while (seaMonstersFound == 0 || (flips < 1 && rotations < 4));

    return seaMonstersFound;
  }
}
