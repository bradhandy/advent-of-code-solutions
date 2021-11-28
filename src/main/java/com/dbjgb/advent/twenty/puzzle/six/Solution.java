package com.dbjgb.advent.twenty.puzzle.six;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Set;

public class Solution {

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("twenty/puzzle/six/input.txt")) {
      Set<Character> distinctYesAnswers = new HashSet<>();
      Set<Character> questionsAllInGroupAnswered = new HashSet<>();
      int totalGroupDistinctYesAnswers = 0;
      int totalDistinctYesAnswersByEntireGroup = 0;
      int numberOfPeopleInGroup = 0;
      String line;
      while ((line = inputReader.readLine()) != null) {
        Set<Character> questionsAnsweredByPerson = new HashSet<>();
        if (line.length() == 0 || line.trim().length() == 0) {
          totalGroupDistinctYesAnswers += distinctYesAnswers.size();
          totalDistinctYesAnswersByEntireGroup += questionsAllInGroupAnswered.size();
          distinctYesAnswers.clear();
          questionsAllInGroupAnswered.clear();
          numberOfPeopleInGroup = 0;
          continue;
        }

        for (int i = 0; i < line.length(); i++) {
          distinctYesAnswers.add(line.charAt(i));
          questionsAnsweredByPerson.add(line.charAt(i));
        }

        numberOfPeopleInGroup += 1;
        if (questionsAllInGroupAnswered.isEmpty() && numberOfPeopleInGroup == 1) {
          questionsAllInGroupAnswered.addAll(questionsAnsweredByPerson);
        } else {
          questionsAllInGroupAnswered.retainAll(questionsAnsweredByPerson);
        }
      }

      System.out.printf(
          "Total Distinct Yes Answers:  %d\n",
          totalGroupDistinctYesAnswers + distinctYesAnswers.size());
      System.out.printf(
          "Total Distinct Yes Answers for Entire Group:  %d\n",
          totalDistinctYesAnswersByEntireGroup + questionsAllInGroupAnswered.size());
    }
  }
}
