package com.dbjgb.advent._2015.puzzle._25;

import java.math.BigInteger;

public class Solution {

  private static final int COLUMN = 3029;
  private static final int ROW = 2947;
  private static final BigInteger FIRST_CODE = new BigInteger("20151125");
  private static final BigInteger MULTIPLIER = new BigInteger("252533");
  private static final BigInteger DIVISOR = new BigInteger("33554393");

  public static void main(String... args) throws Exception {
    int codeNumber = 0;
    int columnsNecessary = (COLUMN + ROW) - 1;
    for (int i = columnsNecessary; i > 0; i--) {
      codeNumber += i;
    }
    codeNumber -= (columnsNecessary - COLUMN);

    BigInteger code = FIRST_CODE;
    for (int numCodes = 1; numCodes < codeNumber; numCodes++) {
      if (numCodes < 6) {
        System.out.println(code);
      }
      code = code.multiply(MULTIPLIER).remainder(DIVISOR);
    }

    System.out.println(code);
  }
}
