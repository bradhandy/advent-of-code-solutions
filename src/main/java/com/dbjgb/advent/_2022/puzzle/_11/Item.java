package com.dbjgb.advent._2022.puzzle._11;

import java.util.function.Function;

public class Item {

  private long worryLevel;

  public Item(long worryLevel) {
    this.worryLevel = worryLevel;
  }

  public void inspect(Function<Item, Long> worryLevelModifier) {
    worryLevel = worryLevelModifier.apply(this);
    //worryLevel = worryLevel / 3;
    worryLevel %= Solution.MODULO;
  }

  public long getWorryLevel() {
    return worryLevel;
  }
}
