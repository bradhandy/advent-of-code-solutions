package com.dbjgb.advent._2015.puzzle._24;

import com.dbjgb.advent.Utility;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

public class Solution {
  private static final List<Integer> PACKAGE_WEIGHTS = Lists.newArrayList();
  private static final Multimap<Integer, Integer> SUMS_MAP = ArrayListMultimap.create();

  public static void main(String... args) throws Exception {
    try (BufferedReader bufferedReader =
             Utility.openInputFile("_2015/puzzle/_24/input.txt")) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        PACKAGE_WEIGHTS.add(Integer.valueOf(line));
      }
    }

    int targetWeight = PACKAGE_WEIGHTS.stream().reduce(0, Integer::sum) / 3;

    int sign = 1;
    Collections.sort(PACKAGE_WEIGHTS);

    SUMS_MAP.put(0, -1);
    for (int index = 0; index < PACKAGE_WEIGHTS.size(); index++) {
      Set<Integer> sums = Sets.newHashSet(SUMS_MAP.keys());
      for (Integer sum : sums) {
        int newSum = sum + PACKAGE_WEIGHTS.get(index);
        if (0 < (newSum - targetWeight) * sign) {
          continue;
        } else {
          SUMS_MAP.put(newSum, index);
        }
      }
    }

    for (Integer key : SUMS_MAP.keySet()) {
      System.out.printf("%d : %s\n", key, SUMS_MAP.get(key));
    }

    Set<Set<Integer>> combinations = findCombinations(targetWeight);
    NavigableMap<Integer, Integer> numberOfOperandsMap = Maps.newTreeMap();
    for (Set<Integer> combination : combinations) {
      int numberOfCombinations = numberOfOperandsMap.getOrDefault(combination.size(), 0) + 1;
      numberOfOperandsMap.put(combination.size(), numberOfCombinations);
    }

    System.out.printf("%d matches.\n", combinations.size());
    Map.Entry<Integer, Integer> minimumNumberOfContainersEntry = numberOfOperandsMap.firstEntry();
    System.out.printf(
        "%d combinations with %d operands.",
        minimumNumberOfContainersEntry.getValue(), minimumNumberOfContainersEntry.getKey());
  }

  public static Set<Set<Integer>> findCombinations(int target) {
    Set<Set<Integer>> accumulator = Sets.newHashSet();
    findCombinations(target, accumulator, null);

    return accumulator;
  }

  public static void findCombinations(int target, Set<Set<Integer>> accumulator, Set<Integer> path) {
    Set<Integer> currentPath = MoreObjects.firstNonNull(path, Sets.newLinkedHashSet());
    for (int index : SUMS_MAP.get(target)) {
      if (currentPath.contains(index)) {
        continue;
      }

      currentPath.add(index);
      int newTarget = target - PACKAGE_WEIGHTS.get(index);
      if (newTarget > 0) {
        findCombinations(newTarget, accumulator, currentPath);
      } else if (newTarget == 0) {
        accumulator.add(Sets.newLinkedHashSet(currentPath));
      }
      currentPath.remove(index);
    }
  }
}
