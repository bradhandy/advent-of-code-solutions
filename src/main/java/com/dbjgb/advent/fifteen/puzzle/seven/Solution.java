package com.dbjgb.advent.fifteen.puzzle.seven;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern INSTRUCTION_PATTERN =
      Pattern.compile(
          "(?:(?:((?:[a-z]+)|(?:\\d+)) )?(AND|OR|NOT|RSHIFT|LSHIFT) )?((?:[a-z]+)|(?:\\d+)) -> ([a-z]+)");
  private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

  private static Map<String, Wire> wireMap = new HashMap<>();

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/seven/input.txt")) {
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher instructionMatcher = INSTRUCTION_PATTERN.matcher(line);
        if (instructionMatcher.matches()) {
          SignalSource rightSource = createSignalSource(instructionMatcher.group(3));
          Wire output = (Wire) createSignalSource(instructionMatcher.group(4));

          if (Objects.nonNull(instructionMatcher.group(2))) {
            SignalSource leftSource = createSignalSource(instructionMatcher.group(1));
            Operation operation = Operation.valueOf(instructionMatcher.group(2));
            output.setSource(new Gate(leftSource, operation, rightSource));
          } else {
            output.setSource(rightSource);
          }
        }
      }

      Wire aWire = wireMap.get("a");
      System.out.printf("'a' wire: %d\n", aWire.getSignal());
    }
  }

  private static SignalSource createSignalSource(String signalOrWire) {
    if (Objects.isNull(signalOrWire)) {
      return IgnoredSource.INSTANCE;
    }
    if (NUMBER_PATTERN.matcher(signalOrWire).matches()) {
      return new ConstantSource(Integer.parseInt(signalOrWire));
    }

    Wire wire = Optional.ofNullable(wireMap.get(signalOrWire)).orElse(new Wire(signalOrWire));
    wireMap.put(signalOrWire, wire);

    return wire;
  }

  private enum Operation {
    NOT((left, right) -> ~right),
    RSHIFT((left, right) -> left >> right),
    LSHIFT((left, right) -> left << right),
    OR((left, right) -> left | right),
    AND((left, right) -> left & right);

    private final BinaryOperator<Integer> function;

    Operation(BinaryOperator<Integer> function) {
      this.function = function;
    }

    public int apply(int left, int right) {
      return function.apply(left, right);
    }
  }

  private interface SignalSource {
    int getSignal();
  }

  private static class ConstantSource implements SignalSource {

    private final int value;

    public ConstantSource(int value) {
      this.value = value;
    }

    public int getSignal() {
      return value;
    }
  }

  private static class Wire implements SignalSource {

    private final String name;

    private SignalSource source;
    private ConstantSource cachedValue;

    public Wire(String name) {
      this.name = name;
    }

    public void setSource(SignalSource source) {
      this.source = source;
    }

    public int getSignal() {
      if (cachedValue == null) {
        cachedValue = new ConstantSource(source.getSignal());
      }
      return cachedValue.getSignal();
    }
  }

  private static class Gate implements SignalSource {

    private final SignalSource leftSource;
    private final Operation operation;
    private final SignalSource rightSource;
    private ConstantSource cachedValue;

    public Gate(SignalSource leftSource, Operation operation, SignalSource rightSource) {
      this.leftSource = leftSource;
      this.operation = operation;
      this.rightSource = rightSource;
    }

    public Gate(Operation operation, SignalSource rightSource) {
      this.leftSource = IgnoredSource.INSTANCE;
      this.operation = operation;
      this.rightSource = rightSource;
    }

    public int getSignal() {
      if (cachedValue == null) {
        cachedValue =
            new ConstantSource(operation.apply(leftSource.getSignal(), rightSource.getSignal()));
      }
      return cachedValue.getSignal();
    }
  }

  private static class IgnoredSource implements SignalSource {

    private static final SignalSource INSTANCE = new IgnoredSource();

    @Override
    public int getSignal() {
      return Integer.MIN_VALUE;
    }
  }
}
