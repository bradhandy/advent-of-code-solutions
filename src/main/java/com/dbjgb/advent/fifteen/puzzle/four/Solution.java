package com.dbjgb.advent.fifteen.puzzle.four;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Solution {

  public static void main(String... args) throws Exception {
    printValueProducingHexHashStartingWith("00000");
    printValueProducingHexHashStartingWith("000000");
  }

  private static void printValueProducingHexHashStartingWith(String expectedPrefix)
      throws NoSuchAlgorithmException, CloneNotSupportedException {
    String md5Hash = null;
    MessageDigest baseInstance = MessageDigest.getInstance("MD5");
    baseInstance.update("yzbqklnj".getBytes());

    for (int i = 0; md5Hash == null || !md5Hash.startsWith(expectedPrefix); i++) {
      MessageDigest md5 = (MessageDigest) baseInstance.clone();
      md5.update(String.valueOf(i).getBytes());
      md5Hash = String.format("%032x", new BigInteger(1, md5.digest()));
      if (md5Hash.startsWith(expectedPrefix)) {
        System.out.printf("Hash:  %s; Value: %d\n", md5Hash, i);
      }
    }
  }
}

/*
0 = hg7U7EmcOzn6C1p8RLcNRQ==
100000000 = muD1RlB8GlsY5QoPn5JnOQ==
200000000 = 0FrK10/q4mNLHSckG18Hug==
300000000 = BBeMuU+DPqbE+WJzd4Fruw==
 */
