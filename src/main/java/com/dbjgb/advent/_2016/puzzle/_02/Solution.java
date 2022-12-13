package com.dbjgb.advent._2016.puzzle._02;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;

public class Solution {

  public static void main(String... args) throws Exception {
    printOutGridKeypadCode();
    printOutDiamondKeypadCode();
  }

  private static void printOutGridKeypadCode() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("_2016/puzzle/_02/input.txt")) {
      KeypadButton keypadButton = new GridKeypadButton(1, 1);
      String instructions;
      while ((instructions = inputReader.readLine()) != null) {
        for (int i = 0; i < instructions.length(); i++) {
          Direction direction = Direction.fromCode(instructions.charAt(i));
          shiftKeypadButton(keypadButton, direction);
        }

        System.out.printf("%d", keypadButton.getButton());
      }

      System.out.printf("\n");
    }
  }

  private static void printOutDiamondKeypadCode() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("_2016/puzzle/_02/input.txt")) {
      KeypadButton keypadButton = new DiamondKeypadButton(0, -2, 5);
      String instructions;

      while ((instructions = inputReader.readLine()) != null) {
        for (int i = 0; i < instructions.length(); i++) {
          Direction direction = Direction.fromCode(instructions.charAt(i));
          shiftKeypadButton(keypadButton, direction);

        }

        System.out.printf("%X", keypadButton.getButton());
      }

      System.out.printf("\n");
    }
  }

  private static void shiftKeypadButton(KeypadButton keypadButton, Direction direction) {
    if (direction == Direction.UP) {
      keypadButton.shiftUp();
    } else if (direction == Direction.DOWN) {
      keypadButton.shiftDown();
    } else if (direction == Direction.LEFT) {
      keypadButton.shiftLeft();
    } else if (direction == Direction.RIGHT) {
      keypadButton.shiftRight();
    }
  }

  public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public static Direction fromCode(char code) {
      if (code == 'U') {
        return UP;
      } else if (code == 'D') {
        return DOWN;
      } else if (code == 'L') {
        return LEFT;
      }

      return RIGHT;
    }
  }

  public interface KeypadButton {
    void shiftUp();

    void shiftDown();

    void shiftLeft();

    void shiftRight();

    int getButton();
  }

  public static class GridKeypadButton implements KeypadButton {

    private int row;
    private int column;

    public GridKeypadButton(int row, int column) {
      this.row = row;
      this.column = column;
    }

    public void shiftUp() {
      row = Math.max(0, row - 1);
    }

    public void shiftDown() {
      row = Math.min(2, row + 1);
    }

    public void shiftLeft() {
      column = Math.max(0, column - 1);
    }

    public void shiftRight() {
      column = Math.min(2, column + 1);
    }

    public int getButton() {
      return 1 + (row * 3) + column;
    }
  }

  public static class DiamondKeypadButton implements KeypadButton {

    private int row;
    private int column;
    private int size;

    public DiamondKeypadButton(int row, int column, int size) {
      this.row = row;
      this.column = column;
      this.size = size;
    }

    @Override
    public void shiftUp() {
      row += 1;
      if (!isValidButton()) {
        row -= 1;
      }
    }

    @Override
    public void shiftDown() {
      row -= 1;
      if (!isValidButton()) {
        row += 1;
      }
    }

    @Override
    public void shiftLeft() {
      column -= 1;
      if (!isValidButton()) {
        column += 1;
      }
    }

    @Override
    public void shiftRight() {
      column += 1;
      if (!isValidButton()) {
        column -= 1;
      }
    }

    private boolean isValidButton() {
      int numEntriesInRow = numberOfEntries(row);
      int numEntriesInColumn = numberOfEntries(column);

      // a valid row depends on the number of available values for the column.  a valid column
      // depends on the number of available values for the row.
      return (0 <= Math.abs(column) && Math.abs(column) <= ((numEntriesInRow - 1) / 2))
          && (0 <= Math.abs(row) && Math.abs(row) <= ((numEntriesInColumn - 1) / 2));
    }

    @Override
    public int getButton() {
      int button = 0;

      for (int currentRow = (size - 1) / 2; this.row < currentRow; currentRow--) {
        button += numberOfEntries(currentRow);
      }
      for (int currentColumn = -((numberOfEntries(this.row) - 1) / 2);
          currentColumn <= this.column;
          currentColumn++) {
        button += 1;
      }

      return button;
    }

    private int numberOfEntries(int rowOrColumn) {
      return Math.max(0, size - (Math.abs(rowOrColumn) * 2));
    }
  }
}
