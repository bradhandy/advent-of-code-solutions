package com.dbjgb.advent._2020.puzzle._25;

public class Solution {

  private static final long CARD_PUBLIC_KEY = 14205034;
  private static final long DOOR_PUBLIC_KEY = 18047856;

  public static void main(String... args) {
    long cardLoopCount = determineLoopCountForKey(CARD_PUBLIC_KEY);
    System.out.printf("Card Count: %d\n", cardLoopCount);

    long doorLoopCount = determineLoopCountForKey(DOOR_PUBLIC_KEY);
    System.out.printf("Door Count: %d\n", doorLoopCount);

    long value = 1;
    for (long i = 0; i < cardLoopCount; i++) {
      value = (value * DOOR_PUBLIC_KEY) % 20201227;
    }
    System.out.printf("Value: %d\n", value);

    value = 1;
    for (long i = 0; i < doorLoopCount; i++) {
      value = (value * CARD_PUBLIC_KEY) % 20201227;
    }

    System.out.printf("Value: %d\n", value);
  }

  private static long determineLoopCountForKey(long publicKey) {
    long value = 1;
    long count = 0;
    while (value != publicKey && count++ >= 0) {
      value = (value * 7) % 20201227;
    }

    return count;
  }
}
