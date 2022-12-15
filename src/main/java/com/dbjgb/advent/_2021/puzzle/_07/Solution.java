package com.dbjgb.advent._2021.puzzle._07;

import com.dbjgb.advent.Utility;

import java.util.ArrayList;
import java.util.List;

public class Solution {

  public static void main(String... args) throws Exception {
    String[] positionValues = Utility.readEntireFile("_2021/puzzle/_07/input.txt").split(",");
    List<Integer> positions = new ArrayList<>();
    for (String position : positionValues) {
      positions.add(Integer.parseInt(position));
    }

    int maxPosition = positions.stream().reduce(0, Math::max);

    List<Integer> finalPositions = new ArrayList<>(maxPosition);
    List<Integer> partTwoFinalPositions = new ArrayList<>(maxPosition);
    for (int positionIndex = 0; positionIndex < positions.size(); positionIndex++) {
      for (int position = 0; position <= maxPosition; position++) {
        int currentPosition = positions.get(positionIndex);
        int fuelCost = Math.max(currentPosition, position) - Math.min(currentPosition, position);
        if (finalPositions.size() > position) {
          int totalFuelCost = finalPositions.get(position);
          finalPositions.set(position, totalFuelCost + fuelCost);
          int partTwoFuelCost = partTwoFinalPositions.get(position);
          partTwoFinalPositions.set(position, partTwoFuelCost + sumToOne(fuelCost));
        } else {
          finalPositions.add(fuelCost);
          partTwoFinalPositions.add(sumToOne(fuelCost));
        }
      }
    }

    System.out.printf(
        "Least fuel cost: %d\n", finalPositions.stream().reduce(Integer.MAX_VALUE, Math::min));
    System.out.printf(
        "Least fuel cost (part 2): %d\n",
        partTwoFinalPositions.stream().reduce(Integer.MAX_VALUE, Math::min));
  }

  private static int sumToOne(int start) {
    if (start < 2) {
      return start;
    }

    int currentValue = start;
    int total = 0;
    while (currentValue > 0) {
      total += currentValue;
      currentValue -= 1;
    }

    return total;
  }
}
