package com.dbjgb.advent.twenty.puzzle.twentyTwo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solution {

  private static final List<Integer> PLAYER_ONE = //List.of(9, 2, 6, 3, 1);
      List.of(
          21, 48, 44, 31, 29, 5, 23, 11, 12, 27, 49, 22, 18, 7, 15, 20, 2, 45, 14, 17, 40, 35, 6,
          24, 41);
  private static final List<Integer> PLAYER_TWO = //List.of(5, 8, 4, 7, 10);
      List.of(
          47, 1, 10, 16, 28, 37, 8, 26, 46, 25, 3, 9, 34, 50, 32, 36, 43, 4, 42, 33, 19, 13, 38, 39,
          30);

  public static void main(String... args) throws Exception {
    processPuzzle(false);
    processPuzzle(true);
  }

  private static void processPuzzle(boolean recurse) {
    Winner winner = playCombat(PLAYER_ONE, PLAYER_TWO, recurse ? 0 : -1);

    BigInteger score = BigInteger.ZERO;
    List<Integer> finalDeck = winner.getFinalDeck();
    int count = finalDeck.size();
    for (int index = 0; index < count; index++) {
      score = score.add(new BigInteger(String.valueOf(finalDeck.get(index) * (count - index))));
    }

    System.out.printf("The final score was %s.\n", score);
  }

  private static Winner playCombat(List<Integer> playerOne, List<Integer> playerTwo, int level) {
    Set<List<Integer>> playerOneHands = new HashSet<>();
    Set<List<Integer>> playerTwoHands = new HashSet<>();

    List<Integer> playerOneCopy = new ArrayList<>(playerOne);
    List<Integer> playerTwoCopy = new ArrayList<>(playerTwo);

    while (!playerOneCopy.isEmpty() && !playerTwoCopy.isEmpty()) {
      if (playerOneHands.contains(playerOneCopy) || playerTwoHands.contains(playerTwoCopy)) {
        return new Winner(Player.ONE, playerOneCopy);
      }

      playerOneHands.add(List.copyOf(playerOneCopy));
      playerTwoHands.add(List.copyOf(playerTwoCopy));

      int playerOneCard = playerOneCopy.remove(0);
      int playerTwoCard = playerTwoCopy.remove(0);

      Winner winner = new Winner(null, List.of());
      if (level >= 0) {
        if (playerOneCard <= playerOneCopy.size() && playerTwoCard <= playerTwoCopy.size()) {
          winner =
              playCombat(
                  playerOneCopy.subList(0, playerOneCard), playerTwoCopy.subList(0, playerTwoCard), level + 1);
        }
      }

      if (winner.getPlayer() == Player.ONE
          || (winner.getPlayer() == null && playerOneCard > playerTwoCard)) {
        playerOneCopy.addAll(List.of(playerOneCard, playerTwoCard));
      } else {
        playerTwoCopy.addAll(List.of(playerTwoCard, playerOneCard));
      }
    }

    if (playerOneCopy.isEmpty()) {
      return new Winner(Player.TWO, playerTwoCopy);
    }
    return new Winner(Player.ONE, playerOneCopy);
  }

  private static class Winner {

    private final Player player;
    private final List<Integer> finalDeck;

    public Winner(Player player, List<Integer> finalDeck) {
      this.player = player;
      this.finalDeck = finalDeck;
    }

    public Player getPlayer() {
      return player;
    }

    public List<Integer> getFinalDeck() {
      return finalDeck;
    }
  }

  private enum Player {
    ONE,
    TWO
  };
}
