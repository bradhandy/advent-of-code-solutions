package com.dbjgb.advent._2022.puzzle._10;

public class Console {

  private int position;
  private int spritePosition;
  private char[][] display;

  public Console() {
    position = 0;
    spritePosition = 1;
    display = new char[6][40];
  }

  public void drawPixel() {
    int row = position / display[0].length;
    int column = position % 40;

    boolean lit = (column >= (spritePosition - 1) && column <= (spritePosition + 1));
    display[row][column] = (lit) ? '#' : '.';
    position++;
  }

  public void setSpritePosition(int spritePosition) {
    this.spritePosition = spritePosition;
  }

  public char[][] getDisplay() {
    return display;
  }
}
