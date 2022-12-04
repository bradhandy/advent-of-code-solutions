package com.dbjgb.advent.twentyTwo.puzzle.four;

import com.dbjgb.advent.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern RANGE_PAIR_PATTERN = Pattern.compile("(\\d+)-(\\d+),(\\d+)-(\\d+)");

  public static void main(String... args) throws Exception {
    List<String> ranges = Utility.readAllLines("twentyTwo/puzzle/four/input.txt");
    int totalEnclosed = 0;
    for (String rangePair : ranges) {
      Matcher rangePairMatcher = RANGE_PAIR_PATTERN.matcher(rangePair);
      rangePairMatcher.matches();
      Range left = new Range(Integer.parseInt(rangePairMatcher.group(1)), Integer.parseInt(rangePairMatcher.group(2)));
      Range right = new Range(Integer.parseInt(rangePairMatcher.group(3)), Integer.parseInt(rangePairMatcher.group(4)));
      if (left.encloses(right) || right.encloses(left) || left.overlaps(right) || right.overlaps(left)) {
        totalEnclosed += 1;
      }
    }

    System.out.println(totalEnclosed);
  }

  private static class Range {

    private final int start;
    private final int end;

    public Range(int start, int end) {
      this.start = start;
      this.end = end;
    }

    public boolean encloses(Range range) {
      return this.start <= range.start && this.end >= range.end;
    }

    public boolean overlaps(Range range) {
      return (this.start >= range.start && this.start <= range.end)
          || (this.end >= range.start && this.end <= range.end);
    }
  }
}
