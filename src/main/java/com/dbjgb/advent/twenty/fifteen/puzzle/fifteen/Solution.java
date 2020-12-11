package com.dbjgb.advent.twenty.fifteen.puzzle.fifteen;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern COOKIE_PROPERTY_PATTERN = Pattern.compile("([^ ]+) (-?\\d+)");

  public static void main(String... args) throws Exception {
    List<CookieProperty> properties = parseCookieProperties();

    int maxScore = 0;
    int maxFiveHundredCalorieScore = 0;
    for (int sugar = 0; sugar <= 100; sugar++) {
      int maxSprinkles = 100 - sugar;
      for (int sprinkles = 0; sprinkles <= maxSprinkles; sprinkles++) {
        for (int candy = 0, chocolate = 100 - (sugar + sprinkles);
            chocolate >= 0;
            candy++, chocolate--) {
          int score = 1;
          int fiveHundredCalorieScore = 1;
          int[] quantities = new int[] {sugar, sprinkles, candy, chocolate};
          for (CookieProperty property : properties) {
            if (property.getName().equals("calories")) {
              int calories = property.scoreForQuantities(quantities);
              if (calories != 500) {
                fiveHundredCalorieScore = 0;
              }
              continue;
            }

            int propertyScore = property.scoreForQuantities(quantities);
            score *= propertyScore;
            fiveHundredCalorieScore *= propertyScore;
          }

          maxScore = Integer.max(maxScore, score);
          maxFiveHundredCalorieScore =
              Integer.max(maxFiveHundredCalorieScore, fiveHundredCalorieScore);
        }
      }
    }

    System.out.printf("Max Score: %d\n", maxScore);
    System.out.printf("Max 500-calorie Score: %d\n", maxFiveHundredCalorieScore);
  }

  private static final List<CookieProperty> parseCookieProperties() throws IOException {
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/fifteen/input.txt")) {
      Map<String, CookieProperty> cookiePropertyMap = new HashMap<>();
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher cookiePropertyMatcher = COOKIE_PROPERTY_PATTERN.matcher(line);
        while (cookiePropertyMatcher.find()) {
          String cookiePropertyName = cookiePropertyMatcher.group(1);
          CookieProperty cookieProperty =
              cookiePropertyMap.computeIfAbsent(cookiePropertyName, CookieProperty::new);
          cookieProperty.addWeight(Integer.parseInt(cookiePropertyMatcher.group(2)));
        }
      }

      return List.copyOf(cookiePropertyMap.values());
    }
  }
}

class CookieProperty {

  private final String name;
  private List<Integer> weights;

  public CookieProperty(String name) {
    this.name = name;
    this.weights = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public void addWeight(int weight) {
    weights.add(weight);
  }

  public int scoreForQuantities(int[] quantities) {
    int score = 0;
    for (int i = 0; i < quantities.length; i++) {
      score += (weights.get(i) * quantities[i]);
    }

    return Integer.max(0, score);
  }
}
