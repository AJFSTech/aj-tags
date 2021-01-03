package tech.ajfs.ajtags.persistence;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PersistenceOptions {

  private final PersistenceType type;
  private final String host;
  private final String port;
  private final String database;
  private final String username;
  private final String password;
  private final String tablePrefix;

  public static PersistenceOptions fromSection(ConfigurationSection section) {
    String typeString = section.getString("type");
    PersistenceType type = PersistenceType.fromString(typeString, PersistenceType.SQLITE);

    if (type == null) {
      return null;
    }

    return new PersistenceOptions(
        type,
        section.getString("host"),
        section.getString("port"),
        section.getString("database"),
        section.getString("username"),
        section.getString("password"),
        section.getString("table-prefix")
    );
  }
}
