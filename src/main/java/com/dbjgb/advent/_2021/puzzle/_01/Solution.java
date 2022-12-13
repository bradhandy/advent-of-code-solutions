package com.dbjgb.advent._2021.puzzle._01;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;

public class Solution {

  public static void main(String... args) throws Exception {
    printNumberOfIncreasedSingleMeasurements();
    printNumberOfIncreasedGroupedMeasurements();
  }

  private static void printNumberOfIncreasedSingleMeasurements() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("_2021/puzzle/_01/input.txt")) {
      int previousReading = -1;
      int numberOfIncreasedReadings = 0;
      String line;
      while ((line = inputReader.readLine()) != null) {
        int reading = Integer.parseInt(line);
        if (previousReading != -1 && previousReading < reading) {
          numberOfIncreasedReadings++;
        }
        previousReading = reading;
      }

      System.out.printf("Increased Readings: %d\n", numberOfIncreasedReadings);
    }
  }

  private static void printNumberOfIncreasedGroupedMeasurements() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("_2021/puzzle/_01/input.txt")) {
      int[] measurements = new int[4];
      int previousReading = 0;
      int numberOfMeasurementsProcessed = 0;
      int numberOfIncreasedReadings = 0;
      String line;
      while ((line = inputReader.readLine()) != null) {
        int currentMeasurement = Integer.parseInt(line);
        measurements[numberOfMeasurementsProcessed % 4] = currentMeasurement;

        int currentReading = previousReading + currentMeasurement;
        if (numberOfMeasurementsProcessed >= 3) {
          currentReading -= measurements[(numberOfMeasurementsProcessed + 1) % 4];
          if (currentReading > previousReading) {
            numberOfIncreasedReadings++;
          }
        }

        previousReading = currentReading;
        numberOfMeasurementsProcessed++;
      }

      System.out.printf("Increased Readings: %d\n", numberOfIncreasedReadings);
    }
  }
}
