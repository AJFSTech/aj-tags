package tech.ajfs.ajtags.persistence;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public enum PersistenceType {

  MARIADB("maria", "mariadb"),
  MYSQL("mysql"),
  SQLITE("sqlite", "sqlite3");

  private final Set<String> names;

  PersistenceType(String... names) {
    this.names = new HashSet<String>(Arrays.asList(names));
  }

  public static PersistenceType fromString(String name, PersistenceType def) {
    name = name.toLowerCase(Locale.ROOT);
    for (PersistenceType type : values()) {
      if (type.names.contains(name)) {
        return type;
      }
    }
    return def;
  }

}
