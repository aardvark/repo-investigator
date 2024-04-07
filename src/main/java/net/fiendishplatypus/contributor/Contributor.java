package net.fiendishplatypus.contributor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class Contributor {

  private final String id;
  private final int historicalId;
  private final Set<String> aliases;
  private final List<Period> periods;

  public Contributor(String id, int historicalId, Set<String> aliases, List<Period> contributionPeriods) {
    this.id = id;
    this.historicalId = historicalId;
    this.aliases = aliases;
    this.periods = contributionPeriods;
  }

  static boolean onePeriodOneDayLength(List<Period> xs) {
    return xs.size() == 1 && xs.getFirst().length() == 1;
  }

  public boolean activeOnDate(LocalDate dateOfCheck) {
    //shortcut out of bounds check
    if (dateOfCheck.isBefore(periods.getFirst().from())) {
      return false;
    }

    if (dateOfCheck.isAfter(periods.getLast().to())) {
      return false;
    }

    for (Period period : periods) {
      boolean betweenOrMatchingBorders =
        (dateOfCheck.isEqual(period.from()) || period.from().isBefore(dateOfCheck)) ||
        (dateOfCheck.isEqual(period.to()) || period.to().isAfter(dateOfCheck));
      if (betweenOrMatchingBorders) return true;
    }
    return false;
  }

  public String getId() {
    return id;
  }

  public int getHistoricalId() {
    return historicalId;
  }

  public Set<String> getAliases() {
    return aliases;
  }

  public List<Period> getPeriods() {
    return periods;
  }

  @Override
  public String toString() {
    return "Contributor{" + "id='" + id + '\'' + ", periods=" + periods + '}';
  }
}
