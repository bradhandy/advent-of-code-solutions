package com.dbjgb.advent.twenty.fifteen.puzzle.twelve;

import com.dbjgb.advent.Utility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern NUMBER_PATTERN = Pattern.compile("(-?\\d+)");
  private static final TypeReference<Map<String, Object>> GENERIC_OBJECT_TYPE =
      new TypeReference<>() {};

  public static void main(String... args) throws Exception {
    printSumOfAllNumberValues();
    printSumOfAllNonRedNumberValues();
  }

  private static void printSumOfAllNumberValues() throws IOException {
    int totalOfAllNumbers = 0;

    String line;
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/twelve/input.txt")) {
      while ((line = inputReader.readLine()) != null) {
        Matcher numberMatcher = NUMBER_PATTERN.matcher(line);
        while (numberMatcher.find()) {
          totalOfAllNumbers += Integer.parseInt(numberMatcher.group(1));
        }
      }
    }

    System.out.printf("Total of all numbers:  %d\n", totalOfAllNumbers);
  }

  private static void printSumOfAllNonRedNumberValues() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> data =
        objectMapper.readValue(
            objectMapper.createParser(Utility.openInputStream("fifteen/puzzle/twelve/input.txt")),
            GENERIC_OBJECT_TYPE);

    System.out.printf("Sum of all non-red elements:  %d\n", sumElementsInMap(data));
  }

  private static long sumElementsInList(List<Object> data) {
    long sum = 0L;
    for (Object element : data) {
      if (element instanceof Number) {
        sum += ((Number) element).longValue();
      } else if (element instanceof Map) {
        sum += sumElementsInMap((Map<String, Object>) element);
      } else if (element instanceof List) {
        sum += sumElementsInList((List<Object>) element);
      }
    }

    return sum;
  }

  private static long sumElementsInMap(Map<String, Object> data) {
    long sum = 0L;

    Optional<String> redValue =
        data.values().stream()
            .filter(value -> value instanceof String)
            .map(Object::toString)
            .filter(value -> ((String) value).equalsIgnoreCase("red"))
            .findFirst();
    if (redValue.isPresent()) {
      return 0;
    }

    for (Object value : data.values()) {
      if (value instanceof Number) {
        sum += ((Number) value).longValue();
      } else if (value instanceof Map) {
        sum += sumElementsInMap((Map<String, Object>) value);
      } else if (value instanceof List) {
        sum += sumElementsInList((List<Object>) value);
      }
    }

    return sum;
  }
}
