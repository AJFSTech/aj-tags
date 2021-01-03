package tech.ajfs.ajtags.persistence;

import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.persistence.impl.sql.MariaDatabase;
import tech.ajfs.ajtags.persistence.impl.sql.MySqlDatabase;
import tech.ajfs.ajtags.persistence.impl.sql.SqliteDatabase;

public class DatabaseFactory {

  private final DatabaseOptions options;

  public DatabaseFactory(@NotNull DatabaseOptions options) {
    this.options = options;
  }

  public AJTagsDatabase createDatabase() {
    switch (options.getType()) {
      case MYSQL:
        return new MySqlDatabase(options);
      case MARIADB:
        return new MariaDatabase(options);
      case SQLITE:
        return new SqliteDatabase(options);
    }

    return null;
  }

}
