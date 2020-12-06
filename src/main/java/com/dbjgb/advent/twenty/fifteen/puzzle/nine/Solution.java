package com.dbjgb.advent.twenty.fifteen.puzzle.nine;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern DESTINATION_PATTERN = Pattern.compile("([^ ]+) to ([^ ]+) = (\\d+)");

  private static Map<String, City> cityMap = new HashMap<>();

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/nine/input.txt")) {
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher destinationMatcher = DESTINATION_PATTERN.matcher(line);
        if (destinationMatcher.matches()) {
          City origin = getCity(destinationMatcher.group(1));
          City destination = getCity(destinationMatcher.group(2));
          int distance = Integer.parseInt(destinationMatcher.group(3));

          origin.addDestination(destination, distance);
          destination.addDestination(origin, distance);
        }
      }

      List<String> cityNames = List.copyOf(cityMap.keySet());
      int minimumDistance = Integer.MAX_VALUE;
      int maximumDistance = Integer.MIN_VALUE;
      for (int i = 0; i < cityNames.size(); i++) {
        String originCityName = cityNames.get(i);
        for (int j = i + 1; j < (cityNames.size() + i); j++) {
          String destinationCityName = cityNames.get(j % cityNames.size());
          minimumDistance =
              Integer.min(
                  minimumDistance,
                  reduceToSinglePathDistanceFromOriginToDestinationThroughCities(
                      originCityName, destinationCityName, List.copyOf(cityNames), Integer::min));
          maximumDistance =
              Integer.max(
                  maximumDistance,
                  reduceToSinglePathDistanceFromOriginToDestinationThroughCities(
                      originCityName, destinationCityName, List.copyOf(cityNames), Integer::max));
        }
      }

      System.out.printf("Minimum distance is %d.\n", minimumDistance);
      System.out.printf("Maximum distance is %d.\n", maximumDistance);
    }
  }

  private static City getCity(String name) {
    City city = Objects.requireNonNullElseGet(cityMap.get(name), () -> new City(name));
    cityMap.put(name, city);

    return city;
  }

  private static int reduceToSinglePathDistanceFromOriginToDestinationThroughCities(
      String origin, String destination, List<String> cityNames, BinaryOperator<Integer> reducer) {
    List<String> cityNamesCopy = new ArrayList<>(cityNames);
    cityNamesCopy.removeAll(List.of(origin, destination));

    if (cityNamesCopy.isEmpty()) {
      return cityMap.get(origin).getDistanceToDestination(destination);
    }

    int distance = -1;
    for (int nextLayoverIndex = 0; nextLayoverIndex < cityNamesCopy.size(); nextLayoverIndex++) {
      for (int lastLayoutIndex = (nextLayoverIndex + 1);
          lastLayoutIndex < (cityNamesCopy.size() + nextLayoverIndex);
          lastLayoutIndex++) {
        String nextLayoverCityName = cityNamesCopy.get(nextLayoverIndex);
        String lastLayoverCityName = cityNamesCopy.get(lastLayoutIndex % cityNamesCopy.size());
        int distanceFromOriginToNextLayover =
            cityMap.get(origin).getDistanceToDestination(nextLayoverCityName);
        int distanceFromLastLayoverToDestination =
            cityMap.get(lastLayoverCityName).getDistanceToDestination(destination);
        int shortestLayoverRoute =
            reduceToSinglePathDistanceFromOriginToDestinationThroughCities(
                nextLayoverCityName, lastLayoverCityName, List.copyOf(cityNamesCopy), reducer);

        int calculatedDistance =
            distanceFromOriginToNextLayover
                + shortestLayoverRoute
                + distanceFromLastLayoverToDestination;
        if (distance == -1) {
          distance = calculatedDistance;
        } else {
          distance = reducer.apply(distance, calculatedDistance);
        }
      }
    }

    return distance;
  }

  private static class City {

    private final String name;
    private Map<String, Destination> destinations;

    public City(String name) {
      this.name = name;
      this.destinations = new HashMap<>();
    }

    public void addDestination(City city, int distance) {
      destinations.put(city.name, new Destination(city, distance));
    }

    public int getDistanceToDestination(String city) {
      return destinations.get(city).getDistance();
    }
  }

  private static class Destination {

    private final City city;
    private final int distance;

    public Destination(City city, int distance) {
      this.city = city;
      this.distance = distance;
    }

    public City getCity() {
      return city;
    }

    public int getDistance() {
      return distance;
    }
  }
}
