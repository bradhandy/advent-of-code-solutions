package com.dbjgb.advent._2020.puzzle._20;

import java.util.EnumSet;
import java.util.Map;

public enum Side {

  LEFT(Map.of(Rotation.LEFT, "BOTTOM", Rotation.RIGHT, "TOP", Flip.HORIZONTAL, "RIGHT", Flip.VERTICAL, "LEFT")) {
    @Override
    public String sideValue(char[][] grid) {
      StringBuilder value = new StringBuilder();
      for (int i = 0; i < grid.length; i++) {
        value.append(grid[i][0]);
      }

      return value.toString();
    }
  },
  TOP(Map.of(Rotation.LEFT, "LEFT", Rotation.RIGHT, "RIGHT", Flip.HORIZONTAL, "TOP", Flip.VERTICAL, "BOTTOM")) {
    @Override
    public String sideValue(char[][] grid) {
      StringBuilder value = new StringBuilder();
      for (int i = 0; i < grid[0].length; i++) {
        value.append(grid[0][(grid[0].length - 1) - i]);
      }

      return value.toString();
    }
  },
  RIGHT(Map.of(Rotation.LEFT, "TOP", Rotation.RIGHT, "BOTTOM", Flip.HORIZONTAL, "LEFT", Flip.VERTICAL, "RIGHT")) {
    @Override
    public String sideValue(char[][] grid) {
      StringBuilder value = new StringBuilder();
      for (int i = 0; i < grid.length; i++) {
        value.append(grid[(grid.length - 1) - i][grid[0].length - 1]);
      }

      return value.toString();
    }
  },
  BOTTOM(Map.of(Rotation.LEFT, "RIGHT", Rotation.RIGHT, "LEFT", Flip.HORIZONTAL, "BOTTOM", Flip.VERTICAL, "TOP")) {
    @Override
    public String sideValue(char[][] grid) {
      StringBuilder value = new StringBuilder();
      for (int i = 0; i < grid[grid.length - 1].length; i++) {
        value.append(grid[grid.length - 1][i]);
      }
      return value.toString();
    }
  };

  private static final Map<String, Side> NAME_TO_SIDE = Map.of(LEFT.name(), LEFT, RIGHT.name(), RIGHT, TOP.name(), TOP, BOTTOM.name(), BOTTOM);
  private static final EnumSet<Side> ALL_SIDES = EnumSet.allOf(Side.class);

  public static EnumSet<Side> getAllSides() {
    return ALL_SIDES;
  }

  private final Map<CoordinateTranslator, String> transformations;

  Side(Map<CoordinateTranslator, String> transformations) {
    this.transformations = transformations;
  }

  public Side translate(CoordinateTranslator coordinateTranslator) {
    return NAME_TO_SIDE.get(transformations.get(coordinateTranslator));
  }

  public Side oppositeSide() {
    Flip flip = transformations.get(Flip.HORIZONTAL).equals(name()) ? Flip.VERTICAL : Flip.HORIZONTAL;
    return NAME_TO_SIDE.get(transformations.get(flip));
  }

  public Flip flipDirection() {
    return transformations.get(Flip.HORIZONTAL).equals(name()) ? Flip.HORIZONTAL : Flip.VERTICAL;
  }

  public abstract String sideValue(char[][] grid);
}
