package com.dbjgb.advent._2015.puzzle._21;

import com.google.common.base.Objects;

import java.util.List;
import java.util.Optional;

public class Player {

  private final Item weapon;
  private final Item armor;
  private final Item leftHandRing;
  private final Item rightHandRing;

  private final int damage;
  private final int defense;
  private final int totalItemCost;

  private int hitPoints = 100;

  public Player(Item weapon, Item armor, Item leftHandRing, Item rightHandRing) {
    this.weapon = weapon;
    this.armor = armor;
    this.leftHandRing = leftHandRing;
    this.rightHandRing = rightHandRing;

    List<Optional<Item>> allItems =
        List.of(
            Optional.of(weapon),
            Optional.ofNullable(armor),
            Optional.ofNullable(leftHandRing),
            Optional.ofNullable(rightHandRing));
    this.damage =
        allItems.stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .mapToInt(Item::getDamage)
            .reduce(0, Integer::sum);
    this.defense =
        allItems.stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .mapToInt(Item::getDefense)
            .reduce(0, Integer::sum);
    this.totalItemCost =
        allItems.stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .mapToInt(Item::getCost)
            .reduce(0, Integer::sum);
  }

  public Item getWeapon() {
    return weapon;
  }

  public Optional<Item> getArmor() {
    return Optional.ofNullable(armor);
  }

  public Optional<Item> getLeftHandRing() {
    return Optional.ofNullable(leftHandRing);
  }

  public Optional<Item> getRightHandRing() {
    return Optional.ofNullable(rightHandRing);
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

  public int getTotalItemCost() {
    return totalItemCost;
  }

  public int defendAttack(int attackerDamage) {
    int damage = Math.max(1, attackerDamage - defense);
    hitPoints -= damage;

    return damage;
  }

  public Player removeArmor() {
    return new Player(weapon, null, leftHandRing, rightHandRing);
  }

  public Player removeLeftHandRing() {
    return new Player(weapon, armor, null, rightHandRing);
  }

  public Player removeRightHandRing() {
    return new Player(weapon, armor, leftHandRing, null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Player player = (Player) o;
    return Objects.equal(weapon, player.weapon)
        && Objects.equal(armor, player.armor)
        && Objects.equal(leftHandRing, player.leftHandRing)
        && Objects.equal(rightHandRing, player.rightHandRing);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(weapon, armor, leftHandRing, rightHandRing);
  }
}
