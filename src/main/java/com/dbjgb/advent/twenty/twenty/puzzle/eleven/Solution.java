package com.dbjgb.advent.twenty.twenty.puzzle.eleven;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

public class Solution {

  private static final int HEIGHT = 98;
  private static int fileCounter = 0;

  public static void main(String... args) throws Exception {
    printOccupiedSeatCountUsingConstrictedRules();
    printOccupiedSeatCountUsingExpandedRules();
  }

  private static void printOccupiedSeatCountUsingExpandedRules() throws IOException {
    char[][] seatingLayout = parseSeatingLayout();
    while (applySeatingExpandedRules(seatingLayout)) {
      System.out.println("Changes applied...");
    }
    System.out.println("Changes complete.");

    int occupiedSeatCount = 0;
    for (int row = 1; row < seatingLayout.length - 1; row++) {
      for (int column = 1; column < seatingLayout[row].length - 1; column++) {
        if (expandedOccupiedState(seatingLayout, row, column) == '#') {
          occupiedSeatCount += 1;
        }
      }
    }

    System.out.printf("Number of occupied seats:  %d\n", occupiedSeatCount);
  }

  private static void printOccupiedSeatCountUsingConstrictedRules() throws IOException {
    char[][] seatingLayout = parseSeatingLayout();
    while (applySeatingConstrictedRules(seatingLayout)) {
      System.out.println("Changes applied...");
    }
    System.out.println("Changes complete.");

    int occupiedSeatCount = 0;
    for (int row = 1; row < seatingLayout.length - 1; row++) {
      for (int column = 1; column < seatingLayout[row].length - 1; column++) {
        if (constrictedOccupiedState(seatingLayout, row, column) == '#') {
          occupiedSeatCount += 1;
        }
      }
    }

    System.out.printf("Number of occupied seats:  %d\n", occupiedSeatCount);
  }

  private static boolean applySeatingConstrictedRules(char[][] currentSeating) throws IOException {
    boolean changeMade = false;

    File newSeating = File.createTempFile("advent", "eleven");
    try (Writer writer = new FileWriter(newSeating)) {
      for (int row = 1; row < currentSeating.length - 1; row++) {
        for (int column = 1; column < currentSeating[row].length - 1; column++) {
          char occupiedState = constrictedOccupiedState(currentSeating, row, column);
          writer.write(occupiedState);
          changeMade |= currentSeating[row][column] != occupiedState;
        }
        writer.write('\n');
        currentSeating[row - 1] = null;
      }

      currentSeating[currentSeating.length - 1] = currentSeating[currentSeating.length - 2] = null;
    }

    parseSeatingLayout(new BufferedReader(new FileReader(newSeating)), currentSeating);
    return changeMade;
  }

  private static boolean applySeatingExpandedRules(char[][] currentSeating) throws IOException {
    boolean changeMade = false;

    File newSeating = File.createTempFile(String.format("%02d-advent", fileCounter++), "eleven");
    try (Writer writer = new FileWriter(newSeating)) {
      for (int row = 1; row < currentSeating.length - 1; row++) {
        for (int column = 1; column < currentSeating[row].length - 1; column++) {
          char occupiedState = expandedOccupiedState(currentSeating, row, column);
          writer.write(occupiedState);
          changeMade |= currentSeating[row][column] != occupiedState;
        }
        writer.write('\n');
      }
    }

    parseSeatingLayout(new BufferedReader(new FileReader(newSeating)), currentSeating);
    return changeMade;
  }

  private static char constrictedOccupiedState(char[][] currentSeating, int row, int column) {
    char currentSeat = currentSeating[row][column];
    if (currentSeat == '.') {
      return '.';
    }

    int occupiedSeatCount = 0;
    for (int observedRow = row - 1; observedRow < (row + 2); observedRow++) {
      for (int observedColumn = column - 1; observedColumn < (column + 2); observedColumn++) {
        if (row == observedRow && column == observedColumn) {
          continue;
        }

        occupiedSeatCount += (currentSeating[observedRow][observedColumn] == '#') ? 1 : 0;
      }
    }

    if (currentSeat == 'L' && occupiedSeatCount == 0) {
      return '#';
    } else if (currentSeat == '#' && occupiedSeatCount >= 4) {
      return 'L';
    }

    return currentSeat;
  }

  private static char expandedOccupiedState(char[][] currentSeating, int row, int column) {
    char currentSeat = currentSeating[row][column];
    if (currentSeat == '.') {
      return '.';
    }

    int occupiedSeatCount = 0;
    for (int observedRow = row - 1; observedRow < (row + 2); observedRow++) {
      for (int observedColumn = column - 1; observedColumn < (column + 2); observedColumn++) {
        if (row == observedRow && column == observedColumn) {
          continue;
        }

        occupiedSeatCount +=
            (occupiedSeatExistsInDirection(
                    currentSeating, row, column, observedRow - row, observedColumn - column))
                ? 1
                : 0;
      }
    }

    if (currentSeat == 'L' && occupiedSeatCount == 0) {
      return '#';
    } else if (currentSeat == '#' && occupiedSeatCount >= 5) {
      return 'L';
    }

    return currentSeat;
  }

  private static boolean occupiedSeatExistsInDirection(
      char[][] currentSeating, int row, int column, int dRow, int dColumn) {
    for (int observedRow = row + dRow, observedColumn = column + dColumn;
        0 <= observedRow
            && observedRow < currentSeating.length
            && 0 <= observedColumn
            && observedColumn < currentSeating[row].length;
         observedRow += dRow, observedColumn += dColumn) {
      if (currentSeating[observedRow][observedColumn] != '.') {
        return currentSeating[observedRow][observedColumn] == '#';
      }
    }

    return false;
  }

  private static char[][] parseSeatingLayout() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/eleven/input.txt")) {
      return parseSeatingLayout(inputReader, new char[HEIGHT + 2][]);
    }
  }

  private static char[][] parseSeatingLayout(BufferedReader inputReader, char[][] seatingLayout)
      throws IOException {
    String line;
    int row = 1;
    while ((line = inputReader.readLine()) != null) {
      char[] seatsInRow = line.toCharArray();
      seatingLayout[row] = new char[seatsInRow.length + 2];
      System.arraycopy(seatsInRow, 0, seatingLayout[row], 1, seatsInRow.length);
      seatingLayout[row][0] = seatingLayout[row][seatsInRow.length + 1] = '.';
      row += 1;
    }

    seatingLayout[0] = new char[seatingLayout[1].length];
    Arrays.fill(seatingLayout[0], '.');
    seatingLayout[seatingLayout.length - 1] =
        Arrays.copyOf(seatingLayout[0], seatingLayout[0].length);

    return seatingLayout;
  }
}
