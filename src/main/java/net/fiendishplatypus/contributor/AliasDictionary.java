package net.fiendishplatypus.contributor;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringJoiner;

public class AliasDictionary {
  LinkedHashMap<String, String> aliases = new LinkedHashMap<>();
  private final String techUser;
  private final Set<String> toSkip;

  public AliasDictionary(String techUser) {
    this.techUser = techUser;
    this.toSkip = new HashSet<>();
  }

  public static void main(String[] args) {
    Map<String, String> aliases;

    LinkedHashMap<Integer, String> matchingIds = new LinkedHashMap<>();
    matchingIds.put(1, "Ananas");
    matchingIds.put(2, "Pineapple");

    String name = "Ananas";
    aliases = new AliasDictionary("tech user").aliasDictionary(name, matchingIds);
  }

  public Map<String, String> aliasDictionary(String name, Map<Integer, String> matchingIds) {
    if (toSkip.contains(name)) { return aliases; }
    if (matchingIds.isEmpty()) {
      aliases.putIfAbsent(name, name);
      return aliases;
    }


    StringJoiner j = new StringJoiner("\n", "Matching contributor '" + name + "' name:\n", "");

    for (Map.Entry<Integer, String> idxToName : matchingIds.entrySet()) {
      int idx = idxToName.getKey();
      String _name = idxToName.getValue();
      j.add("(" + idx + ") " + _name);
    }

    System.out.println(j);
    System.out.println();

    boolean exit = false;
    do {
      System.out.println("Which to use? ");
      System.out.println("[1-9 choice, s - skip, p - use as is, t - tech user]");
      Scanner s = new Scanner(System.in);
      String next = s.next();

      switch (next) {
        case "s" -> {
          System.out.println(name + " skipped for aliasing");
          toSkip.add(name);
        }
        case "p" -> aliases.put(name, name);
        case "t" -> aliases.put(name, techUser);
        default -> {
          int choice = Integer.parseInt(next);
          if (matchingIds.containsKey(choice)) {
            String alias = matchingIds.get(choice);
            System.out.println("Set " + name + " to " + alias);
            aliases.put(alias, name);
            exit = true;
          } else {
            System.out.println("Choice " + choice + " not defined. Try again");
          }
        }
      }
    } while (!exit);

    System.out.println(aliases);
    return aliases;
  }

}
