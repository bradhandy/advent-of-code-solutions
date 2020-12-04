package com.dbjgb.advent.twenty.nineteen.puzzle.two;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class Solution {

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("nineteen/puzzle/two/input.txt")) {
      List<Integer> codes = new ArrayList<>();
      List<Operation> operations = new ArrayList<>();

      String[] stringCodes = inputReader.readLine().split(",");
      for (int i = 0; i < stringCodes.length; i += 4) {
        int code = Integer.parseInt(stringCodes[i]);
        codes.add(code);

        if (code == 99) {
          operations.add(new Operation(99));
          while (++i < stringCodes.length) {
            codes.add(Integer.parseInt(stringCodes[i]));
          }
        } else {
          int leftOperandPosition = Integer.parseInt(stringCodes[i + 1]);
          int rightOperandPosition = Integer.parseInt(stringCodes[i + 2]);
          int outputPosition = Integer.parseInt(stringCodes[i + 3]);

          codes.addAll(List.of(leftOperandPosition, rightOperandPosition, outputPosition));
          operations.add(
              new Operation(code, leftOperandPosition, rightOperandPosition, outputPosition));
        }
      }

      for (Operation operation : operations) {
        if (operation.shouldContinue()) {
          operation.process(codes);
        }
      }

      System.out.printf("Value of position 0:  %d", codes.get(0));
    }
  }

  public static class Operation {
    private final int code;
    private final int leftOperandPosition;
    private final int rightOperationPosition;
    private final int outputPosition;

    public Operation(
        int code, int leftOperandPosition, int rightOperationPosition, int outputPosition) {
      this.code = code;
      this.leftOperandPosition = leftOperandPosition;
      this.rightOperationPosition = rightOperationPosition;
      this.outputPosition = outputPosition;
    }

    public Operation(int code) {
      this(code, -1, -1, -1);
    }

    public void process(List<Integer> codes) {
      int leftOperand = codes.get(leftOperandPosition);
      int rightOperand = codes.get(rightOperationPosition);

      if (code == 1) {
        codes.set(outputPosition, leftOperand + rightOperand);
      } else if (code == 2) {
        codes.set(outputPosition, leftOperand * rightOperand);
      }
    }

    public boolean shouldContinue() {
      return code != 99;
    }
  }
}
