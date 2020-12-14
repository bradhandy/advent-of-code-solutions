package com.dbjgb.advent.twenty.fifteen.puzzle.sixteen;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {

  private static final Pattern AUNT_SUE_ATTRIBUTE = Pattern.compile("([^ ]+): (\\d+)");

  private static List<Map<String, Integer>> auntSues = new ArrayList<>();

  public static void main(String... args) throws IOException {
    parseAuntSueAttributes();
  }

  private static void parseAuntSueAttributes() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/sixteen/input.txt")) {
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher auntSueAttributMatcher = AUNT_SUE_ATTRIBUTE.matcher(line);
        Map<String, Integer> attributes = new HashMap<>();
        while (auntSueAttributMatcher.find()) {
          attributes.put(
              auntSueAttributMatcher.group(1), Integer.parseInt(auntSueAttributMatcher.group(2)));
        }
        auntSues.add(attributes);
      }

      removeSuesWithMatchingAttributeGreaterThan("cats", 7);
      removeSuesWithMatchingAttributeGreaterThan("trees", 3);
      removeSuesWithMatchingAttributeLessThan("pomeranians", 3);
      removeSuesWithMatchingAttributeLessThan("goldfish", 5);
      removeSuesNotMatchingAttribute("children", 3);
      removeSuesNotMatchingAttribute("samoyeds", 2);
      removeSuesNotMatchingAttribute("akitas", 0);
      removeSuesNotMatchingAttribute("vizslas", 0);
      removeSuesNotMatchingAttribute("cars", 2);
      removeSuesNotMatchingAttribute("perfumes", 1);

      List<Map<String, Integer>> remainingSues =
          auntSues.stream().filter(Objects::nonNull).collect(Collectors.toList());
      System.out.printf("Number of Sues left:  %d\n", remainingSues.size());
      for (int i = 0; i < auntSues.size(); i++) {
        if (auntSues.get(i) != null) {
          System.out.printf("%d\n", i + 1);
          break;
        }
      }
    }
  }

  private static void removeSuesNotMatchingAttribute(String attribute, int value) {
    removeSuesWithMatchingAttributeOutOfRange(attribute, value, value);
  }

  private static void removeSuesWithMatchingAttributeGreaterThan(String attribute, int value) {
    removeSuesWithMatchingAttributeOutOfRange(attribute, value + 1, Integer.MAX_VALUE);
  }

  private static void removeSuesWithMatchingAttributeLessThan(String attribute, int value) {
    removeSuesWithMatchingAttributeOutOfRange(attribute, 0, value - 1);
  }

  private static void removeSuesWithMatchingAttributeOutOfRange(
      String attribute, int min, int max) {
    for (int i = 0; i < auntSues.size(); i++) {
      boolean shouldRemove =
          Optional.ofNullable(auntSues.get(i))
              .map(
                  attributeMap -> {
                    Integer attributeValue =
                        Optional.ofNullable(attributeMap.get(attribute)).orElse(-1);
                    return attributeValue >= 0 && (attributeValue < min || max < attributeValue);
                  })
              .orElse(false);
      if (shouldRemove) {
        auntSues.set(i, null);
      }
    }
  }
}
