package com.dbjgb.advent._2022.puzzle._12;

import com.dbjgb.advent.Utility;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;
import org.xguzm.pathfinding.grid.heuristics.EuclideanDistance;

import java.util.ArrayList;
import java.util.List;

public class Solution {

  public static void main(String... args) throws Exception {
    List<String> gridLines = Utility.readAllLines("_2022/puzzle/_12/input.txt");
    List<Cell> lowestElevations = new ArrayList<>();
    Cell[][] cells = new Cell[gridLines.size()][];
    int startX = 0;
    int startY = 0;
    int endX = 0;
    int endY = 0;

    for (int row = 0; row < gridLines.size(); row++) {
      String line = gridLines.get(row);
      cells[row] = new Cell[line.length()];
      for (int column = 0; column < cells[row].length; column++) {
        char height = line.charAt(column);
        if (height == 'S') {
          startX = row;
          startY = column;
        } else if (height == 'E') {
          endX = row;
          endY = column;
        }

        Cell cell = new Cell(row, column, height);
        cells[row][column] = cell;
        if (cell.getHeight() == 'a' || cell.getHeight() == 'S') {
          lowestElevations.add(cell);
        }
      }
    }

    Grid grid = new Grid(cells);
    GridFinderOptions options =
        new GridFinderOptions(false, true, new EuclideanDistance(), true, 0.0f, 0.0f);
    AStarGridFinder<Cell> finder = new AStarGridFinder<>(Cell.class, options);
    List<Cell> path = finder.findPath(startX, startY, endX, endY, grid);
    System.out.printf("Shortest path: %d\n", path.size());

    int fewestMoves = Integer.MAX_VALUE;
    Cell fewestMovesStart = null;
    for (Cell startingPoint : lowestElevations) {
      path = finder.findPath(startingPoint.getX(), startingPoint.getY(), endX, endY, grid);
      if (path == null) {
        continue;
      }

      int nextFewestMoves = Math.min(fewestMoves, path.size());
      if (nextFewestMoves != fewestMoves) {
        fewestMovesStart = startingPoint;
        fewestMoves = nextFewestMoves;
      }
    }

    System.out.printf(
        "Fewest Moves from %d by %d: %d\n",
        fewestMovesStart.getX(), fewestMovesStart.getY(), fewestMoves);
  }
}
