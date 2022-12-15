package com.dbjgb.advent._2015.puzzle._23;

import com.dbjgb.advent.Utility;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern INSTRUCTION_PATTERN =
      Pattern.compile("(ji[oe]|inc|hlf|tpl|jmp) ([ab]|[-+]\\d+)(?:, ([-+]\\d+))?");
  private static final Map<String, Integer> REGISTERS = new HashMap<>(Map.of("a", 0, "b", 0));

  public static void main(String... args) throws Exception {
    Map<String, Integer> registers = processInstructions();
    System.out.printf("Register 'b' is %d.\n", registers.get("b"));

    REGISTERS.put("a", 1);
    registers = processInstructions();
    System.out.printf("Register 'b' is %d.\n", registers.get("b"));
  }

  private static Map<String, Integer> processInstructions() throws IOException {
    List<String> instructions = Lists.newArrayList();
    Map<String, Integer> registers = new HashMap<>(REGISTERS);

    try (BufferedReader inputReader =
        Utility.openInputFile("_2015/puzzle/_23/input.txt")) {
      String line;
      while ((line = inputReader.readLine()) != null) {
        instructions.add(line);
      }

      int incrementer;
      for (int i = 0; i < instructions.size(); i += incrementer) {
        incrementer = 1;
        String instruction = instructions.get(i);
        MatchResult result =
            INSTRUCTION_PATTERN.matcher(instruction).results().findFirst().orElse(null);
        if (result == null) {
          throw new IllegalStateException("All instructions should be processable.");
        }

        String operation = result.group(1);
        if (operation.equals("jmp")) {
          incrementer = Integer.parseInt(result.group(2));
        } else {
          String register = result.group(2);
          int value = registers.get(register);
          if (operation.equals("hlf")) {
            value /= 2;
          } else if (operation.equals("inc")) {
            value++;
          } else if (operation.equals("tpl")) {
            value *= 3;
          } else if ((operation.equals("jie") && value % 2 == 0)
              || (operation.equals("jio") && value == 1)) {
            incrementer = Integer.parseInt(result.group(3));
          }

          registers.put(register, value);
        }
      }
    }

    return registers;
  }
}
