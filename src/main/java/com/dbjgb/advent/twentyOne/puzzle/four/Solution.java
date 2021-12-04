package com.dbjgb.advent.twentyOne.puzzle.four;

import com.dbjgb.advent.Utility;
import com.google.common.base.MoreObjects;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

  public static void main(String... args) throws Exception {
    List<Integer> drawnNumbers = new ArrayList<>();
    List<Board> boards = new ArrayList<>();
    int[][] grid = null;

    try (BufferedReader inputReader = Utility.openInputFile("twentyOne/puzzle/four/input.txt")) {
      String line = inputReader.readLine();
      Matcher drawnNumberMatcher = NUMBER_PATTERN.matcher(line);
      while (drawnNumberMatcher.find()) {
        drawnNumbers.add(Integer.valueOf(drawnNumberMatcher.group()));
      }

      int row = 0;
      int column;
      while ((line = inputReader.readLine()) != null) {
        if (line.trim().isEmpty()) {
          if (grid != null) {
            boards.add(new Board(grid));
          }
          row = 0;
          grid = new int[5][5];
          continue;
        }
        column = 0;

        Matcher gridNumberMatcher = NUMBER_PATTERN.matcher(line);
        while (gridNumberMatcher.find()) {
          grid[row][column++] = Integer.parseInt(gridNumberMatcher.group());
        }

        row += 1;
      }
      if (row > 0) {
        boards.add(new Board(grid));
      }

      Set<Board> winners = new HashSet<>();
      Board firstWinner = null;
      Integer firstWinnerCalledNumber = null;
      Board lastWinner = null;
      Integer lastWinnerCalledNumber = null;
      for (Integer calledNumber : drawnNumbers) {
        for (Board board : boards) {
          if (winners.contains(board)) {
            continue;
          }

          if (board.callNumber(calledNumber)) {
            winners.add(board);
            firstWinner = MoreObjects.firstNonNull(firstWinner, board);
            firstWinnerCalledNumber = MoreObjects.firstNonNull(firstWinnerCalledNumber, calledNumber);
            lastWinner = board;
            lastWinnerCalledNumber = calledNumber;
          }
        }
      }

      System.out.println("First Winner:\n");
      System.out.printf("Bingo on %d!\n", firstWinnerCalledNumber);
      System.out.printf(
          "Product: %d\n",
          firstWinner.getUnmarkedNumbers().stream().reduce(0, Integer::sum) * firstWinnerCalledNumber);
      System.out.println("Last Winner:\n");
      System.out.printf("Bingo on %d!\n", lastWinnerCalledNumber);
      System.out.printf(
          "Product: %d\n",
          lastWinner.getUnmarkedNumbers().stream().reduce(0, Integer::sum) * lastWinnerCalledNumber);
    }
  }

  private static class Board {

    private final int[][] grid;
    private final Map<Integer, int[]> index;

    private Board(int[][] numbers) {
      this.index = new HashMap<>();
      this.grid = new int[numbers.length][];

      for (int i = 0; i < numbers.length; i++) {
        this.grid[i] = new int[numbers[i].length];
        for (int j = 0; j < this.grid[i].length; j++) {
          int boardNumber = numbers[i][j];
          index.put(boardNumber, new int[] {i, j});
          this.grid[i][j] = boardNumber;
        }
      }
    }

    public boolean callNumber(int number) {
      if (index.containsKey(number)) {
        int[] coordinates = index.remove(number);
        grid[coordinates[0]][coordinates[1]] = -1;

        boolean rowWins = true;
        boolean columnWins = true;
        for (int i = 0, row = coordinates[0], column = coordinates[1];
            i < grid.length && (rowWins || columnWins);
            i++) {
          rowWins = rowWins && grid[row][(column + i) % grid[row].length] == -1;
          columnWins = columnWins && grid[(row + i) % grid.length][column] == -1;
        }

        return rowWins || columnWins;
      }

      return false;
    }

    public Set<Integer> getUnmarkedNumbers() {
      return Set.copyOf(index.keySet());
    }
  }
}
