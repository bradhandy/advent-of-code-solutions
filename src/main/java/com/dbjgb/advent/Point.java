package com.dbjgb.advent;

import java.util.Comparator;
import java.util.Objects;

public class Point {

  public static final Comparator<Point> ORDER_BY_Y = Comparator.comparing(Point::getY).thenComparing(Point::getX);
  public static final Comparator<Point> ORDER_BY_X = Comparator.comparing(Point::getX).thenComparing(Point::getY);

  private final int x;
  private final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean inGrid(int width, int height) {
    return x >= 0 && x < width && y >= 0 && y < height;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Point point)) {
      return false;
    }
    return x == point.x && y == point.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
