package net.fiendishplatypus.code;

import java.time.LocalDate;

public record Loc(LocalDate dt, long nFiles, long nLines, String type) {}
