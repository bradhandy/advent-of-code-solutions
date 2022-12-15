package com.dbjgb.advent._2015.puzzle._20;

import com.google.common.collect.Sets;

import java.util.Optional;
import java.util.SortedSet;
import java.util.stream.IntStream;

public class Solution {

  public static void main(String... args) throws Exception {
    findFirstHouseWithThirtySixMillionGifts();
    findFirstHouseWithThirtySixMillionGiftsAfterLaborContract();
  }

  private static void findFirstHouseWithThirtySixMillionGifts() {
    Optional<Product> product =
        IntStream.rangeClosed(1, 36_000_000)
            .mapToObj(Product::new)
            .filter(
                (rawProduct) ->
                    rawProduct.getFactors().stream()
                            .mapToLong(factor -> factor * 10)
                            .reduce(0L, Long::sum)
                        >= 36_000_000L)
            .findFirst();

    System.out.printf(
        "(part 1) The first house with at least 36 million gifts is: %d (%s)\n",
        product.get().getValue(), product.get().getFactors());
  }

  private static void findFirstHouseWithThirtySixMillionGiftsAfterLaborContract() {
    Optional<Product> product =
        IntStream.rangeClosed(1, 36_000_000)
            .mapToObj(Product::new)
            .filter(
                (rawProduct) ->
                    rawProduct.getFactors().stream()
                        // elves only deliver to a maximum of 50 houses and 11 times the number of
                        // presents instead of 10 times the number of presents.
                            .filter(factor -> rawProduct.getValue() / factor <= 50)
                            .mapToLong(factor -> factor * 11)
                            .reduce(0L, Long::sum)
                        >= 36_000_000L)
            .findFirst();

    System.out.printf(
        "(part 2) The first house with at least 36 million gifts is: %d (%s)\n",
        product.get().getValue(), product.get().getFactors());
  }

  public static class Product {
    private final long product;

    public Product(long product) {
      this.product = product;
    }

    public long getValue() {
      return product;
    }

    // based on an answer from
    // https://stackoverflow.com/questions/8647059/finding-factors-of-a-given-integer
    public SortedSet<Long> getFactors() {
      SortedSet<Long> factors = Sets.newTreeSet();

      int incrementer = product % 2 == 0 ? 1 : 2;
      for (long i = 1; i <= Math.sqrt(product); i += incrementer) {
        if (product % i == 0) {
          factors.add(i);
          factors.add(product / i);
        }
      }

      return factors;
    }
  }
}
