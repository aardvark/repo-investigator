package net.fiendishplatypus.contributor.viz.vega;

import net.fiendishplatypus.contributor.Contributor;
import net.fiendishplatypus.contributor.Period;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;

public class Exporter {
  /**
   * When sets to true generate data with contributor ids anonimized.
   */
  private final boolean anonymize;

  public Exporter() {
    this.anonymize = true;
  }

  public Exporter(boolean anonymize) {
    this.anonymize = anonymize;
  }


  public String toDataSection(Collection<Contributor> xs) {
    Map<String, String> anonymousIds = new HashMap<>();
    AtomicInteger i = new AtomicInteger(0);

    StringJoiner sj = new StringJoiner(",\n");
    for (Contributor x : xs) {
      String id = anonymize ? anonymousIds.computeIfAbsent(x.getId(), __ -> "#" + i.incrementAndGet()) : x.getId();
      List<Period> periods = x.getPeriods();
      for (Period period : periods) {
        //@formatter:off
        //build json by hand because we can
        String val =
        "{" +
        " \"label\": \"" + id            + "\"," +
        " \"from\": \""  + period.from() + "\"," +
        " \"to\": \""    + period.to()   + "\"" +
        "}";
        //@formatter:on
        sj.add(val);
      }
    }

    //json allows hanging comma in the object
    //for ease of merging multiple imports we add it this here
    return sj + ",";
  }
}
