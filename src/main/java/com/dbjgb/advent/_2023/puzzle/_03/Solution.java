package com.dbjgb.advent._2023.puzzle._03;

import com.dbjgb.advent.Utility;
import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern PART_IDENTIFIER_PATTERN = Pattern.compile("(\\d+|[^.\\d])");

  public static class Part01 {
    public static void main(String... args) throws Exception {
      Map<Position, String> partIdentifiers = new TreeMap<>();
      Set<Position> symbols = new TreeSet<>();
      int lineNumber = 1;
      for (String line : Utility.readAllLines("_2023/puzzle/_03/input.txt")) {
        Matcher valueMatcher = PART_IDENTIFIER_PATTERN.matcher(line);
        while (valueMatcher.find()) {
          String value = valueMatcher.group(1);
          if (Character.isDigit(value.charAt(0))) {
            for (int index = valueMatcher.start(1); index < valueMatcher.end(1); index++) {
              System.out.printf("%s -> %s\n", new Position(lineNumber, index), value);
              partIdentifiers.put(new Position(lineNumber, index), value);
            }
          } else {
            Position position = new Position(lineNumber, valueMatcher.start(1));
            symbols.addAll(position.getAdjacentPositions());
          }
        }

        lineNumber++;
      }

      Set<Position> partIdentifierPositions = new HashSet<>(partIdentifiers.keySet());
      partIdentifierPositions.retainAll(symbols);

      // There are duplicate values, but I want to make sure we aren't dealing with comparing actual
      // values when adding to the set. I only want to remove the same object when included as a
      // duplicate within the set. Therefore, we only pass a comparator the checks for Object equality
      // using == instead of ".equals" or Objects.equal().
      Set<String> uniqueValues = new TreeSet<>((left, right) -> {
        int result = left.compareTo(right);
        if (result == 0 && left == right) {
          return 0;
        }

        return (result == 0) ? 1 : result;
      });
      long total = partIdentifierPositions.stream()
          .map(position -> {
            String value = partIdentifiers.get(position);
            if (uniqueValues.contains(value)) {
              return 0L;
            }

            uniqueValues.add(value);
            return Long.parseLong(value);
          }).reduce(0L, Long::sum);
      System.out.printf("Total: %d\n", total);
    }
  }

  public static class Part02 {
    public static void main(String... args) throws Exception {
      Map<Position, String> partIdentifiers = new TreeMap<>();
      Set<Position> gears = new TreeSet<>();
      int lineNumber = 1;
      for (String line : Utility.readAllLines("_2023/puzzle/_03/input.txt")) {
        Matcher valueMatcher = PART_IDENTIFIER_PATTERN.matcher(line);
        while (valueMatcher.find()) {
          String value = valueMatcher.group(1);
          if (Character.isDigit(value.charAt(0))) {
            for (int index = valueMatcher.start(1); index < valueMatcher.end(1); index++) {
              System.out.printf("%s -> %s\n", new Position(lineNumber, index), value);
              partIdentifiers.put(new Position(lineNumber, index), value);
            }
          } else if (value.equals("*")) {
            Position position = new Position(lineNumber, valueMatcher.start(1));
            gears.add(position);
          }
        }

        lineNumber++;
      }

      Set<String> uniqueValues = new TreeSet<>((left, right) -> {
        int result = left.compareTo(right);
        if (result == 0 && left == right) {
          return 0;
        }

        return (result == 0) ? 1 : result;
      });

      long total = 0;
      for (Position gear : gears) {
        for (Position adjacentPosition : gear.getAdjacentPositions()) {
          if (partIdentifiers.containsKey(adjacentPosition)) {
            uniqueValues.add(partIdentifiers.get(adjacentPosition));
          }
        }
        if (uniqueValues.size() == 2) {
          total += uniqueValues.stream().map(Long::parseLong).reduce(1L, (left, right) -> left * right);
        }
        uniqueValues.clear();
      }
      System.out.printf("Total: %d\n", total);
    }
  }

  private static class Position implements Comparable<Position> {

    private final int line;
    private final int index;

    public Position(int line, int index) {
      this.line = line;
      this.index = index;
    }

    public Set<Position> getAdjacentPositions() {
      ImmutableSet.Builder<Position> adjacentPositions = ImmutableSet.builder();
      adjacentPositions.add(new Position(line - 1, index - 1));
      adjacentPositions.add(new Position(line - 1, index));
      adjacentPositions.add(new Position(line - 1, index + 1));
      adjacentPositions.add(new Position(line, index - 1));
      adjacentPositions.add(new Position(line, index + 1));
      adjacentPositions.add(new Position(line + 1, index - 1));
      adjacentPositions.add(new Position(line + 1, index));
      adjacentPositions.add(new Position(line + 1, index + 1));

      return adjacentPositions.build();
    }

    @Override
    public int compareTo(Position other) {
      int lineResult = Integer.compare(this.line, other.line);
      if (lineResult != 0) {
        return lineResult;
      }

      return Integer.compare(this.index, other.index);
    }

    @Override
    public String toString() {
      return "Position{" +
          "line=" + line +
          ", index=" + index +
          '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Position position = (Position) o;
      return line == position.line && index == position.index;
    }

    @Override
    public int hashCode() {
      return Objects.hash(line, index);
    }
  }

}
