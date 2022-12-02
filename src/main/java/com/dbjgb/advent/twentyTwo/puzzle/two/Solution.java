package com.dbjgb.advent.twentyTwo.puzzle.two;

import com.dbjgb.advent.Utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern ROUND_PATTERN = Pattern.compile("([ABC]) ([XYZ])");

  public static void main(String... args) throws Exception {
    predictScore();
    determineActualScore();
  }

  private static void predictScore() throws URISyntaxException, IOException {
    int score = 0;
    for (String round : Utility.readAllLines("twentyTwo/puzzle/two/input.txt")) {
      Matcher roundMatcher = ROUND_PATTERN.matcher(round);
      roundMatcher.matches();

      RockPaperScissor action = RockPaperScissor.findByValue(roundMatcher.group(2));
      String elfAction = roundMatcher.group(1);
      Outcome outcome = action.determineOutcome(elfAction);

      score += (outcome.points + action.points);
      System.out.printf(
          "%s vs %s = %s (%d + %d = %d)\n",
          roundMatcher.group(2),
          elfAction,
          outcome,
          outcome.points,
          action.points,
          outcome.points + action.points);
    }

    System.out.println(score);
  }

  private static void determineActualScore() throws IOException, URISyntaxException {
    int score = 0;
    for (String round : Utility.readAllLines("twentyTwo/puzzle/two/input.txt")) {
      Matcher roundMatcher = ROUND_PATTERN.matcher(round);
      roundMatcher.matches();

      RockPaperScissor elfAction = RockPaperScissor.findByValue(roundMatcher.group(1));
      Outcome outcome = Outcome.findByCode(roundMatcher.group(2));
      RockPaperScissor myAction = outcome.actionForOpponentAction(elfAction);

      score += (outcome.points + myAction.points);
      System.out.printf(
          "%s vs %s = %s (%d + %d = %d)\n",
          elfAction,
          myAction,
          outcome,
          outcome.points,
          myAction.points,
          outcome.points + myAction.points);
    }

    System.out.println(score);
  }

  private enum RockPaperScissor {
    INCORRECT_ROCK("X", "C", "B", "A", 1),
    INCORRECT_PAPER("Y", "A", "C", "B", 2),
    INCORRECT_SCISSORS("Z", "B", "A", "C", 3),
    ROCK("A", "C", "B", "A", 1),
    PAPER("B", "A", "C", "B", 2),
    SCISSORS("C", "B", "A", "C", 3);

    private static final Map<String, RockPaperScissor> CODE_TO_ACTION_MAP = new HashMap<>();

    static {
      Arrays.stream(RockPaperScissor.values())
          .forEach(
              (rockPaperScissor) ->
                  CODE_TO_ACTION_MAP.put(rockPaperScissor.code, rockPaperScissor));
    }

    private final String code;
    private final String win;
    private final String lose;
    private final String draw;
    private final int points;

    public static RockPaperScissor findByValue(String value) {
      return CODE_TO_ACTION_MAP.get(value);
    }

    RockPaperScissor(String code, String win, String lose, String draw, int points) {
      this.code = code;
      this.win = win;
      this.lose = lose;
      this.draw = draw;
      this.points = points;
    }

    public Outcome determineOutcome(String opponent) {
      if (win.equals(opponent)) {
        return Outcome.WIN;
      } else if (draw.equals(opponent)) {
        return Outcome.DRAW;
      }

      return Outcome.LOSE;
    }
  }

  private enum Outcome {
    WIN("Z", 6),
    DRAW("Y", 3),
    LOSE("X", 0);

    private static final Map<String, Outcome> CODE_TO_OUTCOME =
        Map.of(WIN.code, WIN, DRAW.code, DRAW, LOSE.code, LOSE);

    private final String code;
    private final int points;

    public static Outcome findByCode(String code) {
      return CODE_TO_OUTCOME.get(code);
    }

    Outcome(String code, int points) {
      this.code = code;
      this.points = points;
    }

    RockPaperScissor actionForOpponentAction(RockPaperScissor rockPaperScissor) {
      String codeForOutcome = rockPaperScissor.draw;
      if (this == WIN) {
        codeForOutcome = rockPaperScissor.lose;
      } else if (this == LOSE) {
        codeForOutcome = rockPaperScissor.win;
      }

      return RockPaperScissor.findByValue(codeForOutcome);
    }
  }
}
