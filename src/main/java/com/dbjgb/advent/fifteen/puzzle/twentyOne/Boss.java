package com.dbjgb.advent.fifteen.puzzle.twentyOne;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Boss {

  private final int damage;
  private final int defense;

  private int hitPoints;

  public Boss(
      @JsonProperty("damage") int damage,
      @JsonProperty("defense") int defense,
      @JsonProperty("hp") int hitPoints) {
    this.damage = damage;
    this.defense = defense;
    this.hitPoints = hitPoints;
  }

  public int getDamage() {
    return damage;
  }

  public int getDefense() {
    return defense;
  }

  public int getHitPoints() {
    return hitPoints;
  }

  public int defendAttack(int attackerDamage) {
    int damage = Math.max(1, attackerDamage - defense);
    hitPoints -= damage;

    return damage;
  }

}
