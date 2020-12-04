package com.dbjgb.advent.twenty.nineteen.puzzle.one;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;

public class Solution {
  private static final BigInteger DENOMINATOR = new BigInteger("3");
  private static final BigInteger DIFFERENCE = new BigInteger("2");

  public static void main(String... args) throws Exception {
    printFuelRequiredForJustModuleMass();
    printFuelRequiredForModuleAndFuelMass();
  }

  private static void printFuelRequiredForJustModuleMass() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("nineteen/puzzle/one/input.txt")) {
      BigInteger totalFuel = BigInteger.ZERO;
      String line;
      while ((line = inputReader.readLine()) != null) {
        BigInteger mass = new BigInteger(line, 10);
        totalFuel = totalFuel.add(mass.divide(DENOMINATOR).subtract(DIFFERENCE));
      }

      System.out.printf("Fuel required: %d\n", totalFuel);
    }
  }

  private static void printFuelRequiredForModuleAndFuelMass() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("nineteen/puzzle/one/input.txt")) {
      BigInteger totalFuel = BigInteger.ZERO;
      String line;
      while ((line = inputReader.readLine()) != null) {
        BigInteger mass = new BigInteger(line, 10);
        totalFuel = totalFuel.add(calculateFuelForModuleMass(mass));
      }

      System.out.printf("Fuel required: %d\n", totalFuel);
    }
  }

  private static BigInteger calculateFuelForModuleMass(BigInteger mass) {
    BigInteger fuelAmount = mass.divide(DENOMINATOR).subtract(DIFFERENCE);
    BigInteger totalFuel = BigInteger.ZERO;

    while (fuelAmount.compareTo(BigInteger.ZERO) > 0) {
      totalFuel = totalFuel.add(fuelAmount);
      fuelAmount = fuelAmount.divide(DENOMINATOR).subtract(DIFFERENCE);
    }

    return totalFuel;
  }
}
