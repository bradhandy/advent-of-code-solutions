package com.dbjgb.advent._2015.puzzle._21;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class Item {

  private final String name;
  private final int cost;
  private final int damage;
  private final int defense;

  public Item(
      @JsonProperty("name") String name,
      @JsonProperty("cost") int cost,
      @JsonProperty("damage") int damage,
      @JsonProperty("defense") int defense) {
    this.name = name;
    this.cost = cost;
    this.damage = damage;
    this.defense = defense;
  }

  public String getName() {
    return name;
  }

  public int getCost() {
    return cost;
  }

  public int getDamage() {
    return damage;
  }

  public int getDefense() {
    return defense;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Item item = (Item) o;
    return Objects.equal(name, item.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
}
