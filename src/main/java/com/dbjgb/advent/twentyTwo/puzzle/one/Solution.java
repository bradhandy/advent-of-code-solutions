package com.dbjgb.advent.twentyTwo.puzzle.one;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solution {

  public static void main(String... args) throws Exception {
    List<String> calorieEntries =
        Files.readAllLines(
            Paths.get(
                Thread.currentThread()
                    .getContextClassLoader()
                    .getResource("com/dbjgb/advent/twentyTwo/puzzle/one/input.txt")
                    .toURI()));
    List<BigInteger> calorieTotals = new ArrayList<>();
    BigInteger singleElfTotal = BigInteger.ZERO;
    BigInteger largestCalorieTotal = BigInteger.ZERO;

    for (String calorieEntry : calorieEntries) {
      if (calorieEntry.equals("")) {
        calorieTotals.add(singleElfTotal);
        if (singleElfTotal.compareTo(largestCalorieTotal) > 0) {
          largestCalorieTotal = singleElfTotal;
        }
        singleElfTotal = BigInteger.ZERO;
        continue;
      }

      BigInteger calories = new BigInteger(calorieEntry);
      singleElfTotal = singleElfTotal.add(calories);
    }

    Collections.sort(calorieTotals);
    int numberOfElves = calorieTotals.size();

    System.out.printf("Largest calorie total is %s.\n", largestCalorieTotal);
    System.out.printf(
        "Sum of top three is %s.\n",
        calorieTotals
            .get(numberOfElves - 1)
            .add(calorieTotals.get(numberOfElves - 2))
            .add(calorieTotals.get(numberOfElves - 3)));
  }
}
