package com.dbjgb.advent._2022.puzzle._10;

import com.dbjgb.advent.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern INSTRUCTION = Pattern.compile("(noop|addx)(?: (-?\\d+))?");

  public static void main(String... args) throws Exception {
    List<String> instructions = Utility.readAllLines("_2022/puzzle/_10/input.txt");
    List<Long> signalStrengths = new ArrayList<>();
    Console console = new Console();

    int register = 1;
    int cycleCount = 0;
    for (String instruction : instructions) {
      Matcher instructionMatcher = INSTRUCTION.matcher(instruction);
      if (!instructionMatcher.matches()) {
        throw new IllegalStateException(String.format("Instruction didn't match pattern: %s\n", instruction));
      }

      recordSignalStrength(cycleCount + 1, register, signalStrengths);
      console.drawPixel();
      cycleCount += 1;

      String name = instructionMatcher.group(1);
      if (name.equals("addx")) {
        recordSignalStrength(cycleCount + 1, register, signalStrengths);
        console.drawPixel();
        cycleCount += 1;
        int value = Integer.parseInt(instructionMatcher.group(2));
        register += value;
        console.setSpritePosition(register);
      }
    }

    System.out.printf("Signal strength sum: %d\n", signalStrengths.stream().reduce(0L, Long::sum));

    char[][] display = console.getDisplay();
    for (int row = 0; row < display.length; row++) {
      System.out.println(display[row]);
    }
  }

  private static void recordSignalStrength(long cycleCount, long register, List<Long> signalStrengths) {
    if (cycleCount == 20 || (cycleCount - 20) % 40 == 0) {
      signalStrengths.add(cycleCount * register);
    }
  }
}
