package tech.ajfs.ajtags.persistence;

import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.lang.enums.EnumUtils;
import org.bukkit.configuration.ConfigurationSection;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseOptions {

  enum DatabaseType {
    SQLITE,
    MARIADB,
    MYSQL;

    private static Map<String, DatabaseType> byName = Map.of(
        "sqlite", SQLITE,
        "mariadb", MARIADB,
        "mysql", MYSQL
    );

    public static DatabaseType fromString(String type) {
      return byName.get(type);
    }
  }

  private final DatabaseType type;
  private final String address;
  private final String database;
  private final String username;
  private final String password;
  private final String tablePrefix;

  public static DatabaseOptions fromSection(ConfigurationSection section) {
    String typeString = section.getString("type");
    DatabaseType type = DatabaseType.fromString(typeString);

    if (type == null) {
      return null;
    }

    return new DatabaseOptions(
        type,
        section.getString("address"),
        section.getString("database"),
        section.getString("username"),
        section.getString("password"),
        section.getString("table-prefix")
    );
  }
}
