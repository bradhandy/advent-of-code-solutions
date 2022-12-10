package com.dbjgb.advent._2020.puzzle._23;

import java.util.HashMap;
import java.util.Map;

public class Solution {

  public static void main(String... args) {
    Link start = createLinks("496138527");
    Link one = start;
    while (one.getValue() != 1) {
      one = one.getLowerValue();
    }

    playCups(start, one, 100);
    printState(one);

    start = createLinks("496138527");
    one = start;
    while (one.getValue() != 1) {
      one = one.getLowerValue();
    }
    expandTo(1_000_000, start, one);
    playCups(start, one, 10_000_000);

    Link first = one.getNext();
    Link second = first.getNext();

    System.out.printf(
        "%d * %d = %d\n",
        first.getValue(), second.getValue(), first.getValue() * second.getValue());
  }

  private static void printState(Link one) {
    Link current = one.getNext();
    while (current.getValue() != 1) {
      System.out.print(current.getValue());
      current = current.getNext();
    }
    System.out.print('\n');
  }

  private static void playCups(Link startingCup, Link one, int turns) {
    Link currentCup = startingCup;
    for (int i = 0; i < turns; i++) {
      Link nextLink = currentCup.getNext();
      Link endLink = nextLink.getNext().getNext();
      Link newNextLink = endLink.getNext();
      currentCup.setNext(newNextLink);
      newNextLink.setPrevious(currentCup);

      nextLink.setPrevious(null);
      endLink.setNext(null);

      Link insertionPoint = currentCup.getLowerValue();
      while (nextLink.isLinkedTo(insertionPoint)) {
        insertionPoint = insertionPoint.getLowerValue();
      }

      Link oppositeInsertionPoint = insertionPoint.getNext();
      insertionPoint.setNext(nextLink);
      nextLink.setPrevious(insertionPoint);

      endLink.setNext(oppositeInsertionPoint);
      oppositeInsertionPoint.setPrevious(endLink);

      currentCup = currentCup.getNext();
    }
  }

  private static Link createLinks(String sequence) {
    Map<Link, Link> valueToLinkMap = new HashMap<>();
    Link start = new Link(sequence.charAt(0) - '0');
    valueToLinkMap.put(start, start);
    Link end = start;
    for (int i = 1; i < sequence.length(); i++) {
      Link next = new Link(sequence.charAt(i) - '0');
      valueToLinkMap.put(next, next);
      if (next.getValue() % sequence.length() == 0) {
        valueToLinkMap.put(new Link(0), next);
      }
      next.setPrevious(end);
      end.setNext(next);
      end = next;
    }
    end.setNext(start);
    start.setPrevious(end);
    for (Link current = start; current.getLowerValue() == null; current = current.getLowerValue()) {
      current.setNextLowerValue(valueToLinkMap.get(new Link(current.getValue() - 1)));
    }

    return start;
  }

  private static void expandTo(int newEndValue, Link start, Link one) {
    Link max = one.getLowerValue(); // wraps around to the max value per the rules.
    Link end = start.getPrevious();

    for (long value = max.getValue() + 1; value <= newEndValue; value++) {
      Link link = new Link(value);
      end.setNext(link);
      link.setPrevious(end);
      link.setNextLowerValue(max);

      end = link;
      max = link;
    }

    end.setNext(start);
    start.setPrevious(end);
    one.setNextLowerValue(end);
  }
}
