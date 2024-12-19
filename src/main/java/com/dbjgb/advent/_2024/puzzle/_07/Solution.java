package com.dbjgb.advent._2024.puzzle._07;

import com.dbjgb.advent.Utility;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern EXPRESSION_CONFIGURATION = Pattern.compile("(\\d+): (\\d+(?: \\d+)+)");

  public static void main(String... args) throws Exception {
    solvePartOne();
    solvePartTwo();
  }

  private static void solvePartOne() throws Exception {
    System.out.println("Part One: " + solveProblem(EnumSet.complementOf(EnumSet.of(Operator.CONCATENATION))));
  }

  private static BigInteger solveProblem(Set<Operator> operators) throws Exception {
    BigInteger total = BigInteger.ZERO;
    for (String expression : Utility.readAllLines("_2024/puzzle/_07/input.txt")) {
      Matcher matcher = EXPRESSION_CONFIGURATION.matcher(expression);
      if (matcher.matches()) {
        BigInteger expectedResult = new BigInteger(matcher.group(1));
        String[] operands = matcher.group(2).split(" ");

        Set<BigInteger> results = applyOperators(operands, operators);
        if (results.contains(expectedResult)) {
          total = total.add(expectedResult);
        }
      }
    }

    return total;
  }

  private static Set<BigInteger> applyOperators(String[] operands, Set<Operator> operators) {
    Set<BigInteger> results = new HashSet<>();
    for (Operator operator : operators) {
      results.addAll(applyOperators(operator.apply(operator.getStart(), new BigInteger(operands[0])), Arrays.copyOfRange(operands, 1, operands.length), operators));
    }
    return results;
  }

  private static Set<BigInteger> applyOperators(BigInteger start, String[] operands, Set<Operator> operators) {
    Set<BigInteger> results = new HashSet<>();
    for (Operator operator : operators) {
      if (operands.length == 1) {
        results.add(operator.apply(start, new BigInteger(operands[0])));
      } else {
        results.addAll(applyOperators(operator.apply(start, new BigInteger(operands[0])), Arrays.copyOfRange(operands, 1, operands.length), operators));
      }
    }

    return results;
  }

  private static void solvePartTwo() throws Exception {
    System.out.println("Part Two: " + solveProblem(EnumSet.allOf(Operator.class)));
  }

  private enum Operator {
    ADDITION(BigInteger::add, BigInteger.ZERO),
    MULTIPLICATION(BigInteger::multiply, BigInteger.ONE),
    CONCATENATION((left, right) -> new BigInteger(left.toString() + right.toString()), BigInteger.ZERO);

    private final BiFunction<BigInteger, BigInteger, BigInteger> operation;
    private final BigInteger start;

    Operator(BiFunction<BigInteger, BigInteger, BigInteger> operation, BigInteger start) {
      this.operation = operation;
      this.start = start;
    }

    public BigInteger apply(BigInteger left, BigInteger right) {
      return operation.apply(left, right);
    }

    public BigInteger getStart() {
      return start;
    }
  }
}
