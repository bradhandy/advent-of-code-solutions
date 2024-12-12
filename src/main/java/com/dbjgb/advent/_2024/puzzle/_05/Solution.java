package com.dbjgb.advent._2024.puzzle._05;

import com.dbjgb.advent.Utility;
import com.google.common.base.Joiner;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {

  private static final Pattern RULE_PATTERN = Pattern.compile("(\\d+)\\|(\\d+)");
  private static final Pattern PRINT_ORDER_PATTERN = Pattern.compile("\\d+(,\\d+)+");

  public static void main(String... args) throws Exception {
    solvePartOne();
    solvePartTwo();
  }

  private static void solvePartOne() throws Exception {
    Map<String, Page> pageMap = new HashMap<>();
    BigInteger result = BigInteger.ZERO;
    for (String line : Utility.readAllLines("_2024/puzzle/_05/input.txt")) {
      Matcher matcher = RULE_PATTERN.matcher(line);
      if (matcher.matches()) {
        String firstPageNumber = matcher.group(1);
        Page firstPage = pageMap.getOrDefault(firstPageNumber, new Page(firstPageNumber));
        String secondPageNumber = matcher.group(2);
        Page secondPage = pageMap.getOrDefault(secondPageNumber, new Page(secondPageNumber));
        firstPage.before(secondPage);
        secondPage.after(firstPage);

        pageMap.putIfAbsent(firstPageNumber, firstPage);
        pageMap.putIfAbsent(secondPageNumber, secondPage);
      } else {
        matcher = PRINT_ORDER_PATTERN.matcher(line);
        if (matcher.matches()) {
          String[] pageNumbers = line.split(",");
          String middlePageNumber = pageNumbers[(int) Math.floor(pageNumbers.length / 2)];
          boolean validOrder = true;
          for (int i = 0; i < pageNumbers.length && validOrder; i++) {
            String[] beforeNumbers = Arrays.copyOfRange(pageNumbers, 0, i);
            String[] afterNumbers = i + 1 >= pageNumbers.length ? new String[0] : Arrays.copyOfRange(pageNumbers, i + 1, pageNumbers.length);

            Page currentPage = pageMap.get(pageNumbers[i]);
            if (beforeNumbers.length > 0) {
              validOrder &= Arrays.stream(beforeNumbers).noneMatch(b -> {
                Page beforePage = pageMap.get(b);
                return !beforePage.appearsBefore(currentPage);
              });
            }
            if (afterNumbers.length > 0) {
              validOrder &= Arrays.stream(afterNumbers).noneMatch(a -> {
                Page afterPage = pageMap.get(a);
                return !afterPage.comesAfter(currentPage);
              });
            }
          }

          if (validOrder) {
            result = result.add(new BigInteger(middlePageNumber));
          }
        }
      }
    }

    System.out.println("Page One: " + result);
  }

  private static void solvePartTwo() throws Exception {
    Map<String, Page> pageMap = new HashMap<>();
    BigInteger result = BigInteger.ZERO;
    for (String line : Utility.readAllLines("_2024/puzzle/_05/input.txt")) {
      Matcher matcher = RULE_PATTERN.matcher(line);
      if (matcher.matches()) {
        String firstPageNumber = matcher.group(1);
        Page firstPage = pageMap.getOrDefault(firstPageNumber, new Page(firstPageNumber));
        String secondPageNumber = matcher.group(2);
        Page secondPage = pageMap.getOrDefault(secondPageNumber, new Page(secondPageNumber));
        firstPage.before(secondPage);
        secondPage.after(firstPage);

        pageMap.putIfAbsent(firstPageNumber, firstPage);
        pageMap.putIfAbsent(secondPageNumber, secondPage);
      } else {
        matcher = PRINT_ORDER_PATTERN.matcher(line);
        if (matcher.matches()) {
          String[] pageNumbers = line.split(",");
          String middlePageNumber = pageNumbers[(int) Math.floor(pageNumbers.length / 2)];
          boolean validOrder = true;
          for (int i = 0; i < pageNumbers.length && validOrder; i++) {
            String[] beforeNumbers = Arrays.copyOfRange(pageNumbers, 0, i);
            String[] afterNumbers = i + 1 >= pageNumbers.length ? new String[0] : Arrays.copyOfRange(pageNumbers, i + 1, pageNumbers.length);

            Page currentPage = pageMap.get(pageNumbers[i]);
            if (beforeNumbers.length > 0) {
              validOrder &= Arrays.stream(beforeNumbers).noneMatch(b -> {
                Page beforePage = pageMap.get(b);
                return !beforePage.appearsBefore(currentPage);
              });
            }
            if (afterNumbers.length > 0) {
              validOrder &= Arrays.stream(afterNumbers).noneMatch(a -> {
                Page afterPage = pageMap.get(a);
                return !afterPage.comesAfter(currentPage);
              });
            }
          }

          if (!validOrder) {
            List<Page> orderedPages = Arrays.stream(pageNumbers).map(pageMap::get).sorted().toList();
            System.out.printf("%s -> %s\n", line, Joiner.on(',').join(orderedPages.stream().map(Page::getName).toList()));
            result = result.add(new BigInteger(orderedPages.get((int) Math.floor(orderedPages.size() / 2.0)).getName()));
          }
        }
      }
    }

    System.out.println("Page Two: " + result);
  }

  private static class Page implements Comparable<Page> {

    private final String name;
    private Set<Page> before = new LinkedHashSet<>();
    private Set<Page> after = new LinkedHashSet<>();

    public Page(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void before(Page page) {
      before.add(page);
    }

    public void after(Page page) {
      after.add(page);
    }

    public boolean comesAfter(Page page) {
      return after.contains(page);
    }

    public boolean appearsBefore(Page page) {
      return before.contains(page);
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Page page)) {
        return false;
      }
      return Objects.equals(name, page.name);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(name);
    }

    @Override
    public int compareTo(Page o) {
      if (equals(o)) {
        return 0;
      }

      return before.contains(o) ? -1 : 1;
    }
  }
}
