package com.dbjgb.advent._2022.puzzle._16;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Valve {

  private final String name;
  private final Set<Valve> neighbors;
  private final int flowRate;

  public Valve(String name, int flowRate) {
    this.name = name;
    this.flowRate = flowRate;
    this.neighbors = new HashSet<>();
  }

  public String getName() {
    return name;
  }

  public int getFlowRate() {
    return flowRate;
  }

  public Set<Valve> getNeighbors() {
    return Set.copyOf(neighbors);
  }

  public void addNeighbor(Valve valve) {
    if (!neighbors.contains(valve)) {
      neighbors.add(valve);
    }
    if (!valve.getNeighbors().contains(this)) {
      valve.addNeighbor(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Valve valve = (Valve) o;
    return name.equals(valve.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
