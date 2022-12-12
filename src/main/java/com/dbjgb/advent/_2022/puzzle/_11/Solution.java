package com.dbjgb.advent._2022.puzzle._11;

import com.google.common.collect.Iterables;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Solution {

  public static final long MODULO = 3 * 17 * 11 * 2 * 19 * 5 * 13 * 7;

  private static final List<Monkey> MONKEYS =
      List.of(
          new Monkey(
              List.of(new Item(66), new Item(79)),
              item -> item.getWorryLevel() * 11,
              item -> item.getWorryLevel() % 7 == 0,
              6,
              7),
          new Monkey(
              List.of(
                  new Item(84),
                  new Item(94),
                  new Item(94),
                  new Item(81),
                  new Item(98),
                  new Item(75)),
              item -> item.getWorryLevel() * 17,
              item -> item.getWorryLevel() % 13 == 0,
              5,
              2),
          new Monkey(
              List.of(
                  new Item(85),
                  new Item(79),
                  new Item(59),
                  new Item(64),
                  new Item(79),
                  new Item(95),
                  new Item(67)),
              item -> item.getWorryLevel() + 8,
              item -> item.getWorryLevel() % 5 == 0,
              4,
              5),
          new Monkey(
              List.of(new Item(70)),
              item -> item.getWorryLevel() + 3,
              item -> item.getWorryLevel() % 19 == 0,
              6,
              0),
          new Monkey(
              List.of(new Item(57), new Item(69), new Item(78), new Item(78)),
              item -> item.getWorryLevel() + 4,
              item -> item.getWorryLevel() % 2 == 0,
              0,
              3),
          new Monkey(
              List.of(new Item(65), new Item(92), new Item(60), new Item(74), new Item(72)),
              item -> item.getWorryLevel() + 7,
              item -> item.getWorryLevel() % 11 == 0,
              3,
              4),
          new Monkey(
              List.of(new Item(77), new Item(91), new Item(91)),
              item -> item.getWorryLevel() * item.getWorryLevel(),
              item -> item.getWorryLevel() % 17 == 0,
              1,
              7),
          new Monkey(
              List.of(
                  new Item(76),
                  new Item(58),
                  new Item(57),
                  new Item(55),
                  new Item(67),
                  new Item(77),
                  new Item(54),
                  new Item(99)),
              item -> item.getWorryLevel() + 6,
              item -> item.getWorryLevel() % 3 == 0,
              2,
              1));

  public static void main(String... args) throws Exception {
    for (int i = 0; i < 10000; i++) {
      for (Monkey monkey : MONKEYS) {
        monkey.passItemsToMonkeys(MONKEYS);
      }
    }

    Set<Monkey> monkeysByItemsTested =
        new TreeSet<>(Comparator.comparing(Monkey::getItemsInspected).reversed());
    monkeysByItemsTested.addAll(MONKEYS);

    Monkey firstMonkey = Iterables.get(monkeysByItemsTested, 0);
    Monkey secondMonkey = Iterables.get(monkeysByItemsTested, 1);
    System.out.printf(
        "Monkey Business: %d\n",
        firstMonkey.getItemsInspected() * secondMonkey.getItemsInspected());
  }
}
