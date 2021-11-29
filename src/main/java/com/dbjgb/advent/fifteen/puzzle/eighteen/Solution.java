package com.dbjgb.advent.fifteen.puzzle.eighteen;

import com.dbjgb.advent.Utility;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {

  private static final int STEPS = 100;
  private static final int GRID_SIZE = 100;

  public static void main(String... args) throws Exception {
    List<Light> lightGrid = Lists.newArrayList();
    List<Light> updatedGrid = Lists.newArrayList();
    Set<Integer> cornerPositions =
        Arrays.stream(Corner.values())
            .map((corner) -> corner.getPositionInGrid(GRID_SIZE))
            .collect(Collectors.toSet());
    boolean cornersStuckOn = true;

    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/eighteen/input.txt")) {
      String line;
      while ((line = inputReader.readLine()) != null) {
        for (char lightState : line.toCharArray()) {
          Light light = new Light(lightGrid.size(), lightState == '#');
          if (cornersStuckOn && cornerPositions.contains(light.getPosition())) {
            light = light.turnOn();
          }
          lightGrid.add(light);
        }
      }
    }

    for (int step = 1; step <= STEPS; step++) {
      //System.out.printf("Step %d:\n", step);
      for (Light light : lightGrid) {
        long neighborsOn =
            NeighborLocation.findNeighborsInSquareGrid(light, lightGrid, GRID_SIZE).stream()
                .filter(Light::isLit)
                .count();
        Light updatedLight = light.turnOff();
        if ((light.isLit() && (neighborsOn == 2 || neighborsOn == 3))
            || (!light.isLit() && neighborsOn == 3)
            || (cornersStuckOn && cornerPositions.contains(light.getPosition()))) {
          updatedLight = light.turnOn();
        }
        updatedGrid.add(updatedLight);
        //        System.out.print(updatedLight.isLit() ? '#' : '.');
        //        if ((light.getPosition() + 1) % GRID_SIZE == 0) {
        //          System.out.print('\n');
        //        }
      }

      //System.out.println("\n");

      lightGrid = updatedGrid;
      updatedGrid = Lists.newArrayList();
    }

    long numberOfLightsOn = lightGrid.stream().filter(Light::isLit).count();
    System.out.printf("There are %d lights on.", numberOfLightsOn);
  }

  private enum NeighborLocation {
    NORTH_WEST(-1, -1),
    NORTH(-1, 0),
    NORTH_EAST(-1, 1),
    WEST(0, -1),
    EAST(0, 1),
    SOUTH_WEST(1, -1),
    SOUTH(1, 0),
    SOUTH_EAST(1, 1);

    private final int deltaRow;
    private final int deltaColumn;

    NeighborLocation(int deltaRow, int deltaColumn) {
      this.deltaRow = deltaRow;
      this.deltaColumn = deltaColumn;
    }

    static List<Light> findNeighborsInSquareGrid(Light light, List<Light> lights, int gridSize) {
      int row = light.getPosition() / gridSize;
      int column = light.getPosition() - (row * gridSize);

      List<Light> neighbors = Lists.newArrayList();
      for (NeighborLocation location : NeighborLocation.values()) {
        int newRow = row + location.deltaRow;
        int newColumn = column + location.deltaColumn;

        if (newRow < 0 || newColumn < 0 || newRow >= gridSize || newColumn >= gridSize) {
          neighbors.add(NoLight.INSTANCE);
        } else {
          neighbors.add(lights.get((newRow * gridSize) + newColumn));
        }
      }

      return neighbors;
    }
  }

  public enum Corner {
    NORTH_EAST(0, 0),
    NORTH_WEST(0, -1),
    SOUTH_WEST(-1, 0),
    SOUTH_EAST(-1, -1);

    private final int deltaHeight;
    private final int deltaWidth;

    Corner(int deltaHeight, int deltaWidth) {
      this.deltaHeight = deltaHeight;
      this.deltaWidth = deltaWidth;
    }

    int getPositionInGrid(int gridSize) {
      int row = (gridSize + deltaHeight) % gridSize;
      int column = (gridSize + deltaWidth) % gridSize;

      return (row * gridSize) + column;
    }
  }

  private static class Light {
    private final int position;
    private final boolean lit;

    public Light(int position, boolean lit) {
      this.position = position;
      this.lit = lit;
    }

    public int getPosition() {
      return position;
    }

    public Light turnOn() {
      return (lit) ? this : new Light(position, true);
    }

    public Light turnOff() {
      return (!lit) ? this : new Light(position, false);
    }

    public boolean isLit() {
      return lit;
    }
  }

  private static class NoLight extends Light {

    private static final NoLight INSTANCE = new NoLight();

    public NoLight() {
      super(-1, false);
    }

    @Override
    public Light turnOn() {
      return this;
    }

    @Override
    public Light turnOff() {
      return this;
    }
  }
}
