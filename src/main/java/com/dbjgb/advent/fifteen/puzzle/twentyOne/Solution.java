package com.dbjgb.advent.fifteen.puzzle.twentyOne;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.Set;

import static com.dbjgb.advent.Utility.openInputFile;

public class Solution {

  public static void main(String... args) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    ItemCatalog itemCatalog =
        objectMapper.readValue(
            openInputFile("fifteen/puzzle/twentyOne/items.json"), ItemCatalog.class);
    Boss boss =
        objectMapper.readValue(openInputFile("fifteen/puzzle/twentyOne/boss.json"), Boss.class);

    Set<Player> playerOptions = createPlayerOptions(itemCatalog);
    System.out.printf("Created %d player options.\n", playerOptions.size());

    playerOptions.stream()
        .filter(player -> playerWinsFightWithBoss(player, boss))
        .min(Ordering.natural().onResultOf(Player::getTotalItemCost))
        .ifPresentOrElse(
            player -> logPlayerResult(boss, player),
            () -> System.out.println("No players can beat the boss."));

    playerOptions.stream()
        .filter(player -> !playerWinsFightWithBoss(player, boss))
        .max(Ordering.natural().onResultOf(Player::getTotalItemCost))
        .ifPresentOrElse(
            player -> logPlayerResult(boss, player),
            () -> System.out.println("No players lose to the boss."));
  }

  private static void logPlayerResult(Boss boss, Player player) {
    System.out.printf("Player Item Cost: %d\n", player.getTotalItemCost());
    System.out.printf(
        "Weapon:  %s (%d)\n", player.getWeapon().getName(), player.getWeapon().getCost());
    player
        .getArmor()
        .ifPresent(item -> System.out.printf("Armor:  %s (%d)\n", item.getName(), item.getCost()));
    player
        .getLeftHandRing()
        .ifPresent(
            item -> System.out.printf("Left Hand:  %s (%d)\n", item.getName(), item.getCost()));
    player
        .getRightHandRing()
        .ifPresent(
            item -> System.out.printf("Right Hand:  %s (%d)\n", item.getName(), item.getCost()));
    playerWinsFightWithBoss(player, boss);
  }

  private static Set<Player> createPlayerOptions(ItemCatalog itemCatalog) {
    ImmutableSet.Builder<Player> players = ImmutableSet.builder();

    for (Item weapon : itemCatalog.getWeapons()) {
      for (Item armor : itemCatalog.getArmor()) {
        for (Item leftHandRing : itemCatalog.getRings()) {
          Set<Item> ringsWithoutLeftHandRing = Sets.newHashSet(itemCatalog.getRings());
          ringsWithoutLeftHandRing.remove(leftHandRing);

          for (Item rightHandRing : ringsWithoutLeftHandRing) {
            Player playerWithArmorAndRings = new Player(weapon, armor, leftHandRing, rightHandRing);
            players.add(playerWithArmorAndRings);

            Player playerWithoutArmor = playerWithArmorAndRings.removeArmor();
            players.add(playerWithoutArmor);

            players.add(playerWithArmorAndRings.removeLeftHandRing());
            players.add(playerWithoutArmor.removeLeftHandRing());

            players.add(playerWithArmorAndRings.removeRightHandRing());
            players.add(playerWithoutArmor.removeRightHandRing());
          }
        }
      }
    }

    return players.build();
  }

  private static boolean playerWinsFightWithBoss(Player originalPlayer, Boss originalBoss) {
    Boss boss =
        new Boss(originalBoss.getDamage(), originalBoss.getDefense(), originalBoss.getHitPoints());
    Player player =
        new Player(
            originalPlayer.getWeapon(),
            originalPlayer.getArmor().orElse(null),
            originalPlayer.getLeftHandRing().orElse(null),
            originalPlayer.getRightHandRing().orElse(null));

    while (boss.getHitPoints() > 0 && player.getHitPoints() > 0) {
      boss.defendAttack(player.getDamage());

      if (boss.getHitPoints() > 0) {
        player.defendAttack(boss.getDamage());
      }
    }

    boolean playerWins = player.getHitPoints() > 0;

    return playerWins;
  }
}
