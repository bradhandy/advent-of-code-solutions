package com.dbjgb.advent.twenty.puzzle.seven;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern BAG_REQUIREMENT_PATTERN =
      Pattern.compile("(.*?) bags contain (no other bags\\.)?");
  private static final Pattern ADDITIONAL_BAG_REQUIREMENTS_PATTERN =
      Pattern.compile("(\\d+) (.*?) bag(s)?");

  private static Map<String, Bag> bagMap = new HashMap<>();

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/seven/input.txt")) {
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher bagRequirementMatch = BAG_REQUIREMENT_PATTERN.matcher(line);
        if (bagRequirementMatch.find()) {
          if (Objects.requireNonNullElse(bagRequirementMatch.group(2), "").startsWith("no other")) {
            ensureBagExistsInCache(bagRequirementMatch.group(1));
            continue;
          }

          Bag bag = ensureBagExistsInCache(bagRequirementMatch.group(1));
          Matcher additionalRequirementMatcher = ADDITIONAL_BAG_REQUIREMENTS_PATTERN.matcher(line);
          while (additionalRequirementMatcher.find()) {
            Bag requiredBag = ensureBagExistsInCache(additionalRequirementMatcher.group(2));
            bag.addBagRequirement(
                new BagRequirement(
                    requiredBag, Integer.parseInt(additionalRequirementMatcher.group(1))));
          }
        }
      }

      int totalContainingShinyGoldBag = 0;
      int totalContainingDirectly = 0;
      for (Bag bag : bagMap.values()) {
        if (bag.canContainNested(bagMap.get("shiny gold"))) {
          totalContainingShinyGoldBag += 1;
        }
        if (bag.canContainDirectly(bagMap.get("shiny gold"))) {
          totalContainingDirectly += 1;
        }
      }

      System.out.printf("Total Bags containing Shiny Gold bag:  %d\n", totalContainingShinyGoldBag);
      System.out.printf(
          "Total Bags inside a Shiny Gold bag:  %d\n",
          bagMap.get("shiny gold").getTotalRequiredNestedBags());
      System.out.printf(
          "Total Bags containing Shiny Gold bag directly:  %d\n", totalContainingDirectly);
    }
  }

  private static Bag ensureBagExistsInCache(String color) {
    Bag bag = Objects.requireNonNullElseGet(bagMap.get(color), () -> new Bag(color));
    bagMap.put(color, bag);

    return bag;
  }

  private static class Bag {

    private final String color;
    private final Map<String, BagRequirement> bagRequirementMap = new HashMap<>();

    public Bag(String color) {
      this.color = color;
    }

    public void addBagRequirement(BagRequirement bagRequirement) {
      bagRequirementMap.put(bagRequirement.bag.color, bagRequirement);
    }

    public int getTotalRequiredNestedBags() {
      int totalRequiredNested = 0;
      for (BagRequirement bagRequirement : bagRequirementMap.values()) {
        totalRequiredNested += bagRequirement.quantity;
        totalRequiredNested +=
            (bagRequirement.quantity * bagRequirement.bag.getTotalRequiredNestedBags());
      }

      return totalRequiredNested;
    }

    public boolean canContainDirectly(Bag bag) {
      return bagRequirementMap.containsKey(bag.color);
    }

    public boolean canContainNested(Bag bag) {
      if (canContainDirectly(bag)) {
        return true;
      }

      for (BagRequirement bagRequirement : bagRequirementMap.values()) {
        if (bagRequirement.bag.canContainNested(bag)) {
          return true;
        }
      }

      return false;
    }
  }

  private static class BagRequirement {

    private final Bag bag;
    private final int quantity;

    public BagRequirement(Bag bag, int quantity) {
      this.bag = bag;
      this.quantity = quantity;
    }
  }
}
