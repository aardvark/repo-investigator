package net.fiendishplatypus.contributor;

import java.time.LocalDate;
import java.util.Collection;

public class ActiveOnDate {
  public int activeOnDate(Collection<Contributor> xs, LocalDate date) {
    int out = 0;
    for (Contributor x : xs) {
      if (x.activeOnDate(date)) {
        out++;
      }
    }
    return out;
  }
}
