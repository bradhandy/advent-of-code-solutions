package com.dbjgb.advent._2024.puzzle._03;

import com.dbjgb.advent.Utility;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern MUL_INSTRUCTION_PATTERN = Pattern.compile("mul\\((-?\\d+),(-?\\d+)\\)");
  private static final Pattern MUL_OR_DONOT_INSTRUCTION_PATTERN = Pattern.compile("((?:do(?:n't)?\\(\\))|(?:mul\\((-?\\d+),(-?\\d+)\\)))");

  public static void main(String... args) throws Exception {
    solvePartOne();
    solvePartTwo();
  }

  private static void solvePartOne() throws Exception {
    BigInteger total = BigInteger.ZERO;
    for (String line : Utility.readAllLines("_2024/puzzle/_03/input.txt")) {
      Matcher mulMatcher = MUL_INSTRUCTION_PATTERN.matcher(line);
      while (mulMatcher.find()) {
        BigInteger left = new BigInteger(mulMatcher.group(1));
        BigInteger right = new BigInteger(mulMatcher.group(2));
        total = total.add(left.multiply(right));
      }
    }

    System.out.println("Part One: " + total);
  }

  private static void solvePartTwo() throws Exception {
    BigInteger total = BigInteger.ZERO;
    boolean enabled = true;
    for (String line : Utility.readAllLines("_2024/puzzle/_03/input.txt")) {
      Matcher mulMatcher = MUL_OR_DONOT_INSTRUCTION_PATTERN.matcher(line);
      while (mulMatcher.find()) {
        String instruction = mulMatcher.group(1);
        boolean multiplicationInstruction = instruction.startsWith("mul");
        if (enabled && multiplicationInstruction) {
          BigInteger left = new BigInteger(mulMatcher.group(2));
          BigInteger right = new BigInteger(mulMatcher.group(3));
          total = total.add(left.multiply(right));
        } else {
          enabled = instruction.equals("do()");
        }
      }
    }

    System.out.println("Part Two: " + total);
  }
}
