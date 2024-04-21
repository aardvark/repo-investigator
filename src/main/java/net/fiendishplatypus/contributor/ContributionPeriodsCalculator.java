package net.fiendishplatypus.contributor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ContributionPeriodsCalculator {

  private final Map<String, String> nameAliases;
  private final int allowedContributionPauseDays;

  public ContributionPeriodsCalculator(int allowedContributionPauseDays) {
    this.allowedContributionPauseDays = allowedContributionPauseDays;
    this.nameAliases = new HashMap<>();
  }

  public ContributionPeriodsCalculator(Map<String, String> aliases, int allowedContributionPauseDays) {
    this.allowedContributionPauseDays = allowedContributionPauseDays;
    this.nameAliases = aliases;
  }

  public NavigableMap<ContributorId, Contributor> calculate(List<String> allLines) throws IOException {
    Map<String, List<Contributor.LineFromGitLog>> linesByName = fromCsv(allLines);

    TreeMap<ContributorId, Contributor> out = new TreeMap<>(Comparator.comparing(ContributorId::firstContribution));

    for (Map.Entry<String, List<Contributor.LineFromGitLog>> nameAndDates : linesByName.entrySet()) {
      String name = alias(nameAndDates.getKey());
      List<Contributor.LineFromGitLog> lineFromGitLogs = nameAndDates.getValue();
      List<LocalDate> dts = lineFromGitLogs.stream().map(Contributor.LineFromGitLog::dt).sorted().toList();
      List<Period> contributionPeriods = Period.fromLocalDates(dts, allowedContributionPauseDays);

      boolean moreThanOneContribution = !Contributor.onePeriodOneDayLength(contributionPeriods);
      if (moreThanOneContribution) {
        Contributor x = new Contributor(name, -1, Collections.emptySet(), contributionPeriods);
        out.put(new ContributorId(name, dts.getFirst()), x);
      }
    }

    return out;
  }

  private String alias(String name) {
    String aliasFound = nameAliases.putIfAbsent(name, name);
    return aliasFound == null ? name : aliasFound;
  }

  private static Map<String, List<Contributor.LineFromGitLog>> fromCsv(List<String> lines) {
    return lines.stream()
      .map(s -> s.split(","))
      .map(a -> {
        String dateString = a[2].contains("@") ? a[3] : a[2];
        LocalDate parsedDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        return new Contributor.LineFromGitLog(a[0], a[1], parsedDate);
      }).collect(Collectors.groupingBy(Contributor.LineFromGitLog::name));
    //the string keys that we have here is the contribution names.
    //some contributors cna have more than one name. In this case
  }
}
