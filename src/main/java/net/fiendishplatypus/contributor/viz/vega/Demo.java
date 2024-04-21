package net.fiendishplatypus.contributor.viz.vega;

import net.fiendishplatypus.contributor.ContributionPeriodsCalculator;
import net.fiendishplatypus.contributor.Contributor;
import net.fiendishplatypus.contributor.ContributorId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

public class Demo {
  public static void main(String[] args) throws IOException {
    List<String> allLines = Files.readAllLines(Paths.get(args[0]));

    Map<String, String> aliases = new HashMap<>();
    ContributionPeriodsCalculator calc = new ContributionPeriodsCalculator(32);
    NavigableMap<ContributorId, Contributor> xs = calc.calculate(allLines);
    Exporter exporter = new Exporter(false);

/*
    System.out.println(xs.size());
    Iterator<Map.Entry<ContributorId, Contributor>> iterator = xs.entrySet().iterator();
    Map.Entry<ContributorId, Contributor> first = iterator.next();
    System.out.println(first);

    System.out.println(exporter.toDataSection(Collections.singleton(first.getValue())));
    System.out.println(exporter.toDataSection(Collections.singleton(iterator.next().getValue())));
    System.out.println(exporter.toDataSection(Collections.singleton(iterator.next().getValue())));
    System.out.println(exporter.toDataSection(Collections.singleton(iterator.next().getValue())));
    System.out.println(exporter.toDataSection(Collections.singleton(iterator.next().getValue())));
    System.out.println(exporter.toDataSection(Collections.singleton(iterator.next().getValue())));
*/


    Files.writeString(Paths.get("contribution-graph.json"), exporter.toDataSection(xs.values()));


/*    System.out.println("Active on date:");
    for (int i = 1; i < 365; i++) {
      LocalDate dt = LocalDate.ofYearDay(2021, i);
      int n = new ActiveOnDate().activeOnDate(xs.values(), dt);
      if (n > 0) {
        System.out.println("["+dt+"]:" + n);
      }
    }*/
  }

}