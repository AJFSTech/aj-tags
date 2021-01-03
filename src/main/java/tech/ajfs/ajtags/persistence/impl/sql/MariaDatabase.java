package tech.ajfs.ajtags.persistence.impl.sql;

import com.zaxxer.hikari.HikariConfig;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.persistence.DatabaseOptions;
import tech.ajfs.ajtags.persistence.impl.SqlDatabase;
import tech.ajfs.ajtags.tag.Tag;

public class MariaDatabase extends SqlDatabase {

  private final DatabaseOptions databaseOptions;
  private final String tagsDatabaseName;
  private final String playerTagsDatabaseName;

  public MariaDatabase(DatabaseOptions databaseOptions) {
    this.databaseOptions = databaseOptions;
    this.tagsDatabaseName = databaseOptions.getTablePrefix() + "tags";
    this.playerTagsDatabaseName = databaseOptions.getTablePrefix() + "playertags";
  }

  @Override
  protected @NotNull HikariConfig getHikariConfig(@NotNull File dataFolder) {
    HikariConfig config = new HikariConfig();
    config.setPoolName("AJTagsMariaDBPool");
    config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
    config.setJdbcUrl(
        "jdbc:mysql://" + databaseOptions.getAddress() + "/" + databaseOptions.getDatabase());

    if (!databaseOptions.getUsername().isEmpty()) {
      config.setUsername(databaseOptions.getUsername());
    }

    if (!databaseOptions.getPassword().isEmpty()) {
      config.setPassword(databaseOptions.getPassword());
    }

    config.setConnectionInitSql("SELECT 1");
    config.setMaxLifetime(60000);
    config.setIdleTimeout(45000);
    config.setMaximumPoolSize(8);

    // Adding config optimizations, taken from Aikar's "DB" project:
    // See: https://github.com/aikar/db/blob/master/core/src/main/java/co/aikar/idb/HikariPooledDatabase.java
    config.addDataSourceProperty("cachePrepStmts", true);
    config.addDataSourceProperty("prepStmtCacheSize", 250);
    config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
    config.addDataSourceProperty("useServerPrepStmts", true);
    config.addDataSourceProperty("cacheCallableStmts", true);
    config.addDataSourceProperty("cacheResultSetMetadata", true);
    config.addDataSourceProperty("cacheServerConfiguration", true);
    config.addDataSourceProperty("useLocalSessionState", true);
    config.addDataSourceProperty("elideSetAutoCommits", true);
    config.addDataSourceProperty("alwaysSendSetIsolation", false);

    return config;
  }

  @Override
  public boolean createTables() {
    try (Connection connection = this.dataSource.getConnection()) {
      try (PreparedStatement statement = connection.prepareStatement(
          "CREATE TABLE IF NOT EXISTS ? ("
              + "id INT(7) UNSIGNED NOT NULL AUTO_INCREMENT,"
              + "tag_name VARCHAR(32) NOT NULL,"
              + "tag VARCHAR(64) NOT NULL,"
              + "PRIMARY KEY (id)"
              + ");")) {
        statement.setString(1, this.tagsDatabaseName);
        statement.execute();
      }

      try (PreparedStatement statement = connection.prepareStatement(
          "CREATE TABLE IF NOT EXISTS ? ("
              + "uuid VARCHAR(36) NOT NULL,"
              + "tag_id INT(7) NOT NULL,"
              + "PRIMARY KEY(uuid, tag_id)"
              + ");")) {
        statement.setString(1, this.playerTagsDatabaseName);
        statement.execute();
      }
    } catch (SQLException err) {
      err.printStackTrace();
      return false;
    }

    return true;
  }

  @Override
  public void removeTag(@NotNull Player player) {

  }

  @Override
  public void setTag(@NotNull Player player, @Nullable Tag tag) {

  }

  @Override
  public @Nullable Tag getTag(@NotNull Player player) {
    return null;
  }
}
