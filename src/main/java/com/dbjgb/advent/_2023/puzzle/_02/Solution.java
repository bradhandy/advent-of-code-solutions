package com.dbjgb.advent._2023.puzzle._02;

import com.dbjgb.advent.Utility;
import com.google.common.base.Splitter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern GAME_ID_PATTERN = Pattern.compile("^Game (\\d+):\\s*");
  private static final Splitter DRAW_SPLITTER = Splitter.on("; ");
  private static final Splitter DICE_SPLITTER = Splitter.on(", ");
  private static final Pattern DIE_PATTERN = Pattern.compile("(\\d+) ([a-z]+)");

  enum Color { RED, BLUE, GREEN }

  public static class Part01 {
    public static void main(String... args) throws Exception {
      Map<Color, Integer> diceAvailable = Map.of(Color.RED, 12, Color.GREEN, 13, Color.BLUE, 14);

      int total = 0;
      for (String game : Utility.readAllLines("_2023/puzzle/_02/input.txt")) {
        Matcher gameIdMatcher = GAME_ID_PATTERN.matcher(game);
        gameIdMatcher.find();
        Integer gameId = Integer.parseInt(gameIdMatcher.group(1));

        boolean valid = true;
        String draws = gameIdMatcher.replaceFirst("");
        for (String draw : DRAW_SPLITTER.split(draws)) {
          for (String die : DICE_SPLITTER.split(draw)) {
            Matcher dieMatcher = DIE_PATTERN.matcher(die);
            dieMatcher.find();

            int drawnDice = Integer.parseInt(dieMatcher.group(1));
            Integer availableDice = diceAvailable.get(Color.valueOf(dieMatcher.group(2).toUpperCase()));
            if (availableDice == null || drawnDice > availableDice) {
              valid = false;
              break;
            }
          }

          if (!valid) {
            break;
          }
        }

        if (valid) {
          total += gameId;
        }
      }

      System.out.printf("Total: %d\n", total);
    }
  }

  public static class Part02 {
    public static void main(String... args) throws Exception {

      long powerTotal = 0;
      for (String game : Utility.readAllLines("_2023/puzzle/_02/input.txt")) {
        Matcher gameIdMatcher = GAME_ID_PATTERN.matcher(game);
        Map<Color, Integer> minimumPerColor = new HashMap<>();

        String draws = gameIdMatcher.replaceFirst("");
        for (String draw : DRAW_SPLITTER.split(draws)) {
          for (String die : DICE_SPLITTER.split(draw)) {
            Matcher dieMatcher = DIE_PATTERN.matcher(die);
            dieMatcher.find();

            int drawnDice = Integer.parseInt(dieMatcher.group(1));
            Color dieColor = Color.valueOf(dieMatcher.group(2).toUpperCase());
            if (drawnDice > minimumPerColor.getOrDefault(dieColor, 0)) {
              minimumPerColor.put(dieColor, drawnDice);
            }
          }
        }

        powerTotal += minimumPerColor.values().stream().reduce(1, (left, right) -> left * right);
      }

      System.out.printf("Total: %d\n", powerTotal);
    }
  }
}
