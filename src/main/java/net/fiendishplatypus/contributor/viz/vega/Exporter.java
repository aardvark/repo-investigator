package net.fiendishplatypus.contributor.viz.vega;

import net.fiendishplatypus.contributor.Contributor;
import net.fiendishplatypus.contributor.Period;

import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public class Exporter {

  public String toDataSection(Collection<Contributor> xs) {
    StringJoiner sj = new StringJoiner(",\n");
    for (Contributor x : xs) {
      String id = x.getId();
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
