package com.dbjgb.advent._2024.puzzle._09;

import com.dbjgb.advent.Utility;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Solution {

  public static void main(String... args) throws Exception {
    solvePartOne();
    solvePartTwo();
  }

  private static void solvePartOne() throws Exception {
    String compressedAllocationTable = Utility.readEntireFile("_2024/puzzle/_09/input.txt");
    BigInteger checkSum = BigInteger.ZERO;
    long blockIndex = 0L;
    int endIndex = compressedAllocationTable.length() - 1;
    int remainingBlocks = 0;
    for (int index = 0; index < compressedAllocationTable.length() && endIndex >= index; index++) {
      int occurrences = compressedAllocationTable.charAt(index) - '0';
      while (occurrences-- > 0 && (endIndex > index || remainingBlocks-- > 0)) {
        if (index % 2 == 0) {
          long fileId = (long) index / 2L;
          checkSum = checkSum.add(BigInteger.valueOf(fileId * blockIndex++));
        } else if (endIndex > index) {
          if (remainingBlocks <= 0) {
            remainingBlocks = compressedAllocationTable.charAt(endIndex) - '0';
          }

          long fileId = (long) endIndex / 2L;
          checkSum = checkSum.add(BigInteger.valueOf(fileId * blockIndex++));
          remainingBlocks -= 1;
          if (remainingBlocks == 0) {
            endIndex -= 2;
          }
        }
      }
    }

    System.out.println("Part One: " + checkSum);
  }

  private static void solvePartTwo() throws Exception {
    String compressedAllocationTable = Utility.readEntireFile("_2024/puzzle/_09/input.txt");
    NavigableSet<Block> freeSpace = new TreeSet<>(Block.ORDER_BY_OFFSET);
    Set<Block> movedFiles = new HashSet<>();
    Block firstBlock = new Block(0, 0, compressedAllocationTable.charAt(0) - '0');
    Block currentBlock = firstBlock;

    for (int index = 1; index < compressedAllocationTable.length(); index++) {
      int id = index / 2;
      if (index % 2 == 1) {
        id = (id * -1) - 1;
      }
      int occurrences = compressedAllocationTable.charAt(index) - '0';
      Block nextBlock = new Block(id, currentBlock.nextOffset(), occurrences);
      nextBlock.insertAfter(currentBlock);
      currentBlock = nextBlock;
      if (nextBlock.isFreeSpace()) {
        freeSpace.add(currentBlock);
      }
    }

    Block targetBlock = currentBlock;
    while (targetBlock.getPrevious() != null) {
      Block nextInChain = targetBlock.getPrevious();
      Iterator<Block> blockIterator = freeSpace.iterator();
      if (!targetBlock.isFreeSpace() && targetBlock.getSize() > 0 && !movedFiles.contains(targetBlock)) {
        while (blockIterator.hasNext()) {
          Block block = blockIterator.next();
          if (targetBlock.getSize() <= block.getSize() && targetBlock.getOffset() > block.getOffset()) {
            Block newFreeSpace = new Block(targetBlock.getId() * -1, 0, targetBlock.getSize());
            newFreeSpace.insertAfter(targetBlock);
            targetBlock.insertBefore(block);
            block.setSize(block.getSize() - targetBlock.getSize());
            if (block.getSize() == 0) {
              blockIterator.remove();
            }
            movedFiles.add(targetBlock);
            break;
          }
        }
      }
      targetBlock = nextInChain;
    }

    Block checkSumBlock = firstBlock;
    BigInteger checkSum = BigInteger.ZERO;
    while (checkSumBlock != null) {
      if (checkSumBlock.getNext() != null) {
        checkSumBlock.getNext().setOffset(checkSumBlock.getOffset() + checkSumBlock.getSize());
      }
      checkSum = checkSum.add(checkSumBlock.calculateChecksum());
      checkSumBlock = checkSumBlock.getNext();
    }

    System.out.println("Part Two: " + checkSum);
  }

  private static class Block {

    public static final Comparator<Block> ORDER_BY_OFFSET = Comparator.comparing(Block::getOffset);

    private final long id;
    private long offset;
    private long size;
    private Block previous = null;
    private Block next = null;

    public Block(long id, long offset, long size) {
      this.id = id;
      this.offset = offset;
      this.size = size;
    }

    public long getId() {
      return id;
    }

    public long getOffset() {
      return offset;
    }

    public void setOffset(long offset) {
      this.offset = offset;
    }

    public long getSize() {
      return size;
    }

    public void setSize(long size) {
      this.size = size;
    }

    public Block getPrevious() {
      return previous;
    }

    private void setPrevious(Block previous) {
      this.previous = previous;
    }

    public void insertBefore(Block block) {
      remove();

      block.getPrevious().setNext(this);
      setPrevious(block.getPrevious());
      setOffset(this.previous.nextOffset() + 1);

      block.setPrevious(this);
      setNext(block);

      setOffset(this.previous.nextOffset());
      block.setOffset(nextOffset());
    }

    public Block getNext() {
      return next;
    }

    private void setNext(Block next) {
      this.next = next;
    }

    public void insertAfter(Block block) {
      remove();

      if (block.getNext() != null) {
        block.getNext().setPrevious(this);
      }
      setNext(block.getNext());

      block.setNext(this);
      setPrevious(block);

      setOffset(block.nextOffset());
      if (getNext() != null) {
        getNext().setOffset(nextOffset());
      }
    }

    public void remove() {
      if (previous != null) {
        previous.setNext(next);
        if (next != null) {
          next.setOffset(previous.nextOffset());
        }
      }
      if (next != null) {
        next.setPrevious(previous);
      }
    }

    public boolean isFreeSpace() {
      return id < 0;
    }

    public long nextOffset() {
      return offset + size;
    }

    public BigInteger calculateChecksum() {
      BigInteger result = BigInteger.ZERO;
      if (id >= 0) {
        for (int i = 0; i < size; i++) {
          result = result.add(BigInteger.valueOf(id).multiply(BigInteger.valueOf(offset + i)));
        }
      }

      return result;
    }

    public String toString() {
      if (id < 0) {
        return ".".repeat((int) size);
      }
      return Long.toString(id).repeat((int) size);
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;
      Block block = (Block) o;
      return id == block.id;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(id);
    }
  }
}
