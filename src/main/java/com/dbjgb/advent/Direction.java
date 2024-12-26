package com.dbjgb.advent;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public enum Direction {
  UP(new Point(0, -1)), RIGHT(new Point(1, 0)), DOWN(new Point(0, 1)), LEFT(new Point(-1, 0));

  private static final Direction[] DIRECTIONS = values();
  private static final Map<String, Direction> DIRECTIONS_BY_NAME = Maps.uniqueIndex(List.of(values()), Direction::toString);

  public static Direction byName(String name) {
    return DIRECTIONS_BY_NAME.get(name);
  }

  private final Point deltaPoint;

  Direction(Point deltaPoint) {
    this.deltaPoint = deltaPoint;
  }

  public Point forward(Point start) {
    return new Point(start.getX() + deltaPoint.getX(), start.getY() + deltaPoint.getY());
  }

  public Point reverse(Point start) {
    Point reverseDelta = new Point(deltaPoint.getX() * -1, deltaPoint.getY() * -1);
    return new Point(start.getX() + reverseDelta.getX(), start.getY() + reverseDelta.getY());
  }
}
