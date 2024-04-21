package net.fiendishplatypus.contributor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

  public NavigableMap<ContributorId, Contributor> calculate(List<String> allLines) {
    Map<String, List<Contributor.LineFromGitLog>> linesByName = fromCsv(allLines);

    //yep people with same name and with same contribution date will be grouped together.
    Comparator<ContributorId> comp =
      Comparator.comparing(ContributorId::firstContribution)
        .thenComparing(ContributorId::name);

    TreeMap<ContributorId, Contributor> out = new TreeMap<>(comp);

    linesByName.forEach((key, lineFromGitLogs) -> {
      String name = alias(key);
      List<LocalDate> dts = lineFromGitLogs.stream().map(Contributor.LineFromGitLog::dt).sorted().toList();
      List<Period> contributionPeriods = Period.fromLocalDates(dts, allowedContributionPauseDays);

      boolean moreThanOneContribution = !Contributor.onePeriodOneDayLength(contributionPeriods);
      if (moreThanOneContribution) {
        Contributor x = new Contributor(name, contributionPeriods);
        out.put(new ContributorId(name, dts.getFirst()), x);
      }
    });

    return out;
  }

  private String alias(String name) {
    String aliasFound = nameAliases.putIfAbsent(name, name);
    return aliasFound == null ? name : aliasFound;
  }

  private Map<String, List<Contributor.LineFromGitLog>> fromCsv(List<String> lines) {
    return lines.stream()
      .map(s -> s.split(","))
      .map(a -> {
        String dateString = a[2].contains("@") ? a[3] : a[2];
        LocalDate parsedDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        // there is no guarantee that history will contain clean names
        // alias map exist to cleanup all history names
        String aliasedName = alias(a[0]);
        return new Contributor.LineFromGitLog(aliasedName, a[1], parsedDate);
      }).collect(Collectors.groupingBy(Contributor.LineFromGitLog::name));
  }
}
