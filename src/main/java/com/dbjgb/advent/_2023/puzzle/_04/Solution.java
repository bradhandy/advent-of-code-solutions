package com.dbjgb.advent._2023.puzzle._04;

import com.dbjgb.advent.Utility;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {

  private static final Pattern CARD_PATTERN = Pattern.compile("Card\\s+(\\d+): ([^|]+?) \\| ([^|]+)");
  private static final Splitter NUMBER_SPLITTER = Splitter.on(" ").omitEmptyStrings();

  public static class Part01 {
    public static void main(String... args) throws Exception {
      long totalPoints = 0L;
      for (String line : Utility.readAllLines("_2023/puzzle/_04/input.txt")) {
        Matcher lineMatcher = CARD_PATTERN.matcher(line);
        lineMatcher.find();

        Set<Integer> winningNumbers = Lists.newArrayList(NUMBER_SPLITTER.split(lineMatcher.group(2)))
            .stream().map(Integer::parseInt).collect(Collectors.toSet());
        Set<Integer> ticketNumbers = Lists.newArrayList(NUMBER_SPLITTER.split(lineMatcher.group(3)))
            .stream().map(Integer::parseInt).collect(Collectors.toSet());
        ticketNumbers.retainAll(winningNumbers);

        totalPoints += ticketNumbers.stream().reduce(0, (left, right) -> left == 0 ? 1 : left << 1);
      }

      System.out.printf("Total: %d\n", totalPoints);
    }
  }

  public static class Part02 {
    public static void main(String... args) throws Exception {
      List<Card> cards = new ArrayList<>();
      for (String line : Utility.readAllLines("_2023/puzzle/_04/input.txt")) {
        Matcher lineMatcher = CARD_PATTERN.matcher(line);
        lineMatcher.find();

        Set<Integer> winningNumbers = Lists.newArrayList(NUMBER_SPLITTER.split(lineMatcher.group(2)))
            .stream().map(Integer::parseInt).collect(Collectors.toSet());
        Set<Integer> ticketNumbers = Lists.newArrayList(NUMBER_SPLITTER.split(lineMatcher.group(3)))
            .stream().map(Integer::parseInt).collect(Collectors.toSet());
        ticketNumbers.retainAll(winningNumbers);

        cards.add(new Card(Integer.parseInt(lineMatcher.group(1)), winningNumbers, ticketNumbers));
      }

      for (Card card : cards) {
        card.applyWinnings(cards);
      }

      System.out.printf("Total: %d\n", cards.stream().map(Card::getCopies).reduce(0L, Long::sum));
    }
  }

  private static class Card {

    private final int id;
    private final Set<Integer> winningNumbers;
    private final Set<Integer> ticketNumbers;
    private long copies;

    public Card(int id, Set<Integer> winningNumbers, Set<Integer> ticketNumbers) {
      this.id = id;
      this.winningNumbers = winningNumbers;
      this.ticketNumbers = ticketNumbers;
      this.copies = 1;
    }

    public void addCopies(long newCopies) {
      this.copies += newCopies;
    }

    public void applyWinnings(List<Card> cards) {
      Set<Integer> ticketNumbers = new HashSet<>(this.ticketNumbers);
      ticketNumbers.retainAll(winningNumbers);

      for (int delta = 1; delta <= ticketNumbers.size(); delta++) {
        if (((id - 1) + delta) < cards.size()) {
          cards.get((id - 1) + delta).addCopies(copies);
        }
      }
    }

    public long getCopies() {
      return copies;
    }
  }
}
