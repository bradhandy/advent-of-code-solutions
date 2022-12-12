package com.dbjgb.advent._2022.puzzle._11;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Monkey {

  private final List<Item> items;
  private final Function<Item, Long> worryLevelModifier;
  private final Predicate<Item> itemTester;
  private final int passingTestMonkey;
  private final int failingTestMonkey;

  private long itemsInspected = 0;

  public Monkey(
      List<Item> items,
      Function<Item, Long> worryLevelModifier,
      Predicate<Item> itemTester,
      int passingTestMonkey,
      int failingTestMonkey) {
    this.items = new ArrayList<>(items);
    this.worryLevelModifier = worryLevelModifier;
    this.itemTester = itemTester;
    this.passingTestMonkey = passingTestMonkey;
    this.failingTestMonkey = failingTestMonkey;
  }

  public void passItemsToMonkeys(List<Monkey> monkeys) {
    for (Item item : items) {
      item.inspect(worryLevelModifier);
      boolean passesInspection = itemTester.test(item);
      if (passesInspection) {
        passItemTo(item, monkeys.get(passingTestMonkey));
      } else {
        passItemTo(item, monkeys.get(failingTestMonkey));
      }
      itemsInspected += 1;
    }
    items.clear();
  }

  public long getItemsInspected() {
    return itemsInspected;
  }

  public void receiveItem(Item item) {
    items.add(item);
  }

  public void passItemTo(Item item, Monkey monkey) {
    monkey.receiveItem(item);
  }
}
