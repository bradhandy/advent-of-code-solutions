package com.dbjgb.advent.twenty.twenty.puzzle.nineteen;

import com.dbjgb.advent.Utility;
import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern RULE_PATTERN =
      Pattern.compile("(\\d+): (\\d+|\\\"(\\w+)\\\")(?: (\\d+))?(?: \\| (?:(\\d+)(?: (\\d+))?))?");

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/nineteen/input.txt")) {
      Map<String, Rule> rulesMap = parseRules(inputReader);
      Pattern valuePattern = Pattern.compile(rulesMap.get("0").getRulePattern());
      int totalMatching = 0;
      String line;
      while ((line = inputReader.readLine()) != null) {
        if (valuePattern.matcher(line).matches()) {
          totalMatching += 1;
        }
      }

      System.out.printf("Total Matching:  %d\n", totalMatching);
    }
  }

  private static Map<String, Rule> parseRules(BufferedReader inputReader) throws IOException {
    Map<String, Rule> rulesMap = new HashMap<>();
    Map<String, RuleReference> rulesReferences = new HashMap<>();
    String line;
    while (!Strings.isNullOrEmpty(line = inputReader.readLine())) {
      Matcher ruleMatcher = RULE_PATTERN.matcher(line);
      if (!ruleMatcher.matches()) {
        throw new IllegalArgumentException("Invalid rule:  " + line);
      }

      String ruleId = ruleMatcher.group(1);
      String firstArgument = ruleMatcher.group(2);
      if (firstArgument.startsWith("\"")) {
        rulesMap.put(ruleId, new ConstantRule(ruleId, ruleMatcher.group(3)));
        continue;
      }

      RuleReference ruleReference =
          rulesReferences.computeIfAbsent(
              firstArgument, ruleReferenceId -> new RuleReference(ruleReferenceId, rulesMap));
      List<Rule> rules = new ArrayList<>(List.of(ruleReference));
      String secondArgument = ruleMatcher.group(4);
      if (!Strings.isNullOrEmpty(secondArgument)) {
        ruleReference =
            rulesReferences.computeIfAbsent(
                secondArgument, ruleReferenceId -> new RuleReference(ruleReferenceId, rulesMap));
        rules.add(ruleReference);
      }
      CompositeRule compositeRule = new CompositeRule(ruleId, rules);

      String thirdArgument = ruleMatcher.group(5);
      if (Strings.isNullOrEmpty(thirdArgument)) {
        rulesMap.put(ruleId, compositeRule);
        continue;
      }

      ruleReference =
          rulesReferences.computeIfAbsent(
              thirdArgument, ruleReferenceId -> new RuleReference(ruleReferenceId, rulesMap));
      List<Rule> rightHandRules = new ArrayList<>(List.of(ruleReference));
      String fourthArgument = ruleMatcher.group(6);
      if (!Strings.isNullOrEmpty(fourthArgument)) {
        ruleReference = rulesReferences.computeIfAbsent(fourthArgument, ruleReferenceId -> new RuleReference(ruleReferenceId, rulesMap));
        rightHandRules.add(ruleReference);
      }
      CompositeRule rightHandRule = new CompositeRule(ruleId, rightHandRules);
      OptionRule optionRule = new OptionRule(ruleId, List.of(compositeRule, rightHandRule));
      rulesMap.put(ruleId, optionRule);
    }

    return rulesMap;
  }
}

abstract class Rule {

  private final String id;

  public Rule(String id) {
    this.id = id;
  }

  public abstract String getRulePattern();
}

class CompositeRule extends Rule {

  private final List<Rule> rules;
  private String value;

  public CompositeRule(String id, List<Rule> rules) {
    super(id);
    this.rules = rules;
  }

  @Override
  public String getRulePattern() {
    if (value == null) {
      StringBuilder rulePatternBuilder = new StringBuilder("(");
      for (Rule rule : rules) {
        rulePatternBuilder.append(rule.getRulePattern());
      }
      rulePatternBuilder.append(")");

      value = rulePatternBuilder.toString();
    }

    return value;
  }
}

class ConstantRule extends Rule {

  private final String value;

  public ConstantRule(String id, String value) {
    super(id);
    this.value = value;
  }

  @Override
  public String getRulePattern() {
    return value;
  }
}

class RuleReference extends Rule {

  private final Supplier<Rule> ruleReference;
  private String value;

  public RuleReference(String id, Map<String, Rule> rulesMap) {
    super(id);
    this.ruleReference = () -> rulesMap.get(id);
  }

  @Override
  public String getRulePattern() {
    if (value == null) {
      value = ruleReference.get().getRulePattern();
    }

    return value;
  }
}

class OptionRule extends Rule {

  private final List<Rule> rules;
  private String value;

  public OptionRule(String id, List<Rule> rules) {
    super(id);
    this.rules = rules;
  }

  @Override
  public String getRulePattern() {
    if (value == null) {
      StringBuilder rulePatternBuilder = new StringBuilder("(");
      for (Rule rule : rules) {
        if (rulePatternBuilder.length() > 1) {
          rulePatternBuilder.append("|");
        }
        rulePatternBuilder.append(rule.getRulePattern());
      }
      rulePatternBuilder.append(")");

      value = rulePatternBuilder.toString();
    }

    return value;
  }
}
