package com.dbjgb.advent._2022.puzzle._15;

public class Range {

  private final int left;
  private final int right;

  public Range(int left, int right) {
    this.left = left;
    this.right = right;
  }

  public int getStart() {
    return left;
  }

  public int getEnd() {
    return right;
  }

  public int getCount() {
    return (right - left) + 1;
  }

  public boolean overlaps(Range range) {
    if (range == null) {
      return false;
    }

    return (this.left <= range.left && range.left <= this.right)
        || (this.left <= range.right && range.right <= this.right);
  }

  public Range merge(Range range) {
    assert overlaps(range) : "Disjoint ranges cannot be merged.";
    return new Range(Math.min(this.left, range.left), Math.max(this.right, range.right));
  }
}
