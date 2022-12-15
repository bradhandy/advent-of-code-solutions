package com.dbjgb.advent._2015.puzzle._19;

import com.dbjgb.advent.Utility;
import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern REPLACEMENT_PATTERN = Pattern.compile("(\\w+) => (\\w+)");
  private static final Multimap<String, String> REPLACEMENTS = ArrayListMultimap.create();
  private static final Map<String, Integer> MOLECULE_MAP = Maps.newHashMap();

  public static void main(String... args) throws Exception {
    String originalMolecule = null;
    try (BufferedReader inputReader = Utility.openInputFile("_2015/puzzle/_19/input.txt")) {
      String line;
      while ((line = inputReader.readLine()) != null) {
        if (line.trim().isEmpty()) {
          continue;
        }

        Matcher replacementMatcher = REPLACEMENT_PATTERN.matcher(line);
        if (replacementMatcher.matches()) {
          REPLACEMENTS.put(replacementMatcher.group(1), replacementMatcher.group(2));
        } else {
          originalMolecule = line;
        }
      }
    }

    Set<String> seedMolecules = Sets.newHashSet();
    for (Map.Entry<String, Collection<String>> replacementsEntry :
        REPLACEMENTS.asMap().entrySet()) {
      Pattern replacementPattern = Pattern.compile(replacementsEntry.getKey());
      Matcher moleculeMatcher = replacementPattern.matcher(originalMolecule);
      for (String replacementValue : replacementsEntry.getValue()) {
        while (moleculeMatcher.find()) {
          MatchResult matchResult = moleculeMatcher.toMatchResult();
          StringBuilder seedMoleculeBuilder =
              new StringBuilder(originalMolecule.substring(0, matchResult.start()));
          seedMoleculeBuilder.append(replacementValue);
          seedMoleculeBuilder.append(originalMolecule.substring(matchResult.end()));
          seedMolecules.add(seedMoleculeBuilder.toString());
        }
        moleculeMatcher.reset();
      }
    }

    System.out.printf(
        "%d distinct seed molecules from %s.\n", seedMolecules.size(), originalMolecule);

    Map<String, String> originIndex = Maps.newHashMap();
    for (Map.Entry<String, String> replacement : REPLACEMENTS.entries()) {
      originIndex.put(
          new StringBuilder(replacement.getValue()).reverse().toString(),
          new StringBuilder(replacement.getKey()).reverse().toString());
    }

    String molecule = new StringBuilder(originalMolecule).reverse().toString();
    Pattern replacementPattern = Pattern.compile(Joiner.on("|").join(originIndex.keySet()));
    int steps = 0;
    while (!molecule.equals("e")) {
      steps++;
      Matcher replacementMatcher = replacementPattern.matcher(molecule);
      if (replacementMatcher.find()) {
        MatchResult matchResult = replacementMatcher.toMatchResult();
        StringBuilder moleculeDeconstructor =
            new StringBuilder(molecule.substring(0, matchResult.start()));
        moleculeDeconstructor.append(originIndex.get(matchResult.group()));
        moleculeDeconstructor.append(molecule.substring(matchResult.end()));
        molecule = moleculeDeconstructor.toString();
      }
    }

    System.out.printf("Steps to create molecule: %d\n", steps);
  }
}
