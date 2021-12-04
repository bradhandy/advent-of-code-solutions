package com.dbjgb.advent.twentyOne.puzzle.three;

import com.dbjgb.advent.Utility;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;

public class Solution {

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("twentyOne/puzzle/three/input.txt")) {
      Multimap<String, String> codeIndex = ArrayListMultimap.create();
      int numberOfCodes = 0;
      String line;
      int[] onesCount = null;

      while ((line = inputReader.readLine()) != null) {
        if (onesCount == null) {
          onesCount = new int[line.length()];
        }

        for (int bitIndex = 0; bitIndex < line.length(); bitIndex++) {
          codeIndex.put(line.substring(0, bitIndex + 1), line);
          if (line.charAt(bitIndex) == '1') {
            onesCount[bitIndex] += 1;
          }
        }

        numberOfCodes++;
      }

      int gammaRate = 0;
      int epsilonRate = 0;
      for (int count : onesCount) {
        gammaRate <<= 1;
        epsilonRate <<= 1;
        if (numberOfCodes - count > count) {
          gammaRate |= 1;
        } else {
          epsilonRate |= 1;
        }
      }

      System.out.printf(
          "Power Consumption (%d * %d): %d\n", gammaRate, epsilonRate, gammaRate * epsilonRate);

      String oxygenRatingString = "";
      String coTwoRatingString = "";
      for (int index = 0; index < onesCount.length; index++) {
        if (oxygenRatingString.length() < onesCount.length) {
          int oxygenOnesCount = codeIndex.get(oxygenRatingString + "1").size();
          int oxygenZeroesCount = codeIndex.get(oxygenRatingString + "0").size();
          if (oxygenZeroesCount <= oxygenOnesCount) {
            oxygenRatingString += "1";
            if (codeIndex.get(oxygenRatingString).size() == 1) {
              oxygenRatingString = Iterables.getOnlyElement(codeIndex.get(oxygenRatingString));
            }
          } else {
            oxygenRatingString += "0";
          }

        }
        if (coTwoRatingString.length() < onesCount.length) {
          int coTwoOnesCount = codeIndex.get(coTwoRatingString + "1").size();
          int coTwoZeroesCount = codeIndex.get(coTwoRatingString + "0").size();
          if (coTwoOnesCount < coTwoZeroesCount) {
            coTwoRatingString += "1";
          } else {
            coTwoRatingString += "0";
            if (codeIndex.get(coTwoRatingString).size() == 1) {
              coTwoRatingString = Iterables.getOnlyElement(codeIndex.get(coTwoRatingString));
            }
          }

        }
      }

      int oxygenRating = Integer.parseInt(oxygenRatingString, 2);
      int coTwoScubberRating = Integer.parseInt(coTwoRatingString, 2);
      System.out.printf(
          "Oxygen Rating: %d; CO2 Rating: %d; Lift Support: %d",
          oxygenRating, coTwoScubberRating, oxygenRating * coTwoScubberRating);
    }
  }
}
