package com.dbjgb.advent.twenty.twenty.puzzle.sixteen;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;

public class Solution {

  private static final Pattern FIELD_PATTERN = compile("([^:]+): (\\d+)-(\\d+) or (\\d+)-(\\d+)");
  private static final Pattern DIGIT_PATTERN = compile("(\\d+)");

  public static void main(String... args) throws Exception {
    printInvalidFieldSum();
    printDepartureFieldProduct();
  }

  private static void printDepartureFieldProduct() throws IOException {
    Map<Field, Integer> fieldPositionMap = new HashMap<>();
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/sixteen/input.txt")) {
      Set<Field> fields = new HashSet<>(parseFields(inputReader));
      List<Set<Field>> possibleFieldSets = new ArrayList<>(20);
      String[] ticketFields = new String[0];

      String line;
      while ((line = inputReader.readLine()) != null) {
        if (line.equals("your ticket:")) {
          line = inputReader.readLine();
          ticketFields = line.split(",");
          continue;
        }

        Matcher fieldValueMatcher = DIGIT_PATTERN.matcher(line);
        int position = 0;
        while (fieldValueMatcher.find()) {
          int fieldValue = Integer.parseInt(fieldValueMatcher.group(1));
          Set<Field> availableFields =
              possibleFieldSets.size() > position
                  ? possibleFieldSets.get(position)
                  : new HashSet<>(fields);
          Set<Field> possibleFields =
              availableFields.stream()
                  .filter(field -> field.inRange(fieldValue))
                  .collect(Collectors.toSet());
          if (possibleFields.isEmpty()) {
            break;
          }

          availableFields.retainAll(possibleFields);
          if (possibleFieldSets.size() > position) {
            possibleFieldSets.set(position, availableFields);
          } else {
            possibleFieldSets.add(availableFields);
          }
          position += 1;
        }
      }

      /*
       * Only retain the fields available for selection.  If the number of available fields is one,
       * then save the field and it's position in the fieldPositionMap.
       */
      boolean madeChange;
      do {
        madeChange = false;
        for (int i = 0; i < possibleFieldSets.size(); i++) {
          Set<Field> possibleFields = possibleFieldSets.get(i);
          madeChange |= possibleFields.retainAll(fields);
          if (possibleFields.size() == 1) {
            Field field = possibleFields.stream().findFirst().get();
            fieldPositionMap.put(field, i);
            fields.remove(field);
          }
        }
      } while (madeChange);

      Set<Field> departureFields =
          fieldPositionMap.keySet().stream()
              .filter(field -> field.getName().contains("departure"))
              .collect(Collectors.toSet());
      long departureFieldProduct = 1;
      for (Field departureField : departureFields) {
        departureFieldProduct *=
            Long.parseLong(ticketFields[fieldPositionMap.get(departureField)]);
      }

      System.out.printf("Departure field product:  %d\n", departureFieldProduct);
    }
  }

  private static void printInvalidFieldSum() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/sixteen/input.txt")) {
      List<Field> fields = parseFields(inputReader);

      int invalidFieldSum = 0;
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher fieldValueMatcher = DIGIT_PATTERN.matcher(line);
        while (fieldValueMatcher.find()) {
          int fieldValue = Integer.parseInt(fieldValueMatcher.group(1));
          if (fields.stream().noneMatch(field -> field.inRange(fieldValue))) {
            invalidFieldSum += fieldValue;
          }
        }
      }

      System.out.printf("Invalid field value sum:  %d\n", invalidFieldSum);
    }
  }

  private static List<Field> parseFields(BufferedReader inputReader) throws IOException {
    List<Field> fields = new ArrayList<>();
    String line;
    while ((line = inputReader.readLine()) != null) {
      Matcher fieldMatcher = FIELD_PATTERN.matcher(line);
      if (!fieldMatcher.matches()) {
        break;
      }

      List<Range> validRanges =
          List.of(
              new Range(
                  Integer.parseInt(fieldMatcher.group(2)), Integer.parseInt(fieldMatcher.group(3))),
              new Range(
                  Integer.parseInt(fieldMatcher.group(4)),
                  Integer.parseInt(fieldMatcher.group(5))));
      fields.add(new Field(fieldMatcher.group(1), validRanges));
    }

    return fields;
  }
}

class Field {

  private final String name;
  private final List<Range> validRanges;

  public Field(String name, List<Range> validRanges) {
    this.name = name;
    this.validRanges = List.copyOf(validRanges);
  }

  public String getName() {
    return name;
  }

  public boolean inRange(int number) {
    return validRanges.stream().anyMatch(range -> range.inRange(number));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Field field = (Field) o;
    return name.equals(field.name) && validRanges.equals(field.validRanges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, validRanges);
  }
}

class Range {

  private final int begin;
  private final int end;

  public Range(int begin, int end) {
    this.begin = begin;
    this.end = end;
  }

  public boolean inRange(int number) {
    return begin <= number && number <= end;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Range range = (Range) o;
    return begin == range.begin && end == range.end;
  }

  @Override
  public int hashCode() {
    return Objects.hash(begin, end);
  }
}
