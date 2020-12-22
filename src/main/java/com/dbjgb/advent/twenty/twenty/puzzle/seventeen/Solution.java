package com.dbjgb.advent.twenty.twenty.puzzle.seventeen;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {

  public static void main(String... args) throws Exception {
    ConwayCubeGrid grid = new ConwayCubeGrid(readInitialLayer());

    for (int expansion = 0; expansion < 6; expansion++) {
      grid.expand(2, 2, 2);
      grid.applyRules();
      grid.printGrid();
    }
    grid.printGrid();

    System.out.println(grid.geActiveCount());
  }

  private static char[][] readInitialLayer() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/seventeen/input.txt")) {
      List<char[]> initialLayer = new ArrayList<>();
      String line;
      while ((line = inputReader.readLine()) != null) {
        initialLayer.add(line.toCharArray());
      }

      return initialLayer.toArray(new char[initialLayer.size()][]);
    }
  }
}

class ConwayCubeGrid {

  private char[][][] grid;

  public ConwayCubeGrid(char[][] initialLayer) {
    char[][][] initialGrid = new char[1][][];
    initialGrid[0] = initialLayer;

    this.grid = initialGrid;
  }

  public void printGrid() {
    int originalLayer = grid.length / 2;
    for (int z = 0; z < grid.length; z++) {
      int layer = z - originalLayer;
      System.out.printf("Layer %d:\n", layer);
      for (int y = 0; y < grid[z].length; y++) {
        for (int x = 0; x < grid[z][y].length; x++) {
          System.out.printf("%c", grid[z][y][x]);
        }
        System.out.println();
      }
    }
  }

  public void expand(int deltaLayers, int deltaRows, int deltaColumns) {
    char[][][] expandedGrid = new char[grid.length + deltaLayers][][];
    for (int z = 0; z < expandedGrid.length; z++) {
      expandedGrid[z] = new char[grid[0].length + deltaRows][];
      char[][] layer = expandedGrid[z];
      for (int y = 0; y < layer.length; y++) {
        layer[y] = new char[grid[0][0].length + deltaColumns];
        Arrays.fill(layer[y], '.');
      }

      if (1 <= z && z < (expandedGrid.length - 1)) {
        for (int y = 0; y < grid[z - 1].length; y++) {
          int offset = deltaRows / 2;
          System.arraycopy(
              grid[z-1][y], 0, layer[y + offset], offset, grid[z-1][y].length);
        }
      }
      expandedGrid[z] = layer;
    }

    this.grid = expandedGrid;
  }

  public void applyRules() {
    char[][][] updatedGrid = new char[grid.length][][];
    for (int z = 0; z < grid.length; z++) {
      updatedGrid[z] = new char[grid[z].length][];
      for (int y = 0; y < grid[z].length; y++) {
        updatedGrid[z][y] = Arrays.copyOf(grid[z][y], grid[z][y].length);
        for (int x = 0; x < grid[z][y].length; x++) {
          int activeNeighborCount = activeNeighborCount(x, y, z);
          if (grid[z][y][x] == '.' && activeNeighborCount == 3) {
            updatedGrid[z][y][x] = '#';
          } else if (grid[z][y][x] == '#' && (activeNeighborCount < 2 || 3 < activeNeighborCount)) {
            updatedGrid[z][y][x] = '.';
          }
        }
      }
    }

    this.grid = updatedGrid;
  }

  public int geActiveCount() {
    int activeCount = 0;
    for (int z = 0; z < grid.length; z++) {
      for (int y = 0; y < grid[z].length; y++) {
        for (int x = 0; x < grid[z][y].length; x++ ) {
          if (grid[z][y][x] == '#') {
            activeCount += 1;
          }
        }
      }
    }

    return activeCount;
  }

  private int activeNeighborCount(int x, int y, int z) {
    return activeNeighborCoordinatesOnPlane(x, y - 1, z)
        + activeNeighborCoordinatesOnSamePlane(x, y, z)
        + activeNeighborCoordinatesOnPlane(x, y + 1, z);
  }

  private int activeNeighborCoordinatesOnSamePlane(int x, int y, int z) {
    int resultDelta = grid[z][y][x] == '#' ? -1 : 0;
    return resultDelta + activeNeighborCoordinatesOnPlane(x, y, z);
  }

  private int activeNeighborCoordinatesOnPlane(int x, int y, int z) {
    int totalActiveOnPlane = 0;
    for (int currentZ = z - 1; currentZ <= z + 1; currentZ++) {
      if (currentZ < 0 || currentZ >= grid.length || y < 0 || y >= grid[z].length) {
        continue;
      }

      for (int currentX = x - 1; currentX <= x + 1; currentX++) {
        if (currentX < 0 || currentX >= grid[z][y].length) {
          continue;
        }

        if (grid[currentZ][y][currentX] == '#') {
          totalActiveOnPlane += 1;
        }
      }
    }

    return totalActiveOnPlane;
  }
}
