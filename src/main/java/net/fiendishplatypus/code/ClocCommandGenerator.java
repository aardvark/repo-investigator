package net.fiendishplatypus.code;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class ClocCommandGenerator {

  //cloc-2.00.exe --vcs=git --git 2004-02-01 --xml --out=../cloc-2004-02-01.xml
  //git branch -f 2004-02-01 $(git log -1 --before=2004-02-01 --format=format:"%H")
  //cloc-2.00.exe --vcs=git --git {date} --xml --out=../cloc-{date}.xml

  Function<LocalDate, String> branch = x -> MessageFormat.format(
    "git branch -f {0} $(git log -1 --before={0} --format=format:\"%H\"", x);

  Function<LocalDate, String> cloc = x -> MessageFormat.format(
    "cloc-2.00.exe --vcs=git --git {0} --xml --out=../cloc-{0}.xml", x);


  public Collection<String> gitBranch(List<LocalDate> xs) {
    return xs.stream()
      .map(branch)
      .collect(toList());
  }

  public Collection<String> cloc(List<LocalDate> xs) {
    return xs.stream()
      .map(cloc)
      .collect(toList());
  }
}
