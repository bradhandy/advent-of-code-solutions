package com.dbjgb.advent._2024.puzzle._01;

import com.dbjgb.advent.Utility;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern LINE_FORMAT = Pattern.compile("(\\d+)\\s+(\\d+)");

  public static void main(String... args) throws Exception {
    solvePartOne();
    solvePartTwo();
  }

  private static void solvePartOne() throws Exception {
    List<BigInteger> left = new ArrayList<>();
    List<BigInteger> right = new ArrayList<>();
    for (String line : Utility.readAllLines("_2024/puzzle/_01/input.txt")) {
      Matcher matcher = LINE_FORMAT.matcher(line);
      if (matcher.matches()) {
        String leftNumber = matcher.group(1);
        String rightNumber = matcher.group(2);

        left.add(new BigInteger(leftNumber));
        right.add(new BigInteger(rightNumber));
      }
    }

    Collections.sort(left);
    Collections.sort(right);

    int iterations = Math.min(left.size(), right.size());
    BigInteger total = BigInteger.ZERO;
    for (int i = 0; i < iterations; i++) {
      total = total.add(left.get(i).subtract(right.get(i)).abs());
    }

    System.out.printf("Part One: %s\n", total);
  }

  private static void solvePartTwo() throws Exception {
    List<BigInteger> left = new ArrayList<>();
    Map<BigInteger, AtomicInteger> rightAppearances = new HashMap<>();
    for (String line : Utility.readAllLines("_2024/puzzle/_01/input.txt")) {
      Matcher matcher = LINE_FORMAT.matcher(line);
      if (matcher.matches()) {
        String leftNumber = matcher.group(1);
        String rightNumber = matcher.group(2);

        left.add(new BigInteger(leftNumber));
        BigInteger rightInteger = new BigInteger(rightNumber);
        AtomicInteger previousValue = rightAppearances.putIfAbsent(rightInteger, new AtomicInteger(1));
        if (previousValue != null) {
          previousValue.incrementAndGet();
        }
      }
    }

    BigInteger result = left.stream().map(value -> {
          AtomicInteger occurrences = rightAppearances.getOrDefault(value, new AtomicInteger(0));
          return value.multiply(BigInteger.valueOf(occurrences.getAndIncrement()));
        })
        .reduce(BigInteger.ZERO, BigInteger::add);
    System.out.printf("Part Two: %s\n", result);
  }
}
