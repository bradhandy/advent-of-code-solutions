package com.dbjgb.advent.fifteen.puzzle.twentyTwo;

import com.dbjgb.advent.Utility;
import com.dbjgb.advent.fifteen.puzzle.twentyOne.Boss;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Solution {

  public static void main(String... args) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Boss boss =
        objectMapper.readValue(
            Utility.openInputFile("fifteen/puzzle/twentyTwo/boss.json"), Boss.class);
  }
}
