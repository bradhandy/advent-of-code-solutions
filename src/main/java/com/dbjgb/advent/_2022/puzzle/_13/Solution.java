package com.dbjgb.advent._2022.puzzle._13;

import com.dbjgb.advent.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class Solution {

  public static void main(String... args) throws Exception {
    List<String> lines = Utility.readAllLines(("_2022/puzzle/_13/input.txt"));
    List<List<Object>> packets = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();
    int orderedPairSum = 0;
    for (int i = 0; i < lines.size(); i += 3) {
      List<Object> left = objectMapper.readValue(lines.get(i), List.class);
      packets.add(left);

      List<Object> right = objectMapper.readValue(lines.get(i + 1), List.class);
      packets.add(right);

      if (inCorrectOrder(left, right) == Result.IN_ORDER) {
        orderedPairSum += ((i / 3) + 1);
      }
    }

    System.out.printf("Sum: %d\n", orderedPairSum);

    packets.add(List.of(List.of(2)));
    packets.add(List.of(List.of(6)));
    Collections.sort(packets, new PacketComparator());
    int firstDividerPacketIndex = packets.indexOf(List.of(List.of(2))) + 1;
    int secondDividerPacketIndex = packets.indexOf(List.of(List.of(6))) + 1;

    System.out.printf("Result: %d\n", firstDividerPacketIndex * secondDividerPacketIndex);
  }

  private static Result inCorrectOrder(List<Object> left, List<Object> right) {
    if (left.equals(right)) {
      return Result.CONTINUE;
    }

    int length = Math.max(left.size(), right.size());
    for (int i = 0; i < length; i++) {
      Object leftValue = (i < left.size()) ? left.get(i) : null;
      Object rightValue = (i < right.size()) ? right.get(i) : null;

      if (leftValue == null || rightValue == null) {
        return (leftValue == null) ? Result.IN_ORDER : Result.OUT_OF_ORDER;
      }

      if (leftValue instanceof Integer && rightValue instanceof Integer) {
        int equality = ((Integer) leftValue).compareTo((Integer) rightValue);
        if (equality != 0) {
          return (equality < 0) ? Result.IN_ORDER : Result.OUT_OF_ORDER;
        }
      } else {
        Result result = inCorrectOrder(convertToList(leftValue), convertToList(rightValue));
        if (result != Result.CONTINUE) {
          return result;
        }
      }
    }

    return Result.CONTINUE;
  }

  private static List<Object> convertToList(Object value) {
    return (value instanceof List) ? (List<Object>) value : List.of(value);
  }

  enum Result {
    IN_ORDER, CONTINUE, OUT_OF_ORDER;
  }

  private static class PacketComparator implements Comparator<List<Object>> {

    @Override
    public int compare(List<Object> o1, List<Object> o2) {
      Result result = inCorrectOrder(o1, o2);
      if (result == Result.CONTINUE) {
        return 0;
      }
      return (result == Result.IN_ORDER) ? -1 : 1;
    }
  }
}
