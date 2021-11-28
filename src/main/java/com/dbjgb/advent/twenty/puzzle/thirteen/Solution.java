package com.dbjgb.advent.twenty.puzzle.thirteen;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.stream;

public class Solution {

  private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");
  private static final Pattern NUMBER_OR_X_PATTERN = Pattern.compile("(x|(\\d+))");

  public static void main(String... args) throws Exception {
    printEarliestBusResult();
    printEarliestTimestampForOffsetPattern();
  }

  private static void printEarliestBusResult() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/thirteen/input.txt")) {
      long earliestDepartureTime = Long.parseLong(inputReader.readLine());
      long minWaitTime = Long.MAX_VALUE;
      long value = 0;

      Matcher busIdsMatcher = NUMBER_PATTERN.matcher(inputReader.readLine());
      while (busIdsMatcher.find()) {
        long busId = Long.parseLong(busIdsMatcher.group(1));
        long timeSinceBusLeft = earliestDepartureTime % busId;
        long waitTime = busId - timeSinceBusLeft;
        if (Long.min(waitTime, minWaitTime) == waitTime) {
          minWaitTime = waitTime;
          value = busId * minWaitTime;
        }
      }

      System.out.printf("Value:  %d\n", value);
    }
  }

  private static void printEarliestTimestampForOffsetPattern() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/thirteen/input.txt")) {
      inputReader.readLine(); // ignore the first line.

      String busLinePositionDefinition = inputReader.readLine();
      List<Long> numbers = new ArrayList<>();
      List<Long> remainders = new ArrayList<>();
      Matcher busLinePositionMatcher = NUMBER_OR_X_PATTERN.matcher(busLinePositionDefinition);
      for (int position = 0; busLinePositionMatcher.find(); position++) {
        String busLineId = busLinePositionMatcher.group(1);
        if (busLineId.equals("x")) {
          continue;
        }

        long interval = Long.parseLong(busLinePositionMatcher.group(1));
        numbers.add(interval);
        remainders.add(expectedRemainder(interval, position));
      }

      Long[] num = numbers.toArray(new Long[numbers.size()]);
      Long[] rem = remainders.toArray(new Long[remainders.size()]);

      System.out.printf("Matching timestamp: %d\n", chineseRemainder(num, rem));
    }
  }

  private static long expectedRemainder(long interval, long position) {
    long expectedRemainer = interval - position;
    while (expectedRemainer < 0) {
      expectedRemainer = interval + expectedRemainer;
    }

    return expectedRemainer % interval;
  }

  /*
   * Pulled this code for the Chinese Remainder Theorem from
   * https://rosettacode.org/wiki/Chinese_remainder_theorem#Java
   */
  public static long chineseRemainder(Long[] n, Long[] a) {

    long prod = stream(n).reduce(1L, (i, j) -> i * j);

    long p, sm = 0;
    for (int i = 0; i < n.length; i++) {
      p = prod / n[i];
      sm += a[i] * mulInv(p, n[i]) * p;
    }
    return sm % prod;
  }

  private static long mulInv(long a, long b) {
    long b0 = b;
    long x0 = 0;
    long x1 = 1;

    if (b == 1)
      return 1;

    while (a > 1) {
      long q = a / b;
      long amb = a % b;
      a = b;
      b = amb;
      long xqx = x1 - q * x0;
      x1 = x0;
      x0 = xqx;
    }

    if (x1 < 0)
      x1 += b0;

    return x1;
  }
}