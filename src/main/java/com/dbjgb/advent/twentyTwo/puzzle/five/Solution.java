package com.dbjgb.advent.twentyTwo.puzzle.five;

import com.dbjgb.advent.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("move (\\d+) from (\\d) to (\\d)");
  public static void main(String... args) throws Exception {
    List<String> stackConfiguration = Utility.readAllLines("twentyTwo/puzzle/five/stacks.txt");
    Collections.reverse(stackConfiguration);

    String[] stackIds = stackConfiguration.get(0).trim().split(" +");
    List<List<Character>> stacks9000 = new ArrayList<>();
    List<List<Character>> stacks9001 = new ArrayList<>();
    Arrays.stream(stackIds).forEach((id) -> {
      stacks9000.add(new ArrayList<>());
      stacks9001.add(new ArrayList<>());
    });

    for (String singleStackPlane : stackConfiguration.subList(1, stackConfiguration.size())) {
      int indexOfColumn = 0;
      while ((indexOfColumn = singleStackPlane.indexOf('[', indexOfColumn)) >= 0) {
        int stack = indexOfColumn / 4;
        char value = singleStackPlane.charAt(++indexOfColumn);

        stacks9000.get(stack).add(value);
        stacks9001.get(stack).add(value);
      }
    }

    List<String> instructions = Utility.readAllLines("twentyTwo/puzzle/five/input.txt");
    for (String instruction : instructions) {
      Matcher instructionMatcher = INSTRUCTION_PATTERN.matcher(instruction);
      if (!instructionMatcher.matches()) {
        throw new IllegalStateException(String.format("'%s' didn't match pattern.", instruction));
      }

      int numberOfItems = Integer.parseInt(instructionMatcher.group(1));
      int sourceStack = Integer.parseInt(instructionMatcher.group(2)) - 1;
      int destinationStack = Integer.parseInt(instructionMatcher.group(3)) - 1;

      List<Character> sourceStack9000 = stacks9000.get(sourceStack);
      List<Character> destinationStack9000 = stacks9000.get(destinationStack);
      List<Character> containersToMove = sourceStack9000.subList(sourceStack9000.size() - numberOfItems, sourceStack9000.size());
      Collections.reverse(containersToMove);
      destinationStack9000.addAll(containersToMove);
      int originalSourceSize = sourceStack9000.size();
      for (int i = originalSourceSize - 1; i >= originalSourceSize - numberOfItems; i--) {
        sourceStack9000.remove(i);
      }

      List<Character> sourceStack9001 = stacks9001.get(sourceStack);
      containersToMove = sourceStack9001.subList(sourceStack9001.size() - numberOfItems, sourceStack9001.size());
      stacks9001.get(destinationStack).addAll(containersToMove);
      originalSourceSize = sourceStack9001.size();
      for (int i = originalSourceSize - 1; i >= originalSourceSize - numberOfItems; i--) {
        sourceStack9001.remove(i);
      }
    }

    System.out.println("\n");
    for (List<Character> stack : stacks9000) {
      System.out.print(stack.get(stack.size() - 1));
    }
    System.out.println("\n");
    System.out.println("\n");
    for (List<Character> stack : stacks9001) {
      System.out.print(stack.get(stack.size() - 1));
    }
    System.out.println("\n");
  }
}
