package com.dbjgb.advent._2020.puzzle._23;

import java.util.Objects;

public class Link {

  private final long value;
  private Link nextLowerValue;
  private Link nextLink;
  private Link previousLink;

  public Link(long value) {
    this.value = value;
  }

  public void setNextLowerValue(Link nextLowerValue) {
    this.nextLowerValue = nextLowerValue;
  }

  public void setNext(Link nextLink) {
    this.nextLink = nextLink;
  }

  public void setPrevious(Link previousLink) {
    this.previousLink = previousLink;
  }

  public Link getNext() {
    return nextLink;
  }

  public Link getPrevious() {
    return previousLink;
  }

  public Link getLowerValue() {
    return nextLowerValue;
  }

  public long getValue() {
    return value;
  }

  public boolean isLinkedTo(Link link) {
    if (this == link) {
      return true;
    }

    Link chain = this;
    while ((chain = chain.getNext()) != null) {
      if (chain == link) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Link link = (Link) o;
    return value == link.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
