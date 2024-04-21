package net.fiendishplatypus.contributor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record Period(LocalDate from, LocalDate to) {

  static List<Period> fromLocalDates(List<LocalDate> dates, int joinSkip) {
    List<LocalDate> dts = dates.stream().sorted().toList();

    LocalDate first = dts.getFirst();
    Period start = new Period(first, first);

    ArrayList<Period> acc = new ArrayList<>();
    for (LocalDate date : dates.subList(1, dts.size())) {
      if (start.to().plusDays(joinSkip).isAfter(date)) {
        start = new Period(start.from(), date);
      } else {
        acc.add(start);
        start = new Period(date, date);
      }
    }

    acc.add(start);
    return acc;
  }

  public long length() {
    return to.toEpochDay() - from.toEpochDay();
  }

}
