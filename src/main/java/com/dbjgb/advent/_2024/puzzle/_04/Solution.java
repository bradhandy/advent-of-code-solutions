package com.dbjgb.advent._2024.puzzle._04;

import com.dbjgb.advent.Utility;

import java.util.List;

public class Solution {

  private static final Match VERTICAL_FORWARD =
      new Match("XMAS", new PositionDelta(0, 0), new PositionDelta(0, 1), new PositionDelta(0, 2), new PositionDelta(0, 3));
  private static final Match VERTICAL_BACKWARD =
      new Match("XMAS", new PositionDelta(0, 3), new PositionDelta(0, 2), new PositionDelta(0, 1), new PositionDelta(0, 0));
  private static final Match HORIZONTAL_FORWARD =
      new Match("XMAS", new PositionDelta(0, 0), new PositionDelta(1, 0), new PositionDelta(2, 0), new PositionDelta(3, 0));
  private static final Match HORIZONTAL_BACKWARD =
      new Match("XMAS", new PositionDelta(3, 0), new PositionDelta(2, 0), new PositionDelta(1, 0), new PositionDelta(0, 0));
  private static final Match DIAGONAL_DOWN_RIGHT_FORWARD =
      new Match("XMAS", new PositionDelta(0, 0), new PositionDelta(1, 1), new PositionDelta(2, 2), new PositionDelta(3, 3));
  private static final Match DIAGONAL_DOWN_RIGHT_BACKWARD =
      new Match("XMAS", new PositionDelta(3, 3), new PositionDelta(2, 2), new PositionDelta(1, 1), new PositionDelta(0, 0));
  private static final Match DIAGONAL_UP_RIGHT_FORWARD =
      new Match("XMAS", new PositionDelta(0, 0), new PositionDelta(-1, 1), new PositionDelta(-2, 2), new PositionDelta(-3, 3));
  private static final Match DIAGONAL_UP_RIGHT_BACKWARD =
      new Match("XMAS", new PositionDelta(-3, 3), new PositionDelta(-2, 2), new PositionDelta(-1, 1), new PositionDelta(0, 0));
  private static final List<Match> MATCHES = List.of(HORIZONTAL_BACKWARD,
      HORIZONTAL_FORWARD,
      VERTICAL_BACKWARD,
      VERTICAL_FORWARD,
      DIAGONAL_DOWN_RIGHT_BACKWARD,
      DIAGONAL_DOWN_RIGHT_FORWARD,
      DIAGONAL_UP_RIGHT_BACKWARD,
      DIAGONAL_UP_RIGHT_FORWARD);

  //  M S
  //   A
  //  M S
  private static final Match MAS_DOWN_RIGHT_UP_RIGHT = new Match("MMASS", new PositionDelta(0, 0), new PositionDelta(0, 2), new PositionDelta(1, 1), new PositionDelta(2, 0), new PositionDelta(2, 2));

  //  S S
  //   A
  //  M M
  private static final Match MAS_DOWN_RIGHT_BACKWARD_UP_RIGHT = new Match("SMASM", new PositionDelta(0, 0), new PositionDelta(0, 2), new PositionDelta(1, 1), new PositionDelta(2, 0), new PositionDelta(2, 2));

  //  S M
  //   A
  //  S M
  private static final Match MAS_DOWN_RIGHT_BACKWARD_UP_RIGHT_BACKWARD = new Match("SSAMM", new PositionDelta(0, 0), new PositionDelta(0, 2), new PositionDelta(1, 1), new PositionDelta(2, 0), new PositionDelta(2, 2));

  //  M M
  //   A
  //  S S
  private static final Match MAS_DOWN_RIGHT_UP_RIGHT_BACKWARD = new Match("MSAMS", new PositionDelta(0, 0), new PositionDelta(0, 2), new PositionDelta(1, 1), new PositionDelta(2, 0), new PositionDelta(2, 2));
  private static final List<Match> XMAS_MATCHES =
      List.of(MAS_DOWN_RIGHT_BACKWARD_UP_RIGHT, MAS_DOWN_RIGHT_BACKWARD_UP_RIGHT_BACKWARD, MAS_DOWN_RIGHT_UP_RIGHT, MAS_DOWN_RIGHT_UP_RIGHT_BACKWARD);

  public static void main(String... args) throws Exception {
    solvePartOne();
    solvePartTwo();
  }

  private static void solvePartOne() throws Exception {
    List<String> fileData = Utility.readAllLines("_2024/puzzle/_04/input.txt");
    char[][] data = new char[fileData.size()][];
    int lineNumber = 0;
    for (String line : fileData) {
      data[lineNumber++] = line.toCharArray();
    }

    int totalMatches = 0;
    for (int y = 0; y < data.length; y++) {
      for (int x = 0; x < data[y].length; x++) {
        for (Match match : MATCHES) {
          totalMatches += match.matches(data, x, y) ? 1 : 0;
        }
      }
    }

    System.out.println("Part One: " + totalMatches);
  }

  private static void solvePartTwo() throws Exception {
    List<String> fileData = Utility.readAllLines("_2024/puzzle/_04/input.txt");
    char[][] data = new char[fileData.size()][];
    int lineNumber = 0;
    for (String line : fileData) {
      data[lineNumber++] = line.toCharArray();
    }

    int totalMatches = 0;
    for (int y = 0; y < data.length; y++) {
      for (int x = 0; x < data[y].length; x++) {
        for (Match match : XMAS_MATCHES) {
          totalMatches += match.matches(data, x, y) ? 1 : 0;
        }
      }
    }

    System.out.println("Part Two: " + totalMatches);
  }

  private static class Match {

    private final String word;
    private final List<PositionDelta> positions;

    public Match(String word, PositionDelta... positions) {
      this.word = word;
      this.positions = List.of(positions);
    }

    public boolean matches(char[][] data, int x, int y) {
      for (int i = 0; i < word.length(); i++) {
        if (!positions.get(i).isInBounds(data, x, y) || word.charAt(i) != positions.get(i).getChar(data, x, y)) {
          return false;
        }
      }
      return true;
    }
  }

  private static class PositionDelta {

    private final int x;
    private final int y;

    public PositionDelta(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public boolean isInBounds(char[][] data, int actualX, int actualY) {
      return (actualY + y) >= 0 && (actualY + y) < data.length && (actualX + x) >= 0 && (actualX + x) < data[actualY + y].length;
    }

    public char getChar(char[][] data, int actualX, int actualY) {
      return data[actualY + y][actualX + x];
    }
  }
}
