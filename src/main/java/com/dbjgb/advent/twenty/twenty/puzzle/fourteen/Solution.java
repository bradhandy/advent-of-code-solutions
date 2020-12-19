package com.dbjgb.advent.twenty.twenty.puzzle.fourteen;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern MASK_PATTERN = Pattern.compile("mask = ([01X]+)");
  private static final Pattern MEMORY_ADDRESS_VALUE_PATTERN =
      Pattern.compile("\\D+(\\d+)\\D+(\\d+)");

  public static void main(String... args) throws Exception {
    printSumOfModifiedValues();
    printSumOfValuesForModifiedKeys();
  }

  private static void printSumOfValuesForModifiedKeys() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/fourteen/input.txt")) {
      Map<String, BigInteger> memory = new HashMap<>();
      final char[] currentMask = new char[36];
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher maskMatcher = MASK_PATTERN.matcher(line);
        if (maskMatcher.matches()) {
          char[] mask = maskMatcher.group(1).toCharArray();
          System.arraycopy(mask, 0, currentMask, 0, currentMask.length);
          continue;
        }

        Matcher valueMatcher = MEMORY_ADDRESS_VALUE_PATTERN.matcher(line);
        if (valueMatcher.matches()) {
          BigInteger value = new BigInteger(valueMatcher.group(2));
          for (BigInteger key : createKeys(new BigInteger(valueMatcher.group(1)), currentMask)) {
            memory.put(key.toString(), value);
          }
        }
      }

      BigInteger finalTotal = memory.values().stream().reduce(BigInteger.ZERO, BigInteger::add);
      System.out.printf("The final total (splitting keys) is %d.\n", finalTotal);
    }
  }

  private static void printSumOfModifiedValues() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/fourteen/input.txt")) {
      Map<String, BigInteger> memory = new HashMap<>();
      final char[] currentMask = new char[36];
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher maskMatcher = MASK_PATTERN.matcher(line);
        if (maskMatcher.matches()) {
          char[] mask = maskMatcher.group(1).toCharArray();
          System.arraycopy(mask, 0, currentMask, 0, currentMask.length);
          continue;
        }

        Matcher valueMatcher = MEMORY_ADDRESS_VALUE_PATTERN.matcher(line);
        if (valueMatcher.matches()) {
          BigInteger value = new BigInteger(valueMatcher.group(2));
          for (int index = 0; index < currentMask.length; index++) {
            value = alterValueBit(value, currentMask[index], (currentMask.length - (index + 1)));
          }
          memory.put(valueMatcher.group(1), value);
        }
      }

      BigInteger finalTotal = memory.values().stream().reduce(BigInteger.ZERO, BigInteger::add);
      System.out.printf("The final total is %d.\n", finalTotal);
    }
  }

  private static BigInteger alterValueBit(BigInteger value, char maskChar, int bitToModify) {
    if (maskChar == '0') {
      return value.clearBit(bitToModify);
    } else if (maskChar == '1') {
      return value.setBit(bitToModify);
    }

    return value;
  }

  private static Set<BigInteger> createKeys(BigInteger key, char[] mask) {
    Set<BigInteger> keys = new HashSet<>(Set.of(key));
    for (int index = 0; index < mask.length; index++) {
      Set<BigInteger> workingSet = Set.copyOf(keys);
      for (BigInteger workingKey : workingSet) {
        if (mask[index] == '0') {
          continue;
        }

        int bitToModify = mask.length - (index + 1);
        BigInteger modifiedKey = alterValueBit(workingKey, mask[index], bitToModify);
        if (workingKey.equals(modifiedKey)) {
          if (mask[index] == 'X') {
            keys.add(workingKey.flipBit(bitToModify));
          }
        } else {
          keys.remove(workingKey);
          keys.add(modifiedKey);
        }
      }
    }

    return keys;
  }
}
