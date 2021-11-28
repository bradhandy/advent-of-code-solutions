package com.dbjgb.advent.fifteen.puzzle.thirteen;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern HAPPINESS_PATTERN =
      Pattern.compile("([^ ]+) would (lose|gain) (\\d+).*?([^ ]+)\\.");

  public static void main(String... args) throws Exception {
    List<Person> people = parsePeople();
    int maxHappiness = calculateMaximumHappinessArrangementFor(people);
    System.out.printf("Max Happiness (w/o Yourself):  %d\n", maxHappiness);

    people = addYourselfTo(people);
    maxHappiness = calculateMaximumHappinessArrangementFor(people);
    System.out.printf("Max Happiness (w/ Yourself):  %d\n", maxHappiness);
  }

  private static List<Person> addYourselfTo(List<Person> people) {
    Person me = new Person("me");
    for (Person person : people) {
      me.addHappinessPoints(person, 0);
      person.addHappinessPoints(me, 0);
    }

    List<Person> mutablePeople = new ArrayList<>(people);
    mutablePeople.add(0, me);

    return List.copyOf(mutablePeople);
  }

  private static int calculateMaximumHappinessArrangementFor(List<Person> people) {
    Person headOfTheTable = people.get(0);
    Set<Person> remainingGuests = new HashSet<>(people);
    remainingGuests.remove(headOfTheTable);

    int maxHappiness = 0;
    for (int i = 1; i < people.size(); i++) {
      Person rightHandPerson = people.get(i);
      remainingGuests.remove(rightHandPerson);

      maxHappiness =
          Integer.max(
              maxHappiness,
              maxHappinessForAllPossibleAggrangements(
                  List.of(headOfTheTable, rightHandPerson), List.copyOf(remainingGuests)));

      remainingGuests.add(rightHandPerson);
    }
    return maxHappiness;
  }

  private static List<Person> parsePeople() throws IOException {
    Map<String, Person> peopleMap = new HashMap<>();

    String line;
    try (BufferedReader inputReader = Utility.openInputFile("fifteen/puzzle/thirteen/input.txt")) {
      while ((line = inputReader.readLine()) != null) {
        Matcher personalHappinessMatcher = HAPPINESS_PATTERN.matcher(line);
        if (personalHappinessMatcher.matches()) {
          Person currentPerson =
              Optional.ofNullable(peopleMap.get(personalHappinessMatcher.group(1)))
                  .orElseGet(() -> new Person(personalHappinessMatcher.group(1)));
          Person contingentPerson =
              Optional.ofNullable(peopleMap.get(personalHappinessMatcher.group(4)))
                  .orElseGet(() -> new Person(personalHappinessMatcher.group(4)));
          int happinessPoints =
              Integer.parseInt(personalHappinessMatcher.group(3))
                  * (personalHappinessMatcher.group(2).equals("lose") ? -1 : 1);

          currentPerson.addHappinessPoints(contingentPerson, happinessPoints);
          peopleMap.put(currentPerson.getName(), currentPerson);
          peopleMap.put(contingentPerson.getName(), contingentPerson);
        }
      }
    }

    return List.copyOf(peopleMap.values());
  }

  private static int maxHappinessForAllPossibleAggrangements(
      List<Person> tableArrangement, List<Person> additionalGuests) {
    if (additionalGuests.isEmpty()) {
      return calculateHappiness(tableArrangement);
    }

    Set<Person> remainingGuests = new HashSet<>(additionalGuests);
    int maxHappiness = 0;
    for (int i = 0; i < additionalGuests.size(); i++) {
      int insertionPoint = tableArrangement.size() / 2;
      List<Person> mutableTableArrangement = new ArrayList<>(tableArrangement);
      Person nextPersonInArrangement = additionalGuests.get(i);
      mutableTableArrangement.add(insertionPoint++, nextPersonInArrangement);
      remainingGuests.remove(nextPersonInArrangement);

      // handle the odd number of guests case.  if there are no more remaining guests, then
      // return the calculated happiness.
      if (remainingGuests.isEmpty()) {
        return calculateHappiness(List.copyOf(mutableTableArrangement));
      }

      for (int j = i + 1; (j % additionalGuests.size()) != i; j++) {
        Person nextToLastPersonInArrangement = additionalGuests.get(j % additionalGuests.size());
        mutableTableArrangement.add(insertionPoint, nextToLastPersonInArrangement);
        remainingGuests.remove(nextToLastPersonInArrangement);

        maxHappiness =
            Integer.max(
                maxHappiness,
                maxHappinessForAllPossibleAggrangements(
                    List.copyOf(mutableTableArrangement), List.copyOf(remainingGuests)));

        remainingGuests.add(nextToLastPersonInArrangement);
        mutableTableArrangement.remove(insertionPoint);
      }

      remainingGuests.add(nextPersonInArrangement);
    }

    return maxHappiness;
  }

  private static int calculateHappiness(List<Person> tableArrangement) {
    int totalHappiness = 0;

    for (int i = 0; i < tableArrangement.size(); i++) {
      Person currentPerson = tableArrangement.get(i);
      Person nextPerson = tableArrangement.get((i + 1) % tableArrangement.size());
      totalHappiness +=
          currentPerson.getHappinessContingentOn(nextPerson)
              + nextPerson.getHappinessContingentOn(currentPerson);
    }

    return totalHappiness;
  }

  private static class Person {

    private final String name;
    private Map<Person, Integer> happinessPoints;

    public Person(String name) {
      this.name = name;
      this.happinessPoints = new HashMap<>();
    }

    public String getName() {
      return name;
    }

    public void addHappinessPoints(Person contingentPerson, int happinessDifference) {
      happinessPoints.put(contingentPerson, happinessDifference);
    }

    public int getHappinessContingentOn(Person contingentPerson) {
      return happinessPoints.get(contingentPerson);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Person person = (Person) o;
      return name.equals(person.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }
  }
}
