package com.dbjgb.advent.twenty.puzzle.eighteen;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern PARENTHESIZED_OPERANDS_PATTERN =
      Pattern.compile("\\((\\d+(?: [+*] \\d+)+)\\)");
  private static final Pattern ADDITION_EXPRESSION_PATTERN = Pattern.compile("(\\d+ \\+ \\d+)");

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/eighteen/input.txt")) {
      long totalValue = 0;
      String line;
      while ((line = inputReader.readLine()) != null) {

        // part 1 the second parameter will be "false".
        long value = evaluateExpression(line, true);
        System.out.printf("%s becomes %d.\n", line, value);
        totalValue += value;
      }

      System.out.println(totalValue);
    }
  }

  private static long evaluateExpression(String expression, boolean newRules) {
    String expressionProgress = expression;
    Matcher parenthesizedOperandsMatcher =
        PARENTHESIZED_OPERANDS_PATTERN.matcher(expressionProgress);
    while (parenthesizedOperandsMatcher.find()) {
      long value = evaluateExpression(parenthesizedOperandsMatcher.group(1), newRules);
      StringBuilder updatedExpression =
          new StringBuilder(expressionProgress.substring(0, parenthesizedOperandsMatcher.start()))
              .append(value);
      if (parenthesizedOperandsMatcher.end() < expressionProgress.length()) {
        updatedExpression.append(expressionProgress.substring(parenthesizedOperandsMatcher.end()));
      }
      expressionProgress = updatedExpression.toString();
      parenthesizedOperandsMatcher.reset(expressionProgress);
    }

    if (newRules) {
      Matcher additionExpressionMatcher = ADDITION_EXPRESSION_PATTERN.matcher(expressionProgress);
      while (additionExpressionMatcher.find()) {
        long value = evaluateExpression(additionExpressionMatcher.group(1), false);
        StringBuilder updatedExpression =
            new StringBuilder(expressionProgress.substring(0, additionExpressionMatcher.start()))
                .append(value);
        if (additionExpressionMatcher.end() < expressionProgress.length()) {
          updatedExpression.append(expressionProgress.substring(additionExpressionMatcher.end()));
        }
        expressionProgress = updatedExpression.toString();
        additionExpressionMatcher.reset(expressionProgress);
      }
    }

    String[] operands = expressionProgress.split(" ");
    long value = Long.parseLong(operands[0]);
    if (operands.length > 1) {
      String operator = operands[1];
      for (int i = 2; i < operands.length; i++) {
        switch (operands[i]) {
          case "+":
          case "*":
            operator = operands[i];
            break;
          default:
            long rightOperand = Long.parseLong(operands[i]);
            value = (operator.equals("+")) ? value + rightOperand : value * rightOperand;
        }
      }
    }

    return value;
  }
}
