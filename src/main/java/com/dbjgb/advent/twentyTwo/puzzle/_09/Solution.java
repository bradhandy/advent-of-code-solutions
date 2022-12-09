package com.dbjgb.advent.twentyTwo.puzzle._09;

import com.dbjgb.advent.Utility;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("([LURD]) (\\d+)");

  public static void main(String... args) throws Exception {
    simulateRopeWithKnots(2);
    simulateRopeWithKnots(10);
  }

  private static void simulateRopeWithKnots(int numberOfKnots) throws Exception {
    List<String> instructions = Utility.readAllLines("twentyTwo/puzzle/_09/input.txt");
    Set<Cell> uniquePlaces = new HashSet<>();

    RopeEnd tail = new RopeEnd(0, new Cell(0, 0));
    RopeEnd head = new RopeEnd(1, tail, tail.getPosition());
    for (int knot = 2; knot < numberOfKnots; knot++) {
      head = new RopeEnd(knot, head, head.getPosition());
    }

    for (String instruction : instructions) {
      Matcher instructionMatcher = INSTRUCTION_PATTERN.matcher(instruction);
      if (!instructionMatcher.matches()) {
        throw new IllegalStateException(String.format("Instruction did not match pattern: %s\n", instruction));
      }

      Direction direction = Direction.valueByCode(instructionMatcher.group(1).charAt(0));
      int moves = Integer.parseInt(instructionMatcher.group(2));

      for (int move = 0; move < moves; move++) {
        head.move(direction);
        uniquePlaces.add(tail.getPosition());
      }
    }

    System.out.printf("Unique places tail visited: %d\n", uniquePlaces.size());
  }
}
