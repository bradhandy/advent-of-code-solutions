package com.dbjgb.advent._2022.puzzle._12;

import org.xguzm.pathfinding.PathFinderOptions;
import org.xguzm.pathfinding.grid.NavigationGrid;

import java.util.List;

public class Grid extends NavigationGrid<Cell> {

  private final ThreadLocal<Cell> currentCell;

  public Grid(Cell[][] nodes) {
    super(nodes, false);
    this.currentCell = new ThreadLocal<>();
  }

  @Override
  public List<Cell> getNeighbors(Cell node, PathFinderOptions opt) {
    currentCell.set(node);
    return super.getNeighbors(node, opt);
  }

  @Override
  public boolean isWalkable(Cell node) {
    return node.reachableFrom(currentCell.get());
  }

  @Override
  public boolean isWalkable(int x, int y) {
    return this.contains(x, y) && this.nodes[x][y].reachableFrom(currentCell.get());
  }
}
