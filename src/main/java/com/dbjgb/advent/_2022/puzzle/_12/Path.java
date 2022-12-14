package com.dbjgb.advent._2022.puzzle._12;

import java.util.LinkedHashSet;
import java.util.Set;

public class Path {

  private final Set<Cell> visited;

  private Cell current;

  public Path(Cell current, Set<Cell> visited) {
    this.visited = new LinkedHashSet<>(visited);
    this.current = current;
  }


}
