package com.dbjgb.advent._2020.puzzle._04;

import com.dbjgb.advent.Utility;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern PASSPORT_FIELD_PATTERN =
      Pattern.compile("((?:[bie]yr)|(?:[he]cl)|(?:[cp]id)|hgt):([^\\s]+)");
  private static final List<String> REQUIRED_PASSPORT_FIELDS =
      List.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
  private static final Pattern VALID_HAIR_COLOR_PATTERN = Pattern.compile("#[0-9a-f]{6}");
  private static final Set<String> VALID_EYE_COLORS =
      Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
  private static final Pattern VALID_PASSPORT_ID_PATTERN = Pattern.compile("\\d{9}");
  private static final Pattern VALID_YEAR_PATTERN = Pattern.compile("\\d{4}");

  public static void main(String... args) throws Exception {
    try (BufferedReader inputReader = Utility.openInputFile("_2020/puzzle/_04/input.txt")) {
      Map<String, String> passportFields = new HashMap<>();
      int validMissingFieldPassports = 0;
      int validFieldValidationPassports = 0;
      boolean done = false;
      String line;
      while (!done) {
        line = inputReader.readLine();
        if (line != null && line.trim().length() > 0) {
        }

        if (line == null || line.length() == 0 || line.trim().length() == 0) {
          if (passportFields.keySet().containsAll(REQUIRED_PASSPORT_FIELDS)) {
            validMissingFieldPassports += 1;
          }
          if (passportIsValid(passportFields)) {
            validFieldValidationPassports += 1;
          }
          passportFields.clear();
          done = line == null;
          continue;
        }

        Matcher passportFieldMatcher = PASSPORT_FIELD_PATTERN.matcher(line);
        while (passportFieldMatcher.find()) {
          passportFields.put(passportFieldMatcher.group(1), passportFieldMatcher.group(2));
        }
      }

      System.out.printf("Valid passports w/o field validation:  %d\n", validMissingFieldPassports);
      System.out.printf(
          "Valid passports w/ field validation:  %d\n", validFieldValidationPassports);
    }
  }

  private static boolean passportIsValid(Map<String, String> passportFields) {
    if (!passportFields.keySet().containsAll(REQUIRED_PASSPORT_FIELDS)) {
      return false;
    }

    String birthYearString = passportFields.get("byr");
    if (!VALID_YEAR_PATTERN.matcher(birthYearString).matches()) {
      return false;
    }

    int birthYear = Integer.parseInt(birthYearString);
    if (birthYear < 1920 || 2002 < birthYear) {
      return false;
    }

    String issueYearString = passportFields.get("iyr");
    if (!VALID_YEAR_PATTERN.matcher(issueYearString).matches()) {
      return false;
    }

    int issueYear = Integer.parseInt(issueYearString);
    if (issueYear < 2010 || 2020 < issueYear) {
      return false;
    }

    String expirationYearString = passportFields.get("eyr");
    if (!VALID_YEAR_PATTERN.matcher(expirationYearString).matches()) {
      return false;
    }

    int expirationYear = Integer.parseInt(expirationYearString);
    if (expirationYear < 2020 || 2030 < expirationYear) {
      return false;
    }

    String heightString = passportFields.get("hgt");
    boolean measurementSystemValid = heightString.endsWith("cm") || heightString.endsWith("in");
    if (!measurementSystemValid) {
      return false;
    }

    int height = Integer.parseInt(heightString.replaceAll("(cm|in)", ""));
    boolean metric = heightString.endsWith("cm");
    if ((metric && (height < 150 || 193 < height)) || (!metric && (height < 59 || 76 < height))) {
      return false;
    }

    if (!VALID_HAIR_COLOR_PATTERN.matcher(passportFields.get("hcl")).matches()) {
      return false;
    }

    if (!VALID_EYE_COLORS.contains(passportFields.get("ecl"))) {
      return false;
    }

    if (!VALID_PASSPORT_ID_PATTERN.matcher(passportFields.get("pid")).matches()) {
      return false;
    }

    return true;
  }
}
