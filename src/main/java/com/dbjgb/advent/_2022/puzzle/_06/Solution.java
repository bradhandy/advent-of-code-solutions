package com.dbjgb.advent._2022.puzzle._06;

import com.dbjgb.advent.Utility;

import java.util.HashSet;
import java.util.Set;

public class Solution {

  public static void main(String... args) throws Exception {
    String signal = Utility.readEntireFile("_2022/puzzle/_06/input.txt");
    System.out.printf("Signal length %d.\n", signal.length());
    for (int i = 0; i <= signal.length() - 4; i++) {
      if (allCharactersDifferent(signal.substring(i, i+4).toCharArray())) {
        System.out.println(i + 4);
        break;
      }
    }
    for (int i = 0; i <= signal.length() - 14; i++) {
      if (allCharactersDifferent(signal.substring(i, i+14).toCharArray())) {
        System.out.println(i + 14);
        break;
      }
    }
  }

  private static boolean allCharactersDifferent(char[] characters) {
    Set<Character> uniqueCharacters = new HashSet<>();
    for (char character : characters) {
      uniqueCharacters.add(character);
    }

    return uniqueCharacters.size() == characters.length;
  }
}
