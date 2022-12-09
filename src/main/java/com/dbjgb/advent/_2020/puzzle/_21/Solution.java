package com.dbjgb.advent._2020.puzzle._21;

import com.dbjgb.advent.Utility;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern ALLERGEN_LIST_PATTERN = Pattern.compile("\\(contains ([^\\)]+)\\)");
  private static final Pattern LIST_PATTERN = Pattern.compile(",? ");

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("_2020/puzzle/_21/input.txt")) {
      Map<String, Set<String>> allergenToPossibiliesMap = new TreeMap<>();
      List<Set<String>> ingredientsLists = new ArrayList<>();
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher allergenListMatcher = ALLERGEN_LIST_PATTERN.matcher(line);
        if (allergenListMatcher.find()) {
          String ingredientsList = line.substring(0, allergenListMatcher.start() - 1);
          Set<String> ingredients =
              new HashSet<>(Arrays.asList(LIST_PATTERN.split(ingredientsList)));
          ingredientsLists.add(ingredients);

          for (String allergenName : LIST_PATTERN.split(allergenListMatcher.group(1))) {
            allergenToPossibiliesMap.compute(
                allergenName,
                (allergenKey, possibilities) -> {
                  if (possibilities == null) {
                    return new HashSet<>(ingredients);
                  }

                  possibilities.retainAll(ingredients);
                  return possibilities;
                });
          }
        }
      }

      while (allergenToPossibiliesMap.values().stream()
          .anyMatch(possibilites -> possibilites.size() > 1)) {
        Set<String> discovered =
            allergenToPossibiliesMap.values().stream()
                .filter(possibilites -> possibilites.size() == 1)
                .reduce(
                    new HashSet<>(),
                    (left, right) -> {
                      Set<String> combined = new HashSet<>(left);
                      combined.addAll(right);

                      return combined;
                    });
        for (String allergen : allergenToPossibiliesMap.keySet()) {
          Set<String> possibilities = allergenToPossibiliesMap.get(allergen);
          if (possibilities.size() > 1) {
            possibilities.removeAll(discovered);
            if (possibilities.size() == 1) {
              discovered.addAll(possibilities);
            }
          }
        }
      }

      Set<String> discovered =
          allergenToPossibiliesMap.values().stream()
              .filter(possibilites -> possibilites.size() == 1)
              .reduce(
                  new HashSet<>(),
                  (left, right) -> {
                    Set<String> combined = new HashSet<>(left);
                    combined.addAll(right);

                    return combined;
                  });

      int numberOfIngredientAppearances = 0;
      for (Set<String> ingredientsList : ingredientsLists) {
        ingredientsList.removeAll(discovered);
        numberOfIngredientAppearances += ingredientsList.size();
      }

      System.out.printf("Part one answer: %d\n", numberOfIngredientAppearances);

      for (String allergen : allergenToPossibiliesMap.keySet()) {
        System.out.printf(
            "%s could be %s.\n",
            allergen, Joiner.on(", ").join(allergenToPossibiliesMap.get(allergen)));
      }

      StringBuilder canonicalList = new StringBuilder();
      allergenToPossibiliesMap
          .values()
          .forEach(
              possibilities -> {
                if (canonicalList.length() > 0) {
                  canonicalList.append(',');
                }
                canonicalList.append(Iterables.getOnlyElement(possibilities));
              });
      System.out.printf("Canonical List: %s\n", canonicalList);
    }
  }
}