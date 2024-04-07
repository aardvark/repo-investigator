package net.fiendishplatypus.contributor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ContributionPeriodsCalculator {

  private final int allowedContributionPauseDays;

  public ContributionPeriodsCalculator(int allowedContributionPauseDays) {
    this.allowedContributionPauseDays = allowedContributionPauseDays;
  }

  public NavigableMap<ContributorId, Contributor> calculate(List<String> allLines) throws IOException {
    Map<String, List<ContributionLogLine>> linesByName = fromCsv(allLines);

    TreeMap<ContributorId, Contributor> out = new TreeMap<>(Comparator.comparing(ContributorId::firstContribution));

    for (Map.Entry<String, List<ContributionLogLine>> nameAndDates : linesByName.entrySet()) {
      String s = nameAndDates.getKey();
      List<ContributionLogLine> contributionLogLines = nameAndDates.getValue();
      List<LocalDate> dts = contributionLogLines.stream().map(ContributionLogLine::dt).sorted().toList();
      List<Period> contributionPeriods = Period.fromLocalDates(dts, allowedContributionPauseDays);

      boolean moreThanOneContribution = !Contributor.onePeriodOneDayLength(contributionPeriods);
      if (moreThanOneContribution) {
        out.put(new ContributorId(s, dts.getFirst()), new Contributor(s, -1, Collections.emptySet(), contributionPeriods));
      }
    }

    return out;
  }

  private static Map<String, List<ContributionLogLine>> fromCsv(List<String> lines) {
    return lines.stream()
      .map(s -> s.split(","))
      .map(a -> {
        return new ContributionLogLine(a[0], LocalDate.parse(a[2].contains("@")? a[3] : a[2], DateTimeFormatter.ISO_LOCAL_DATE));
      })
      .collect(Collectors.groupingBy(ContributionLogLine::name));
  }
}
