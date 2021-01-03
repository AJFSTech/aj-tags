package tech.ajfs.ajtags.persistence;

import lombok.RequiredArgsConstructor;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.persistence.impl.file.SqlitePersistenceImplementation;
import tech.ajfs.ajtags.persistence.impl.sql.MariaDBPersistenceImplementation;
import tech.ajfs.ajtags.persistence.impl.sql.MySQLPersistenceImplementation;

@RequiredArgsConstructor
public class PersistenceFactory {

  private final AJTags plugin;

  public Persistence getInstance(PersistenceOptions options) {
    Persistence persistence;

    switch (options.getType()) {
      case MYSQL:
        persistence = new Persistence(plugin, new MySQLPersistenceImplementation(options));
        break;
      case MARIADB:
        persistence = new Persistence(plugin, new MariaDBPersistenceImplementation(options));
        break;
      case SQLITE:
        persistence = new Persistence(plugin, new SqlitePersistenceImplementation());
        break;
      default:
        throw new IllegalArgumentException("Unknown database type");
    }

    persistence.init(this.plugin);
    return persistence;
  }

}
