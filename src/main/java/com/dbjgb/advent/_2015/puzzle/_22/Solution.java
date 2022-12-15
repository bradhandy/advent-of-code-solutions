package com.dbjgb.advent._2015.puzzle._22;

import com.dbjgb.advent.Utility;
import com.dbjgb.advent._2015.puzzle._21.Boss;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Solution {

  public static void main(String... args) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Boss boss =
        objectMapper.readValue(Utility.openInputFile("_2015/puzzle/_22/boss.json"), Boss.class);
  }
}
