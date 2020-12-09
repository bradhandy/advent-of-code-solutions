package com.dbjgb.advent.twenty.twenty.puzzle.eight;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {

  private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("(nop|acc|jmp) ([\\-+]\\d+)");

  public static void main(String... args) throws Exception {
    printAccumulatorAtFirstCycle();
    printAccumulatorAfterFixingProgram();
  }

  private static List<Instruction> parseInstructions() throws IOException {
    List<Instruction> instructions = new ArrayList<>();

    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/eight/input.txt")) {
      String line;
      while ((line = inputReader.readLine()) != null) {
        Matcher instructionMatcher = INSTRUCTION_PATTERN.matcher(line);
        if (instructionMatcher.matches()) {
          instructions.add(
              new Instruction(
                  instructionMatcher.group(1), Integer.parseInt(instructionMatcher.group(2))));
        }
      }
    }

    return instructions;
  }

  private static ExecutionResults executeInstructions(List<Instruction> instructions) {
    int accumulator = 0;
    List<Integer> instructionList = new ArrayList<>();

    for (int i = 0; i < instructions.size(); i++) {
      Instruction instruction = instructions.get(i);
      if (instruction.executed) {
        return new ExecutionResults(State.CYCLE, i, accumulator, instructionList);
      }

      instruction.executed();
      instructionList.add(i);
      if (instruction.getType().equals("nop")) {
        continue;
      }

      if (instruction.getType().equals("acc")) {
        accumulator += instruction.getArgument();
      } else if (instruction.getType().equals("jmp")) {
        i += (instruction.getArgument() - 1);
      }
    }

    return new ExecutionResults(State.NO_CYCLE, -1, accumulator, instructionList);
  }

  private static void printAccumulatorAtFirstCycle() throws IOException {
    List<Instruction> instructions = parseInstructions();
    ExecutionResults executionResults = executeInstructions(instructions);

    System.out.printf("State: %s; ", executionResults.getState());
    System.out.printf(
        "Cycle Start: %d; %s; ",
        executionResults.getCycleStartIndex(),
        instructions.get(executionResults.getCycleStartIndex()).toString());
    System.out.printf(
        "Cycle End: %d; %s; ",
        executionResults.getCycleEndIndex(),
        instructions.get(executionResults.getCycleEndIndex()).toString());
    System.out.printf("Accumulator value: %d\n", executionResults.getAccumulator());
  }

  private static void printAccumulatorAfterFixingProgram() throws IOException {
    ExecutionResults executionResults = executeInstructions(parseInstructions());
    List<Integer> executedInstructionList = List.copyOf(executionResults.getInstructionList());
    int indexOfCycleStart = executedInstructionList.indexOf(executionResults.getCycleStartIndex());

    List<Instruction> originalInstructions = parseInstructions();
    List<Integer> significantCycleInstructions =
        executedInstructionList.subList(indexOfCycleStart, executedInstructionList.size()).stream()
            .filter(index -> !originalInstructions.get(index).getType().equals("acc"))
            .collect(Collectors.toList());

    for (Integer instructionIndex : significantCycleInstructions) {
      List<Instruction> instructions = parseInstructions();
      Instruction instruction = instructions.get(instructionIndex);
      if (instruction.getType().equals("nop")) {
        instructions.set(instructionIndex, new Instruction("jmp", instruction.getArgument()));
      } else {
        instructions.set(instructionIndex, new Instruction("nop", instruction.getArgument()));
      }

      ExecutionResults newExecutionResults = executeInstructions(instructions);
      if (newExecutionResults.getState() == State.NO_CYCLE) {
        System.out.printf("Broken Cycle Accumulator:  %d\n", newExecutionResults.getAccumulator());
        break;
      }
    }

    System.out.println("done");
  }

  private enum State {
    CYCLE,
    NO_CYCLE
  }

  private static class ExecutionResults {

    private final State state;
    private final int cycleStartIndex;
    private final int accumulator;
    private final List<Integer> instructionList;

    public ExecutionResults(
        State state, int cycleStartIndex, int accumulator, List<Integer> instructionList) {
      this.state = state;
      this.cycleStartIndex = cycleStartIndex;
      this.accumulator = accumulator;
      this.instructionList = List.copyOf(instructionList);
    }

    public State getState() {
      return state;
    }

    public int getCycleStartIndex() {
      return cycleStartIndex;
    }

    public int getCycleEndIndex() {
      return instructionList.get(instructionList.size() - 1);
    }

    public int getAccumulator() {
      return accumulator;
    }

    public List<Integer> getInstructionList() {
      return instructionList;
    }
  }

  private static class Instruction {

    private final String type;
    private final int argument;

    private boolean executed;

    public Instruction(String type, int argument) {
      this.type = type;
      this.argument = argument;
    }

    public String getType() {
      return type;
    }

    public int getArgument() {
      return argument;
    }

    public void executed() {
      executed = true;
    }

    public String toString() {
      StringBuilder instruction = new StringBuilder(type).append(" ");
      if (argument >= 0) {
        instruction.append('+');
      }
      instruction.append(argument);

      return instruction.toString();
    }
  }
}
