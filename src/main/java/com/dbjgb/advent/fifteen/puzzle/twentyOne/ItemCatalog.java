package com.dbjgb.advent.fifteen.puzzle.twentyOne;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class ItemCatalog {

  private final List<Item> weapons;
  private final List<Item> armor;
  private final List<Item> rings;

  @JsonCreator
  public ItemCatalog(
      @JsonProperty("weapons") List<Item> weapons,
      @JsonProperty("armor") List<Item> armor,
      @JsonProperty("rings") List<Item> rings) {
    this.weapons = ImmutableList.copyOf(weapons);
    this.armor = ImmutableList.copyOf(armor);
    this.rings = ImmutableList.copyOf(rings);
  }

  public List<Item> getWeapons() {
    return weapons;
  }

  public List<Item> getArmor() {
    return armor;
  }

  public List<Item> getRings() {
    return rings;
  }
}
