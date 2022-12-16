package com.dbjgb.advent._2022.puzzle._15;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SensorCoverage {

  private final Cell sensor;
  private final Cell beacon;

  private final Cell topCorner;
  private final Cell bottomCorner;

  private final Cell leftCorner;

  private final Cell rightCorner;

  public SensorCoverage(int sensorRow, int sensorColumn, int beaconRow, int beaconColumn) {
    this.sensor = new Cell(sensorRow, sensorColumn);
    this.beacon = new Cell(beaconRow, beaconColumn);

    int manhattanDistance = getManhattanDistance(sensor, beacon);
    this.topCorner = new Cell(sensor.getRow() - manhattanDistance, sensor.getColumn());
    this.bottomCorner = new Cell(sensor.getRow() + manhattanDistance, sensor.getColumn());
    this.leftCorner = new Cell(sensor.getRow(), sensor.getColumn() - manhattanDistance);
    this.rightCorner = new Cell(sensor.getRow(), sensor.getColumn() + manhattanDistance);
  }

  public Cell getBeacon() {
    return beacon;
  }

  private int getManhattanDistance(Cell start, Cell destination) {
    return Math.abs(start.getRow() - destination.getRow())
        + Math.abs(start.getColumn() - destination.getColumn());
  }

  public Set<Cell> locationsCoveredInGrid(int row, int origin, int rightCorner) {
    Cell middleCellInRow = new Cell(row, sensor.getColumn());
    if (!cellInCoverageRange(middleCellInRow)) {
      return Set.of();
    }

    if (topCorner.getRow() == row || bottomCorner.getRow() == row) {
      return Set.of((topCorner.getRow() == row) ? topCorner : bottomCorner);
    }

    Set<Cell> cellsInRow = new HashSet<>(Set.of(middleCellInRow));
    for (int manhattanDistanceFromCenter =
            getManhattanDistance(sensor, bottomCorner)
                - getManhattanDistance(sensor, middleCellInRow);
        manhattanDistanceFromCenter > 0;
        manhattanDistanceFromCenter--) {
      cellsInRow.add(
          new Cell(
              middleCellInRow.getRow(), middleCellInRow.getColumn() + manhattanDistanceFromCenter));
      cellsInRow.add(
          new Cell(
              middleCellInRow.getRow(), middleCellInRow.getColumn() - manhattanDistanceFromCenter));
    }

    return cellsInRow.stream()
        .filter(
            cell ->
                cell.getRow() >= origin
                    && cell.getColumn() > origin
                    && cell.getRow() <= rightCorner
                    && cell.getColumn() <= rightCorner)
        .collect(Collectors.toSet());
  }

  public Range rangeCoveredInRowInGrid(int row, int origin, int rightCorner) {
    Cell middleCellInRow = new Cell(row, sensor.getColumn());
    if (!cellInCoverageRange(middleCellInRow)) {
      return null;
    }

    int manhattanDistanceFromCenter =
        getManhattanDistance(sensor, bottomCorner) - getManhattanDistance(sensor, middleCellInRow);
    int left = sensor.getColumn() - manhattanDistanceFromCenter;
    int right = sensor.getColumn() + manhattanDistanceFromCenter;

    return new Range(Math.max(left, origin), Math.min(right, rightCorner));
  }

  public boolean cellInCoverageRange(Cell candidate) {
    return topCorner.getRow() <= candidate.getRow()
        && candidate.getRow() <= bottomCorner.getRow()
        && leftCorner.getColumn() <= candidate.getColumn()
        && candidate.getColumn() <= rightCorner.getColumn();
  }
}
