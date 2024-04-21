package net.fiendishplatypus.contributor;

import java.time.LocalDate;
import java.util.List;

public class Contributor {

  private final String id;
  private final List<Period> periods;

  public Contributor(String id, List<Period> contributionPeriods) {
    this.id = id;
    this.periods = contributionPeriods;
  }

  static boolean onePeriodOneDayLength(List<Period> xs) {
    return xs.size() == 1 && xs.get(0).length() == 1;
  }

  public boolean activeOnDate(LocalDate dateOfCheck) {
    //shortcut out of bounds check
    if (dateOfCheck.isBefore(periods.get(0).from())) {
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

  public List<Period> getPeriods() {
    return periods;
  }

  @Override
  public String toString() {
    return "Contributor{" + "name='" + id + '\'' + ", periods=" + periods + '}';
  }

  public record LineFromGitLog(String name, String email, LocalDate dt) {}
}
