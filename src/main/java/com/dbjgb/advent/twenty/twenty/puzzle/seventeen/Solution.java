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

      /* To eliminate the "w" dimension for part 1, set the first argument to zero. */
      grid.expand(2, 2, 2, 2);
      grid.applyRules();
    }

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

  private char[][][][] grid;

  public ConwayCubeGrid(char[][] initialLayer) {
    char[][][][] initialGrid = new char[1][1][][];
    initialGrid[0][0] = initialLayer;

    this.grid = initialGrid;
  }

  public void printGrid() {
    int originalW = grid.length / 2;
    for (int w = 0; w < grid.length; w++) {
      int wLayer = w - originalW;
      int originalLayer = grid[w].length / 2;
      for (int z = 0; z < grid[w].length; z++) {
        int layer = z - originalLayer;
        System.out.printf("Layer (z=%d;w=%d):\n", layer, wLayer);
        for (int y = 0; y < grid[w][z].length; y++) {
          for (int x = 0; x < grid[w][z][y].length; x++) {
            System.out.printf("%c", grid[w][z][y][x]);
          }
          System.out.println();
        }
      }
    }
  }

  public void expand(int deltaW, int deltaLayers, int deltaRows, int deltaColumns) {
    char[][][][] expandedGrid = new char[grid.length + deltaW][][][];
    for (int w = 0; w < expandedGrid.length; w++) {
      expandedGrid[w] = new char[grid[0].length + deltaLayers][][];
      for (int z = 0; z < expandedGrid[w].length; z++) {
        expandedGrid[w][z] = new char[grid[0][0].length + deltaRows][];
        char[][] layer = expandedGrid[w][z];
        for (int y = 0; y < layer.length; y++) {
          layer[y] = new char[grid[0][0][0].length + deltaColumns];
          Arrays.fill(layer[y], '.');
        }

        int wOffset = deltaW / 2;
        if (0 <= (w - wOffset) && (w - wOffset) < grid.length) {
          if (1 <= z && z < (expandedGrid[w].length - 1)) {
            for (int y = 0; y < grid[w - wOffset][z - 1].length; y++) {
              int offset = deltaRows / 2;
              System.arraycopy(
                  grid[w - wOffset][z - 1][y],
                  0,
                  layer[y + offset],
                  offset,
                  grid[w - wOffset][z - 1][y].length);
            }
          }
        }
        expandedGrid[w][z] = layer;
      }
    }

    this.grid = expandedGrid;
  }

  public void applyRules() {
    char[][][][] updatedGrid = new char[grid.length][][][];
    for (int w = 0; w < grid.length; w++) {
      updatedGrid[w] = new char[grid[w].length][][];
      for (int z = 0; z < grid[w].length; z++) {
        updatedGrid[w][z] = new char[grid[w][z].length][];
        for (int y = 0; y < grid[w][z].length; y++) {
          updatedGrid[w][z][y] = Arrays.copyOf(grid[w][z][y], grid[w][z][y].length);
          for (int x = 0; x < grid[w][z][y].length; x++) {
            int activeNeighborCount = activeNeighborCount(x, y, z, w);
            if (grid[w][z][y][x] == '.' && activeNeighborCount == 3) {
              updatedGrid[w][z][y][x] = '#';
            } else if (grid[w][z][y][x] == '#' && (activeNeighborCount < 2 || 3 < activeNeighborCount)) {
              updatedGrid[w][z][y][x] = '.';
            }
          }
        }
      }
    }

    this.grid = updatedGrid;
  }

  public int geActiveCount() {
    int activeCount = 0;
    for (int w = 0; w < grid.length; w++) {
      for (int z = 0; z < grid[w].length; z++) {
        for (int y = 0; y < grid[w][z].length; y++) {
          for (int x = 0; x < grid[w][z][y].length; x++) {
            if (grid[w][z][y][x] == '#') {
              activeCount += 1;
            }
          }
        }
      }
    }

    return activeCount;
  }

  private int activeNeighborCount(int x, int y, int z, int w) {
    return activeNeighborCoordinatesOnPlane(x, y - 1, z, w)
        + activeNeighborCoordinatesOnSamePlane(x, y, z, w)
        + activeNeighborCoordinatesOnPlane(x, y + 1, z, w);
  }

  private int activeNeighborCoordinatesOnSamePlane(int x, int y, int z, int w) {
    int resultDelta = grid[w][z][y][x] == '#' ? -1 : 0;
    return resultDelta + activeNeighborCoordinatesOnPlane(x, y, z, w);
  }

  private int activeNeighborCoordinatesOnPlane(int x, int y, int z, int w) {
    int totalActiveOnPlane = 0;
    for (int currentW = w - 1; currentW <= w + 1; currentW++) {
      if (currentW < 0 || currentW >= grid.length) {
        continue;
      }

      for (int currentZ = z - 1; currentZ <= z + 1; currentZ++) {
        if (currentZ < 0 || currentZ >= grid[w].length || y < 0 || y >= grid[w][z].length) {
          continue;
        }

        for (int currentX = x - 1; currentX <= x + 1; currentX++) {
          if (currentX < 0 || currentX >= grid[w][z][y].length) {
            continue;
          }

          if (grid[currentW][currentZ][y][currentX] == '#') {
            totalActiveOnPlane += 1;
          }
        }
      }

    }

    return totalActiveOnPlane;
  }
}
