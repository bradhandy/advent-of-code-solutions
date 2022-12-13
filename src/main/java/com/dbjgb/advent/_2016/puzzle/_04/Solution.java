package com.dbjgb.advent._2016.puzzle._04;

import com.dbjgb.advent.Utility;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern ROOM_ID_PATTERN =
      Pattern.compile("((?:[a-z]+)(?:-(?:[a-z]+))+)-(\\d+)\\[([a-z]+)]");

  public static void main(String... args) throws Exception {
    List<String> rooms = Utility.readAllLines("_2016/puzzle/_04/input.txt");
    BigInteger sectorIdTotal = BigInteger.ZERO;

    for (String room : rooms) {
      Matcher roomMatcher = ROOM_ID_PATTERN.matcher(room);
      if (!roomMatcher.matches()) {
        throw new IllegalStateException(String.format("'%s' is not a valid room.", room));
      }

      String checksum = calculateChecksum(roomMatcher.group(1));
      if (checksum.equals(roomMatcher.group(3))) {
        sectorIdTotal = sectorIdTotal.add(new BigInteger(roomMatcher.group(2)));
        String roomName = decryptString(roomMatcher.group(1),
            Integer.parseInt(roomMatcher.group(2)));
        if (roomName.contains("pole")) {
          System.out.println(roomMatcher.group(2));
        }
      }
    }

    System.out.println(sectorIdTotal);
  }

  private static String decryptString(String value, int sectorId) {
    char[] decryptedValue = new char[value.length()];
    int index = 0;
    for (char character : value.toCharArray()) {
      if (character == '-') {
        decryptedValue[index++] = ' ';
        continue;
      }

      int offset = ((int) character) - 97;
      offset = ((sectorId + offset) % 26) + 97;
      decryptedValue[index++] = (char) offset;
    }

    return new String(decryptedValue);
  }

  private static String calculateChecksum(String value) {
    Map<Character, Integer> characterUsage = new HashMap<>();
    for (Character character : value.toCharArray()) {
      if (character == '-') {
        continue;
      }
      characterUsage.put(character, characterUsage.getOrDefault(character, 0) + 1);
    }

    List<Map.Entry<Character, Integer>> usages = new ArrayList<>(characterUsage.entrySet());
    Collections.sort(usages,
        Comparator.<Map.Entry<Character, Integer>>comparingInt(Map.Entry::getValue)
            .reversed()
            .thenComparing(Map.Entry::getKey));

    StringBuilder checksum = new StringBuilder();
    for (Map.Entry<Character, Integer> usage : usages.subList(0, 5)) {
      checksum.append(usage.getKey());
    }

    return checksum.toString();
  }
}
